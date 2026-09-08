package com.lne_wizards.neoforge.client;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.client.LneWizardsClient;
import com.lne_wizards.client.entity.ElementalEvokerRenderer;
import com.lne_wizards.client.entity.IceWallRenderer;
import com.lne_wizards.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.spell_engine.client.gui.ConfigMenuScreen;

@EventBusSubscriber(modid = LNE_Wizards_Mod.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        LneWizardsClient.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (modContainer, parent) -> new ConfigMenuScreen(parent));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
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
