package com.lne_wizards.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
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
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import com.mrpg_lib.compat.spell_engine.MrpgLibSpells;
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
    private static final RegistryKey<ItemGroup> LOOT_N_EXPLORE_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("loot_n_explore", "loot.general"));

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

    public static Identifier water_flow = MrpgLibSpells.water_flow.id();
    public static Identifier obsidian_shards = MrpgLibSpells.obsidian_shards.id();
    public static Identifier zephyrs_speed = MrpgLibSpells.zephyrs_speed.id();

    public static void register(Map<String, WeaponConfig> configs) {
        if (!tweaksConfig.value.disable_special_lne_weapons) {
            staff("glacial_staff_frost",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.ICE)))
                    .translatedName("Everfrost Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.FROST.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("wizards:frostbolt"))
                    .withAdditionalSpell(MrpgLibSpells.rimefrost.id().toString());
            staff("wither_staff_fire",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.BONE)))
                    .translatedName("Netherflame Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.FIRE.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("wizards:fire_blast"))
                    .withAdditionalSpell(MrpgLibSpells.pyromaniac.id().toString());
            staff("ender_dragon_staff_arcane",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .translatedName("Arcane Dragon Staff")
                    .attribute(AttributeModifier.bonus(SpellSchools.ARCANE.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("wizards:arcane_blast"))
                    .withAdditionalSpell(MrpgLibSpells.arcane_precision.id().toString());
        }
        if (!tweaksConfig.value.disable_special_lne_weapons && Platform.util().isModLoaded("elemental_wizards_rpg")) {
            staff("elder_guardian_staff_aqua",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD)))
                    .translatedName("Tidecaller's Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.WATER.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("elemental_wizards_rpg:aqua_water_whip"))
                    .withAdditionalSpell(MrpgLibSpells.water_flow.id().toString());
            staff("wither_staff_terra",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.OBSIDIAN)))
                    .translatedName("Seismic Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.EARTH.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("elemental_wizards_rpg:terra_stone_spear"))
                    .withAdditionalSpell(MrpgLibSpells.obsidian_shards.id().toString());
            staff("ender_dragon_staff_wind",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, () -> Ingredient.ofItems(Items.AMETHYST_SHARD)))
                    .translatedName("Zephyrwing Staff")
                    .attribute(AttributeModifier.bonus(MoreSpellSchools.AIR.id, staffSpellPower))
                    .spellContainer(SpellContainers.forMagicWeapon().withSpell("elemental_wizards_rpg:wind_air_cutter"))
                    .withAdditionalSpell(MrpgLibSpells.zephyrs_speed.id().toString());
        }
        entries.forEach(entry -> entry.rarity = Rarity.RARE);
        Weapon.register(configs, entries, LOOT_N_EXPLORE_GROUP);
    }
}
