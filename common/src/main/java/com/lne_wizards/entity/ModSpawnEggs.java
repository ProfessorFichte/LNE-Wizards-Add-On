package com.lne_wizards.entity;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModSpawnEggs {

    @Nullable public static SpawnEggItem AIR_EVOKER_SPAWN_EGG = null;

    public static final SpawnEggItem ARCANE_EVOKER_SPAWN_EGG = register(
            "arcane_evoker_spawn_egg", ModEntities.ARCANE_EVOKER, 0x5C0F8B, 0xC074F3);

    @Nullable public static SpawnEggItem EARTH_EVOKER_SPAWN_EGG = null;

    public static final SpawnEggItem FIRE_EVOKER_SPAWN_EGG = register(
            "fire_evoker_spawn_egg", ModEntities.FIRE_EVOKER, 0x8B0000, 0xFF6A00);

    public static final SpawnEggItem FROST_EVOKER_SPAWN_EGG = register(
            "frost_evoker_spawn_egg", ModEntities.FROST_EVOKER, 0x4FC3F7, 0xF0F8FF);

    @Nullable public static SpawnEggItem WATER_EVOKER_SPAWN_EGG = null;

    public static final List<SpawnEggItem> ALL_FOR_TAB = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private static SpawnEggItem register(String id, EntityType<?> type, int primaryColor, int secondaryColor) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(LNE_Wizards_Mod.MOD_ID, id),
                new SpawnEggItem((EntityType<? extends MobEntity>) type, primaryColor, secondaryColor, new Item.Settings())
        );
    }

    public static void registerItemGroup() {
        if (ModEntities.AIR_EVOKER != null) {
            AIR_EVOKER_SPAWN_EGG = register("air_evoker_spawn_egg", ModEntities.AIR_EVOKER, 0xA8D8EA, 0xFFFFFF);
        }
        if (ModEntities.EARTH_EVOKER != null) {
            EARTH_EVOKER_SPAWN_EGG = register("earth_evoker_spawn_egg", ModEntities.EARTH_EVOKER, 0x5C3317, 0x4CAF50);
        }
        if (ModEntities.WATER_EVOKER != null) {
            WATER_EVOKER_SPAWN_EGG = register("water_evoker_spawn_egg", ModEntities.WATER_EVOKER, 0x0A3D62, 0x74B9FF);
        }

        ALL_FOR_TAB.clear();
        if (AIR_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(AIR_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(ARCANE_EVOKER_SPAWN_EGG);
        if (EARTH_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(EARTH_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(FIRE_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(FROST_EVOKER_SPAWN_EGG);
        if (WATER_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(WATER_EVOKER_SPAWN_EGG);
    }
}
