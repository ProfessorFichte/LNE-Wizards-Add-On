package com.lne_wizards.client;

import com.lne_wizards.client.particle.Particles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SnowflakeParticle;

@Environment(EnvType.CLIENT)
public class LneWizardsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(Particles.FROST_RAY, SnowflakeParticle.Factory::new);
    }
}
