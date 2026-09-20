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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModSpawnEggs {

    @Nullable public static SpawnEggItem AIR_EVOKER_SPAWN_EGG = null;
    @Nullable public static SpawnEggItem ARCANE_EVOKER_SPAWN_EGG = null;
    @Nullable public static SpawnEggItem EARTH_EVOKER_SPAWN_EGG = null;
    @Nullable public static SpawnEggItem FIRE_EVOKER_SPAWN_EGG = null;
    @Nullable public static SpawnEggItem FROST_EVOKER_SPAWN_EGG = null;
    @Nullable public static SpawnEggItem WATER_EVOKER_SPAWN_EGG = null;

    public static final List<SpawnEggItem> ALL_FOR_TAB = new ArrayList<>();

    private static final Map<Identifier, Item> built = new LinkedHashMap<>();
    private static boolean created = false;
    private static boolean registered = false;

    @SuppressWarnings("unchecked")
    private static SpawnEggItem build(String id, EntityType<?> type, int primaryColor, int secondaryColor) {
        var egg = new SpawnEggItem((EntityType<? extends MobEntity>) type, primaryColor, secondaryColor, new Item.Settings());
        built.put(new Identifier(LNE_Wizards_Mod.MOD_ID, id), egg);
        return egg;
    }

    public static void create() {
        if (created) return;
        created = true;

        ModEntities.create();

        ARCANE_EVOKER_SPAWN_EGG = build("arcane_evoker_spawn_egg", ModEntities.ARCANE_EVOKER, 0x5C0F8B, 0xC074F3);
        FIRE_EVOKER_SPAWN_EGG = build("fire_evoker_spawn_egg", ModEntities.FIRE_EVOKER, 0x8B0000, 0xFF6A00);
        FROST_EVOKER_SPAWN_EGG = build("frost_evoker_spawn_egg", ModEntities.FROST_EVOKER, 0x4FC3F7, 0xF0F8FF);

        if (ModEntities.AIR_EVOKER != null) {
            AIR_EVOKER_SPAWN_EGG = build("air_evoker_spawn_egg", ModEntities.AIR_EVOKER, 0xA8D8EA, 0xFFFFFF);
        }
        if (ModEntities.EARTH_EVOKER != null) {
            EARTH_EVOKER_SPAWN_EGG = build("earth_evoker_spawn_egg", ModEntities.EARTH_EVOKER, 0x5C3317, 0x4CAF50);
        }
        if (ModEntities.WATER_EVOKER != null) {
            WATER_EVOKER_SPAWN_EGG = build("water_evoker_spawn_egg", ModEntities.WATER_EVOKER, 0x0A3D62, 0x74B9FF);
        }

        ALL_FOR_TAB.clear();
        if (AIR_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(AIR_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(ARCANE_EVOKER_SPAWN_EGG);
        if (EARTH_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(EARTH_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(FIRE_EVOKER_SPAWN_EGG);
        ALL_FOR_TAB.add(FROST_EVOKER_SPAWN_EGG);
        if (WATER_EVOKER_SPAWN_EGG != null) ALL_FOR_TAB.add(WATER_EVOKER_SPAWN_EGG);
    }

    public static Map<Identifier, Item> itemsToRegister() {
        create();
        return built;
    }

    public static void register() {
        if (registered) return;
        registered = true;
        itemsToRegister().forEach((id, egg) -> Registry.register(Registries.ITEM, id, egg));
    }
}
