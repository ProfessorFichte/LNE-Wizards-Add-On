package com.lne_wizards.client.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class Particles {
    public static final DefaultParticleType FROST_RAY = FabricParticleTypes.simple();

    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "frost_ray"), FROST_RAY);
    }
}
