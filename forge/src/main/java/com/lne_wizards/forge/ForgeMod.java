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

    /**
     * Registration goes through the {@code RegisterHelper} that {@code RegisterEvent} hands out, NOT
     * through {@code Registry.register}. Forge only clears the vanilla {@code NamespacedWrapper}'s lock
     * from 47.4.0 onward; on 47.0-47.3 and NeoForge 1.20.1 it stays locked even inside the correct
     * window, so a plain {@code Registry.register} there throws
     * {@code Can not register to a locked registry}. {@code mods.toml} declares {@code [47,)}, so those
     * are supported configurations.
     *
     * <p>The loops below duplicate what {@code common} runs on Fabric, on purpose - the whole workaround
     * stays inside {@code forge/} and the Fabric path is untouched.
     *
     * <p>Forge fires one {@code RegisterEvent} per registry and only accepts writes into the registry
     * whose window is open. Measured window order on 47.4.22:
     * {@code sound_event -> fluid -> block -> attribute -> mob_effect -> particle_type -> item -> entity_type}.
     * Note {@code item} runs BEFORE {@code entity_type}, which is why {@link ModEntities} separates
     * building an {@code EntityType} from registering it: the spawn eggs are created in the {@code item}
     * window and each one needs its type instance.
     */
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.BLOCK, helper -> {
            // `new EntityType(...)` calls `Registries.ENTITY_TYPE.createEntry(this)` on Forge-patched
            // vanilla, so building a type needs the ENTITY_TYPE wrapper unfrozen. Forge unfreezes every
            // vanilla registry for the whole RegisterEvent phase (but only lets you *register* into the
            // one whose window is open), so building here - in the first window this mod uses - is fine,
            // while doing it in the mod constructor dies with "Registry is already frozen".
            LNE_Wizards_Mod.createEntities();
            // Touching `ModBlocks` runs its <clinit>, which constructs each block's BlockItem. That is
            // construction, not registration, and the RegisterEvent sequence has begun - so it is fine.
            for (var e : ModBlocks.all) {
                helper.register(new Identifier(LNE_Wizards_Mod.MOD_ID, e.name()), e.block());
            }
        });

        // `LNE_Wizards_Mod.init()` already refreshed `effectConfig`, and `registerEffects()` does not
        // save it - so this block mirrors it exactly.
        event.register(RegistryKeys.STATUS_EFFECT, helper ->
                LNE_WizardsEffects.effectsToRegister(LNE_Wizards_Mod.effectConfig.value)
                        .forEach(helper::register));

        event.register(RegistryKeys.ITEM, helper -> {
            for (var e : ModBlocks.all) {
                helper.register(new Identifier(LNE_Wizards_Mod.MOD_ID, e.name()), e.item());
            }
            // Loot & Explore is Fabric-only, so this branch is never taken on Forge today - it mirrors
            // `LNE_Wizards_Mod.registerItems()` exactly (config refresh and save included) so it stays
            // correct if that ever changes.
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

    /**
     * Forge 47's {@code AddPackFindersEvent} has no {@code addPackFinders(Identifier, ...)} convenience
     * overload (that is NeoForge); the built-in pack has to be assembled by hand from the mod file.
     */
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
            // PathPackResources(String packId, boolean isBuiltin, Path source) - the boolean is in the middle.
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

    /**
     * Forge 47's {@code BuildCreativeModeTabContentsEvent} has no {@code insertAfter} (that is NeoForge);
     * it exposes the backing {@code MutableHashedLinkedMap} instead, and {@code accept} takes a Supplier.
     */
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
