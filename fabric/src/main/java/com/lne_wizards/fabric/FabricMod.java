package com.lne_wizards.fabric;

import com.lne_wizards.LNE_Wizards_Mod;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LNE_Wizards_Mod.init();
        LNE_Wizards_Mod.registerBlocks();
        LNE_Wizards_Mod.registerEntities();
        LNE_Wizards_Mod.registerEntityAttributes();
        LNE_Wizards_Mod.registerItems();
        LNE_Wizards_Mod.registerEffects();
        LNE_Wizards_Mod.registerSpawnEggs();
        registerElementalWizardsCompatPack();
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
