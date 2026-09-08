package com.lne_wizards.item;

import more_rpg_loot.item.Group;
import net.spell_engine.Platform;
import net.minecraft.client.render.entity.EvokerEntityRenderer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.custom.MrpgLibSpells;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.WeaponConfig;
import net.spell_engine.api.spell.container.SpellContainers;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.item.Weapon;
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
        entry.spellContainer(SpellContainers.forMagicWeapon());
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
    private static final float staffSpellPower = 8F;

    public static Identifier rimefrost = MrpgLibSpells.rimefrost.id();
    public static Identifier pyromaniac = MrpgLibSpells.pyromaniac.id();
    public static Identifier arcane_precision = MrpgLibSpells.arcane_precision.id();
    public static Identifier water_flow = MrpgLibSpells.water_flow.id();
    public static Identifier obsidian_shards = MrpgLibSpells.obsidian_shards.id();
    public static Identifier zephyrs_speed = MrpgLibSpells.zephyrs_speed.id();

    public static void register(Map<String, WeaponConfig> configs) {
        if (!tweaksConfig.value.disable_special_lne_weapons) {
            staff("glacial_staff_frost",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.ICE)))
                    .translatedName("Everfrost Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(rimefrost));
            staff("wither_staff_fire",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)))
                    .translatedName("Netherflame Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.FIRE.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(pyromaniac));
            staff("ender_dragon_staff_arcane",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .translatedName("Arcane Dragon Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(arcane_precision));
        }
        if (!tweaksConfig.value.disable_special_lne_weapons && Platform.util().isModLoaded("elemental_wizards_rpg")) {
            staff("elder_guardian_staff_aqua",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)))
                    .translatedName("Tidecaller's Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(water_flow));
            staff("wither_staff_terra",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)))
                    .translatedName("Seismic Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.EARTH.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(obsidian_shards));
            staff("ender_dragon_staff_wind",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .translatedName("Zephyrwing Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.AIR.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpellId(zephyrs_speed));
        }
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
        Weapon.register(configs, entries, Group.RPG_LOOT_KEY);
    }
}
