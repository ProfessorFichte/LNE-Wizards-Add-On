package com.lne_wizards.client;

import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.spell.LneWizardSpells;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.spell_engine.client.gui.SpellTooltip;

public class LneWizardsClient {

    public static void init() {
        for (var entry : LneWizardSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }
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
