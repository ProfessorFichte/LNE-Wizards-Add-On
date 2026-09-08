package com.lne_wizards.forge;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.entity.ModSpawnEggs;
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
     * Forge 47 fires one {@code RegisterEvent} per registry and unlocks only that one, so every
     * registration has to sit in its own window. Measured window order on 47.4.22:
     * {@code sound_event -> fluid -> block -> attribute -> mob_effect -> particle_type -> item -> entity_type}.
     * Note {@code item} runs BEFORE {@code entity_type}, which is why {@link com.lne_wizards.entity.ModEntities}
     * separates building an {@code EntityType} from registering it: the spawn eggs are created in the
     * {@code item} window and each one needs its type instance.
     */
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.BLOCK, reg -> {
            // `new EntityType(...)` calls `Registries.ENTITY_TYPE.createEntry(this)` on Forge-patched
            // vanilla, so building a type needs the ENTITY_TYPE wrapper unfrozen. Forge unfreezes every
            // vanilla registry for the whole RegisterEvent phase (but only lets you *register* into the
            // one whose window is open), so building here - in the first window this mod uses - is fine,
            // while doing it in the mod constructor dies with "Registry is already frozen".
            LNE_Wizards_Mod.createEntities();
            LNE_Wizards_Mod.registerBlocks();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> LNE_Wizards_Mod.registerEffects());
        event.register(RegistryKeys.ITEM, reg -> {
            LNE_Wizards_Mod.registerBlockItems();
            LNE_Wizards_Mod.registerItems();
            LNE_Wizards_Mod.registerSpawnEggs();
        });
        event.register(RegistryKeys.ENTITY_TYPE, reg -> LNE_Wizards_Mod.registerEntities());
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
