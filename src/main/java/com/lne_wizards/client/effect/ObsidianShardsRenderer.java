package com.lne_wizards.client.effect;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.spell_engine.api.render.OrbitingEffectRenderer;

import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class ObsidianShardsRenderer extends OrbitingEffectRenderer {
    public static final Identifier modelId_base = new Identifier(MOD_ID, "effect/obsidian_shards");

    private static final RenderLayer BASE_RENDER_LAYER =
            RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);


    public ObsidianShardsRenderer() {
        super(List.of(
                new Model(BASE_RENDER_LAYER, modelId_base)),
                0.75F,
                0.1F
        );
    }
}
