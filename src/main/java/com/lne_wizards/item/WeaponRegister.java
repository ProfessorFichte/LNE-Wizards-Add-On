package com.lne_wizards.item;

import com.lne_wizards.item.weapons.*;
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
import net.spell_engine.api.item.ItemConfig;
import net.spell_engine.api.item.weapon.Weapon;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;
import static com.lne_wizards.LNE_Wizards_Mod.tweaksConfig;

public class WeaponRegister {
    public static final ArrayList<Weapon.Entry> entries = new ArrayList<>();

    private static Weapon.Entry entry(String requiredMod, String name, Weapon.CustomMaterial material, Item item, ItemConfig.Weapon defaults) {
        var entry = new Weapon.Entry(MOD_ID, name, material, item, defaults, null);
        if (entry.isRequiredModInstalled()) {
            entries.add(entry);
        }
        return entry;
    }

    private static Supplier<Ingredient> ingredient(String idString, boolean requirement, Item fallback) {
        var id = new Identifier(idString);
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



    private static final float staffAttackDamage = 4;
    private static final float staffAttackSpeed = -3F;
    private static final float staffSpellPower = 7F;

    ///WIZARDS
    private static Weapon.Entry staffDragon(String name, Weapon.CustomMaterial material) {
        return staffDragon(null, name, material);
    }
    private static Weapon.Entry staffDragon(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new DragonStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }
    private static Weapon.Entry staffEverfrost(String name, Weapon.CustomMaterial material) {
        return staffEverfrost(null, name, material);
    }
    private static Weapon.Entry staffEverfrost(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new EverfrostStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }
    private static Weapon.Entry staffNetherflame(String name, Weapon.CustomMaterial material) {
        return staffNetherflame(null, name, material);
    }
    private static Weapon.Entry staffNetherflame(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new NetherflameStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }
    ///ELEMENTAL WIZARDS
    private static Weapon.Entry staffTidecaller(String name, Weapon.CustomMaterial material) {
        return staffTidecaller(null, name, material);
    }
    private static Weapon.Entry staffTidecaller(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new TidecallerStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }
    private static Weapon.Entry staffSeismic(String name, Weapon.CustomMaterial material) {
        return staffSeismic(null, name, material);
    }
    private static Weapon.Entry staffSeismic(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new SeismicStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }
    private static Weapon.Entry staffZephyrwing(String name, Weapon.CustomMaterial material) {
        return staffZephyrwing(null, name, material);
    }
    private static Weapon.Entry staffZephyrwing(String requiredMod, String name, Weapon.CustomMaterial material) {
        var settings = new Item.Settings();
        settings = settings.rarity(Rarity.EPIC).fireproof();
        var item = new ZephyrwingStaff(material, settings);
        return entry(requiredMod, name, material, item, new ItemConfig.Weapon(staffAttackDamage, staffAttackSpeed));
    }


    public static void register(Map<String, ItemConfig.Weapon> configs) {
        if (!tweaksConfig.value.disable_special_lne_weapons) {
            var dragonRepair = ingredient("loot_n_explore:ender_dragon_scales",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var frostMonarchRepair = ingredient("loot_n_explore:frozen_soul",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var witherRepair = ingredient("minecraft:nether_star",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);

            staffEverfrost("staff_everfrost",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, frostMonarchRepair))
                    .attribute(ItemConfig.Attribute.bonus(SpellSchools.FROST.id, staffSpellPower));
            staffNetherflame("staff_netherflame",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, witherRepair))
                    .attribute(ItemConfig.Attribute.bonus(SpellSchools.FIRE.id, staffSpellPower));
            staffDragon("staff_arcane_dragon",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, dragonRepair))
                    .attribute(ItemConfig.Attribute.bonus(SpellSchools.ARCANE.id, staffSpellPower));
        }
        if (!tweaksConfig.value.disable_special_lne_weapons && FabricLoader.getInstance().isModLoaded("elemental_wizards_rpg")) {
            var dragonRepair = ingredient("loot_n_explore:ender_dragon_scales",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var witherRepair = ingredient("minecraft:nether_star",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);
            var elderGuardianRepair = ingredient("loot_n_explore:elder_guardian_eye",
                    FabricLoader.getInstance().isModLoaded("loot_n_explore"), Items.NETHERITE_INGOT);

            staffTidecaller("staff_tidecaller",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, elderGuardianRepair))
                    .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.WATER.id, staffSpellPower));
            staffSeismic("staff_seismic",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, witherRepair))
                    .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.EARTH.id, staffSpellPower));
            staffZephyrwing("staff_zephyrwing",
                    Weapon.CustomMaterial.matching(ToolMaterials.NETHERITE, dragonRepair))
                    .attribute(ItemConfig.Attribute.bonus(MoreSpellSchools.AIR.id, staffSpellPower));
        }

        Weapon.register(configs, entries, Group.RPG_LOOT_KEY);
    }
}
