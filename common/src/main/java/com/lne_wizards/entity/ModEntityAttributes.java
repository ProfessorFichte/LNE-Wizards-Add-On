package com.lne_wizards.entity;

import com.lne_wizards.entity.mob.elemental_evokers.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;

public class ModEntityAttributes {

    @FunctionalInterface
    public interface AttributeRegistrar {
        void register(EntityType<? extends MobEntity> type, DefaultAttributeContainer.Builder builder);
    }

    public static void register(AttributeRegistrar registrar) {
        if (ModEntities.AIR_EVOKER != null) {
            registrar.register(ModEntities.AIR_EVOKER, AirEvokerEntity.createAirEvokerAttributes());
        }

        registrar.register(ModEntities.ARCANE_EVOKER, ArcaneEvokerEntity.createArcaneEvokerAttributes());

        if (ModEntities.EARTH_EVOKER != null) {
            registrar.register(ModEntities.EARTH_EVOKER, EarthEvokerEntity.createEarthEvokerAttributes());
        }

        registrar.register(ModEntities.FIRE_EVOKER, FireEvokerEntity.createFireEvokerAttributes());
        registrar.register(ModEntities.FROST_EVOKER, FrostEvokerEntity.createFrostEvokerAttributes());

        if (ModEntities.WATER_EVOKER != null) {
            registrar.register(ModEntities.WATER_EVOKER, WaterEvokerEntity.createWaterEvokerAttributes());
        }
    }
}
