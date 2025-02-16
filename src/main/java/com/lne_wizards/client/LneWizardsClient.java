package com.lne_wizards.client;

import com.lne_wizards.client.effect.ObsidianShardsRenderer;
import com.lne_wizards.effect.Effects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

@Environment(EnvType.CLIENT)
public class LneWizardsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CustomModels.registerModelIds(List.of(
                ObsidianShardsRenderer.modelId_base,
                new Identifier(MOD_ID, "projectile/starfall")
        ));

        CustomModelStatusEffect.register(Effects.OBSIDIAN_SHARDS, new ObsidianShardsRenderer());
    }
}
