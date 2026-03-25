package com.lne_wizards.entity;

import com.lne_wizards.entity.mob.elemental_evokers.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class ModEntityAttributes {

    public static void register() {
        FabricDefaultAttributeRegistry.register(
                ModEntities.AIR_EVOKER,
                AirEvokerEntity.createAirEvokerAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                ModEntities.ARCANE_EVOKER,
                ArcaneEvokerEntity.createArcaneEvokerAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                ModEntities.EARTH_EVOKER,
                EarthEvokerEntity.createEarthEvokerAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                ModEntities.FIRE_EVOKER,
                FireEvokerEntity.createFireEvokerAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                ModEntities.FROST_EVOKER,
                FrostEvokerEntity.createFrostEvokerAttributes()
        );

        FabricDefaultAttributeRegistry.register(
                ModEntities.WATER_EVOKER,
                WaterEvokerEntity.createWaterEvokerAttributes()
        );
    }
}
