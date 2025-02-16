package com.lne_wizards.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

@Environment(EnvType.CLIENT)
public class LneWizardsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                new Identifier(MOD_ID, "projectile/starfall")
        ));
    }
}
