package com.lne_wizards.client;

import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.ExplosiveBubbleRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.spell.LneWizardSpells;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.spell_engine.client.gui.SpellTooltip;

public class LneWizardsClient {

    public static void init() {
        for (var entry : LneWizardSpells.entries) {
            if (entry.mutator() != null) {
                SpellTooltip.addDescriptionMutator(entry.id(), entry.mutator());
            }
        }
        registerEntityRenderers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.AIR_EVOKER, ElementalEvokerRenderer.Air::new);
        EntityRendererRegistry.register(ModEntities.ARCANE_EVOKER, ElementalEvokerRenderer.Arcane::new);
        EntityRendererRegistry.register(ModEntities.EARTH_EVOKER, ElementalEvokerRenderer.Earth::new);
        EntityRendererRegistry.register(ModEntities.FIRE_EVOKER, ElementalEvokerRenderer.Fire::new);
        EntityRendererRegistry.register(ModEntities.FROST_EVOKER, ElementalEvokerRenderer.Frost::new);
        EntityRendererRegistry.register(ModEntities.WATER_EVOKER, ElementalEvokerRenderer.Water::new);
        EntityRendererRegistry.register(ModEntities.EXPLOSIVE_BUBBLE, ExplosiveBubbleRenderer::new);
        EntityRendererRegistry.register(ModEntities.ICE_WALL, IceWallRenderer::new);
    }
}
