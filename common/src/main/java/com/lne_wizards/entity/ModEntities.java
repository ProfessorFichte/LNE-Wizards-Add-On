package com.lne_wizards.entity;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.entity.mob.elemental_evokers.*;
import com.lne_wizards.entity.spell_spawned.ExplosiveBubbleEntity;
import com.lne_wizards.entity.spell_spawned.IceWallEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<AirEvokerEntity> AIR_EVOKER = register(
            "air_evoker",
            EntityType.Builder.create(AirEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<ArcaneEvokerEntity> ARCANE_EVOKER = register(
            "arcane_evoker",
            EntityType.Builder.create(ArcaneEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<EarthEvokerEntity> EARTH_EVOKER = register(
            "earth_evoker",
            EntityType.Builder.create(EarthEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<FireEvokerEntity> FIRE_EVOKER = register(
            "fire_evoker",
            EntityType.Builder.create(FireEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<FrostEvokerEntity> FROST_EVOKER = register(
            "frost_evoker",
            EntityType.Builder.create(FrostEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<WaterEvokerEntity> WATER_EVOKER = register(
            "water_evoker",
            EntityType.Builder.create(WaterEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<ExplosiveBubbleEntity> EXPLOSIVE_BUBBLE = register(
            "explosive_bubble",
            EntityType.Builder.create(ExplosiveBubbleEntity::new, SpawnGroup.MISC)
                    .dimensions(1.5F, 1.5F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<IceWallEntity> ICE_WALL = register(
            "ice_wall",
            EntityType.Builder.create(IceWallEntity::new, SpawnGroup.MISC)
                    .dimensions(1.0F, 3.0F)
                    .maxTrackingRange(8)
    );

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(LNE_Wizards_Mod.MOD_ID, id),
                builder.build(id)
        );
    }

    public static void register() {
    }
}
