package com.lne_wizards.entity;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.entity.mob.elemental_evokers.*;
import com.lne_wizards.entity.spell_spawned.ExplosiveBubbleEntity;
import com.lne_wizards.entity.spell_spawned.IceWallEntity;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ModEntities {

    @Nullable public static EntityType<AirEvokerEntity> AIR_EVOKER = null;

    public static final EntityType<ArcaneEvokerEntity> ARCANE_EVOKER = registerType(
            "arcane_evoker",
            EntityType.Builder.create(ArcaneEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    @Nullable public static EntityType<EarthEvokerEntity> EARTH_EVOKER = null;

    public static final EntityType<FireEvokerEntity> FIRE_EVOKER = registerType(
            "fire_evoker",
            EntityType.Builder.create(FireEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<FrostEvokerEntity> FROST_EVOKER = registerType(
            "frost_evoker",
            EntityType.Builder.create(FrostEvokerEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6F, 1.95F)
                    .maxTrackingRange(8)
    );

    @Nullable public static EntityType<WaterEvokerEntity> WATER_EVOKER = null;

    public static final EntityType<ExplosiveBubbleEntity> EXPLOSIVE_BUBBLE = registerType(
            "explosive_bubble",
            EntityType.Builder.create(ExplosiveBubbleEntity::new, SpawnGroup.MISC)
                    .dimensions(1.5F, 1.5F)
                    .maxTrackingRange(8)
    );

    public static final EntityType<IceWallEntity> ICE_WALL = registerType(
            "ice_wall",
            EntityType.Builder.create(IceWallEntity::new, SpawnGroup.MISC)
                    .dimensions(1.0F, 3.0F)
                    .maxTrackingRange(8)
    );

    private static <T extends Entity> EntityType<T> registerType(String id, EntityType.Builder<T> builder) {
        return Registry.register(
                Registries.ENTITY_TYPE,
                Identifier.of(LNE_Wizards_Mod.MOD_ID, id),
                builder.build(id)
        );
    }

    public static void register() {
        if (FabricLoader.getInstance().isModLoaded("elemental_wizards_rpg")) {
            AIR_EVOKER = registerType("air_evoker",
                    EntityType.Builder.create(AirEvokerEntity::new, SpawnGroup.MONSTER)
                            .dimensions(0.6F, 1.95F).maxTrackingRange(8));
            EARTH_EVOKER = registerType("earth_evoker",
                    EntityType.Builder.create(EarthEvokerEntity::new, SpawnGroup.MONSTER)
                            .dimensions(0.6F, 1.95F).maxTrackingRange(8));
            WATER_EVOKER = registerType("water_evoker",
                    EntityType.Builder.create(WaterEvokerEntity::new, SpawnGroup.MONSTER)
                            .dimensions(0.6F, 1.95F).maxTrackingRange(8));
        }
    }
}
