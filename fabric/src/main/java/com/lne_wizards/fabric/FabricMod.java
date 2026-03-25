package com.lne_wizards.fabric;

import com.lne_wizards.LNE_Wizards_Mod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        LNE_Wizards_Mod.init();
        LNE_Wizards_Mod.registerEntities();
        LNE_Wizards_Mod.registerEntityAttributes();
        LNE_Wizards_Mod.registerItems();
        LNE_Wizards_Mod.registerEffects();
        LNE_Wizards_Mod.registerSpawnEggs();
    }
}
