package com.lne_wizards.client;

import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.spell.LneWizardSpells;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class LneWizardsClient {

    public static void init() {
        // Description values that aren't expressible as declarative `{token}`s. `TooltipTokens` is
        // server-safe; it is registered here simply because the tooltip is a client concern.
        LneWizardSpells.registerTooltipTokens();

        registerEntityRenderers();
        for(var entry : ModBlocks.all){
            if (entry.name().contains("magic_orb")) {
                BlockRenderLayerMap.INSTANCE.putBlock(entry.block(), RenderLayer.getCutout());
            }
        }
    }

    public static void registerEntityRenderers() {
        if (ModEntities.AIR_EVOKER != null) {
            EntityRendererRegistry.register(ModEntities.AIR_EVOKER, ElementalEvokerRenderer.Air::new);
        }
        EntityRendererRegistry.register(ModEntities.ARCANE_EVOKER, ElementalEvokerRenderer.Arcane::new);
        if (ModEntities.EARTH_EVOKER != null) {
            EntityRendererRegistry.register(ModEntities.EARTH_EVOKER, ElementalEvokerRenderer.Earth::new);
        }
        EntityRendererRegistry.register(ModEntities.FIRE_EVOKER, ElementalEvokerRenderer.Fire::new);
        EntityRendererRegistry.register(ModEntities.FROST_EVOKER, ElementalEvokerRenderer.Frost::new);
        if (ModEntities.WATER_EVOKER != null) {
            EntityRendererRegistry.register(ModEntities.WATER_EVOKER, ElementalEvokerRenderer.Water::new);
        }
        EntityRendererRegistry.register(ModEntities.ICE_WALL, IceWallRenderer::new);
    }

}
