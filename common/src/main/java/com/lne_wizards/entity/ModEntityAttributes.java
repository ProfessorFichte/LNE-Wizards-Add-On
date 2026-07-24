package com.lne_wizards.entity;

import com.lne_wizards.entity.mob.elemental_evokers.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {

    public static void register() {
        if (ModEntities.AIR_EVOKER != null) {
            FabricDefaultAttributeRegistry.register(ModEntities.AIR_EVOKER, AirEvokerEntity.createAirEvokerAttributes());
        }

        FabricDefaultAttributeRegistry.register(ModEntities.ARCANE_EVOKER, ArcaneEvokerEntity.createArcaneEvokerAttributes());

        if (ModEntities.EARTH_EVOKER != null) {
            FabricDefaultAttributeRegistry.register(ModEntities.EARTH_EVOKER, EarthEvokerEntity.createEarthEvokerAttributes());
        }

        FabricDefaultAttributeRegistry.register(ModEntities.FIRE_EVOKER, FireEvokerEntity.createFireEvokerAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.FROST_EVOKER, FrostEvokerEntity.createFrostEvokerAttributes());

        if (ModEntities.WATER_EVOKER != null) {
            FabricDefaultAttributeRegistry.register(ModEntities.WATER_EVOKER, WaterEvokerEntity.createWaterEvokerAttributes());
        }
    }
}
