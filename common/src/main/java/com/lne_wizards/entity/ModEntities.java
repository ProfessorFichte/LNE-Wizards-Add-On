package com.lne_wizards.entity;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.entity.mob.elemental_evokers.*;
import com.lne_wizards.entity.spell_spawned.IceWallEntity;
import net.spell_engine.Platform;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModEntities {

    @Nullable public static EntityType<AirEvokerEntity> AIR_EVOKER = null;
    @Nullable public static EntityType<ArcaneEvokerEntity> ARCANE_EVOKER = null;
    @Nullable public static EntityType<EarthEvokerEntity> EARTH_EVOKER = null;
    @Nullable public static EntityType<FireEvokerEntity> FIRE_EVOKER = null;
    @Nullable public static EntityType<FrostEvokerEntity> FROST_EVOKER = null;
    @Nullable public static EntityType<WaterEvokerEntity> WATER_EVOKER = null;
    @Nullable public static EntityType<IceWallEntity> ICE_WALL = null;

    private static final Map<Identifier, EntityType<?>> built = new LinkedHashMap<>();
    private static boolean created = false;
    private static boolean registered = false;

    private static <T extends Entity> EntityType<T> build(String id, EntityType.Builder<T> builder) {
        var type = builder.build(id);
        built.put(new Identifier(LNE_Wizards_Mod.MOD_ID, id), type);
        return type;
    }

    private static EntityType.Builder<? extends ElementalEvokerEntity> evokerBuilder(EntityType.EntityFactory<? extends ElementalEvokerEntity> factory) {
        return EntityType.Builder.create(factory, SpawnGroup.MONSTER)
                .setDimensions(0.6F, 1.95F)
                .maxTrackingRange(8);
    }

    @SuppressWarnings("unchecked")
    public static void create() {
        if (created) return;
        created = true;

        ARCANE_EVOKER = build("arcane_evoker",
                (EntityType.Builder<ArcaneEvokerEntity>) evokerBuilder(ArcaneEvokerEntity::new));
        FIRE_EVOKER = build("fire_evoker",
                (EntityType.Builder<FireEvokerEntity>) evokerBuilder(FireEvokerEntity::new));
        FROST_EVOKER = build("frost_evoker",
                (EntityType.Builder<FrostEvokerEntity>) evokerBuilder(FrostEvokerEntity::new));
        ICE_WALL = build("ice_wall",
                EntityType.Builder.create(IceWallEntity::new, SpawnGroup.MISC)
                        .setDimensions(1.0F, 3.0F)
                        .maxTrackingRange(8));

        if (Platform.util().isModLoaded("elemental_wizards_rpg")) {
            AIR_EVOKER = build("air_evoker",
                    (EntityType.Builder<AirEvokerEntity>) evokerBuilder(AirEvokerEntity::new));
            EARTH_EVOKER = build("earth_evoker",
                    (EntityType.Builder<EarthEvokerEntity>) evokerBuilder(EarthEvokerEntity::new));
            WATER_EVOKER = build("water_evoker",
                    (EntityType.Builder<WaterEvokerEntity>) evokerBuilder(WaterEvokerEntity::new));
        }
    }

    public static Map<Identifier, EntityType<?>> typesToRegister() {
        create();
        return built;
    }

    public static void register() {
        if (registered) return;
        registered = true;
        typesToRegister().forEach((id, type) -> Registry.register(Registries.ENTITY_TYPE, id, type));
    }
}
