package com.lne_wizards.fabric;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.entity.ModSpawnEggs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LNE_Wizards_Mod.init();
        LNE_Wizards_Mod.registerBlocks();
        LNE_Wizards_Mod.registerEntities();
        LNE_Wizards_Mod.registerEntityAttributes(FabricDefaultAttributeRegistry::register);
        LNE_Wizards_Mod.registerItems();
        LNE_Wizards_Mod.registerEffects();
        LNE_Wizards_Mod.registerSpawnEggs();
        registerItemGroupContents();
        registerElementalWizardsCompatPack();
    }

    private static void registerItemGroupContents() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            for (var e : ModBlocks.all) content.add(e.item());
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries ->
                entries.addAfter(Items.EVOKER_SPAWN_EGG, ModSpawnEggs.ALL_FOR_TAB.toArray(new SpawnEggItem[0]))
        );
    }

    private static void registerElementalWizardsCompatPack() {
        if (!FabricLoader.getInstance().isModLoaded(LNE_Wizards_Mod.ELEMENTAL_WIZARDS_MOD_ID)) {
            return;
        }
        FabricLoader.getInstance().getModContainer(LNE_Wizards_Mod.MOD_ID).ifPresent(container ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        Identifier.of(LNE_Wizards_Mod.MOD_ID, "elemental_wizards_compat"),
                        container,
                        ResourcePackActivationType.ALWAYS_ENABLED));
    }
}
