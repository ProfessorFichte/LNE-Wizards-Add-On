package com.lne_wizards.fabric.client;

import com.lne_wizards.client.LneWizardsClient;
import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LneWizardsClient.init();
        registerEntityRenderers();
    }

    private static void registerEntityRenderers() {
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
