package com.lne_wizards.forge;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.effect.LNE_WizardsEffects;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.entity.ModSpawnEggs;
import com.lne_wizards.item.WeaponRegister;
import net.spell_engine.Platform;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

@Mod(LNE_Wizards_Mod.MOD_ID)
@SuppressWarnings("removal")
public final class ForgeMod {
    public ForgeMod() {
        LNE_Wizards_Mod.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, ForgeMod::register);
        modBus.addListener(EventPriority.NORMAL, false, EntityAttributeCreationEvent.class, ForgeMod::registerAttributes);
        modBus.addListener(EventPriority.NORMAL, false, AddPackFindersEvent.class, ForgeMod::addPackFinders);
        modBus.addListener(EventPriority.NORMAL, false, BuildCreativeModeTabContentsEvent.class, ForgeMod::buildTabContents);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            com.lne_wizards.forge.client.ForgeClient.register(modBus);
        }
    }

    // Goes through the helper on purpose, on Forge 47.0-47.3 a plain Registry.register throws "Can not register to a locked registry".
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.BLOCK, helper -> {
            LNE_Wizards_Mod.createEntities();
            for (var e : ModBlocks.all) {
                helper.register(new Identifier(LNE_Wizards_Mod.MOD_ID, e.name()), e.block());
            }
        });

        event.register(RegistryKeys.STATUS_EFFECT, helper ->
                LNE_WizardsEffects.effectsToRegister(LNE_Wizards_Mod.effectConfig.value)
                        .forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            for (var e : ModBlocks.all) {
                helper.register(new Identifier(LNE_Wizards_Mod.MOD_ID, e.name()), e.item());
            }
            if (Platform.util().isModLoaded("loot_n_explore")) {
                LNE_Wizards_Mod.itemConfig.refresh();
                WeaponRegister.itemsToRegister(LNE_Wizards_Mod.itemConfig.value.weapons)
                        .forEach(helper::register);
                LNE_Wizards_Mod.itemConfig.save();
            }
            ModSpawnEggs.itemsToRegister().forEach(helper::register);
        });

        event.register(RegistryKeys.ENTITY_TYPE, helper ->
                ModEntities.typesToRegister().forEach(helper::register));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        LNE_Wizards_Mod.registerEntityAttributes((type, builder) -> event.put(type, builder.build()));
    }

    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != ResourceType.SERVER_DATA) {
            return;
        }
        if (!ModList.get().isLoaded(LNE_Wizards_Mod.ELEMENTAL_WIZARDS_MOD_ID)) {
            return;
        }
        var modFile = ModList.get().getModFileById(LNE_Wizards_Mod.MOD_ID);
        if (modFile == null) return;
        Path packPath = modFile.getFile().findResource(LNE_Wizards_Mod.ELEMENTAL_WIZARDS_COMPAT_PACK_PATH);
        event.addRepositorySource(consumer -> {
            var profile = ResourcePackProfile.create(
                    new Identifier(LNE_Wizards_Mod.MOD_ID, "elemental_wizards_compat").toString(),
                    Text.literal("LNE Wizards - Elemental Wizards Compat"),
                    true,
                    name -> new PathPackResources(name, true, packPath),
                    ResourceType.SERVER_DATA,
                    ResourcePackProfile.InsertionPosition.TOP,
                    ResourcePackSource.BUILTIN);
            if (profile != null) consumer.accept(profile);
        });
    }

    private static void buildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ItemGroups.BUILDING_BLOCKS)) {
            for (var e : ModBlocks.all) event.accept(() -> e.item());
        }
        if (event.getTabKey().equals(ItemGroups.SPAWN_EGGS)) {
            var anchor = new ItemStack(Items.EVOKER_SPAWN_EGG);
            for (var egg : ModSpawnEggs.ALL_FOR_TAB) {
                var eggStack = new ItemStack(egg);
                event.getEntries().putAfter(anchor, eggStack, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
                anchor = eggStack;
            }
        }
    }
}
