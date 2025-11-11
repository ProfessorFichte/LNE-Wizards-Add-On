package com.lne_wizards.item;

import more_rpg_loot.item.Group;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.WeaponConfig;
import net.spell_engine.api.item.Equipment;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellSchools;
import net.spell_engine.api.item.weapon.StaffItem;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;
import static com.lne_wizards.LNE_Wizards_Mod.tweaksConfig;

public class WeaponRegister {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String name, Weapon.CustomMaterial material, Weapon.Factory factory, WeaponConfig defaults, Equipment.WeaponType type) {
        var entry = new Weapon.Entry(MOD_ID, name, material, factory, defaults, type);
        entry.castSpell();
        entries.add(entry);
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = Identifier.of(idString);
        if (requirement) {
            return () -> {
                return Ingredient.ofItems(fallback);
            };
        } else {
            return () -> {
                var item = Registries.ITEM.get(id);
                var ingredient = item != null ? item : fallback;
                return Ingredient.ofItems(ingredient);
            };
        }
    }

    private static Weapon.Entry staff(String name, Weapon.CustomMaterial material) {
        return entry(name, material, StaffItem::new, new WeaponConfig(staffAttackDamage, staffAttackSpeed), Equipment.WeaponType.DAMAGE_STAFF);
    }

    private static final float staffAttackDamage = 4;
    private static final float staffAttackSpeed = -3F;
    private static final float staffSpellPower = 7F;

    public static Identifier rimefrost = Identifier.of("lne_wizards", "rimefrost");
    public static Identifier pyromaniac = Identifier.of("lne_wizards", "pyromaniac");
    public static Identifier arcane_precision = Identifier.of("lne_wizards", "arcane_precision");
    public static Identifier water_flow = Identifier.of("lne_wizards", "water_flow");
    public static Identifier obsidian_shards = Identifier.of("lne_wizards", "obsidian_shards");
    public static Identifier zephyrs_speed = Identifier.of("lne_wizards", "zephyrs_speed");

    public static void register(Map<String, WeaponConfig> configs) {
        if (!tweaksConfig.value.disable_special_lne_weapons) {
            staff("glacial_staff_frost",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.ICE)))
                    .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, staffSpellPower))
                    .spell(rimefrost);
            staff("wither_staff_fire",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)))
                    .attribute(AttributeModifier.bonus(SpellSchools.FIRE.id, staffSpellPower))
                    .spell(pyromaniac);
            staff("ender_dragon_staff_arcane",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, staffSpellPower))
                    .spell(arcane_precision);
        }
        if (!tweaksConfig.value.disable_special_lne_weapons && FabricLoader.getInstance().isModLoaded("elemental_wizards_rpg")) {
            staff("elder_guardian_staff_aqua",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)))
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, staffSpellPower))
                    .spell(water_flow);
            staff("wither_staff_terra",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)))
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.EARTH.id, staffSpellPower))
                    .spell(obsidian_shards);
            staff("ender_dragon_staff_wind",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.AIR.id, staffSpellPower))
                    .spell(zephyrs_speed);
        }
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
        Weapon.register(configs, entries, Group.RPG_LOOT_KEY);
    }
}
