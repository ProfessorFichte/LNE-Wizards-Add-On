package com.lne_wizards.forge.client;

import com.lne_wizards.client.LneWizardsClient;
import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.spell_engine.client.gui.ConfigMenuScreen;

/**
 * No {@code @EventBusSubscriber} here on purpose: the class is only ever touched from
 * {@code ForgeMod} behind a {@code Dist.CLIENT} guard, so a dedicated server never classloads it.
 */
public class ForgeClient {

    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.NORMAL, false, FMLClientSetupEvent.class, ForgeClient::onClientSetup);
        modBus.addListener(EventPriority.NORMAL, false, EntityRenderersEvent.RegisterRenderers.class, ForgeClient::onRegisterRenderers);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        LneWizardsClient.init();
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new ConfigMenuScreen(parent)));
    }

    private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (ModEntities.AIR_EVOKER != null) {
            event.registerEntityRenderer(ModEntities.AIR_EVOKER, ElementalEvokerRenderer.Air::new);
        }
        event.registerEntityRenderer(ModEntities.ARCANE_EVOKER, ElementalEvokerRenderer.Arcane::new);
        if (ModEntities.EARTH_EVOKER != null) {
            event.registerEntityRenderer(ModEntities.EARTH_EVOKER, ElementalEvokerRenderer.Earth::new);
        }
        event.registerEntityRenderer(ModEntities.FIRE_EVOKER, ElementalEvokerRenderer.Fire::new);
        event.registerEntityRenderer(ModEntities.FROST_EVOKER, ElementalEvokerRenderer.Frost::new);
        if (ModEntities.WATER_EVOKER != null) {
            event.registerEntityRenderer(ModEntities.WATER_EVOKER, ElementalEvokerRenderer.Water::new);
        }
        event.registerEntityRenderer(ModEntities.ICE_WALL, IceWallRenderer::new);
    }
}
