package com.lne_wizards.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModRecipeProvider extends SmithingRecipeBase {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output, "lne_wizards");
    }

    @Override
    public void generate() {
        var dragonTemplate = new Identifier("loot_n_explore", "dragon_upgrade_smithing_template");
        var guardianTemplate = new Identifier("loot_n_explore", "guardian_upgrade_smithing_template");
        var witherTemplate = new Identifier("loot_n_explore", "wither_upgrade_smithing_template");
        var frostMonarchTemplate = new Identifier("loot_n_explore", "frostmonarch_upgrade_smithing_template");

        var dragonScales = new Identifier("loot_n_explore", "ender_dragon_scales");
        var guardianEye = new Identifier("loot_n_explore", "elder_guardian_eye");
        var witherSpine = new Identifier("loot_n_explore", "wither_spine");
        var frozenSoul = new Identifier("loot_n_explore", "frozen_soul");

        var staffNetheriteFrost = new Identifier("wizards", "staff_netherite_frost");
        var staffNetheriteFire = new Identifier("wizards", "staff_netherite_fire");
        var staffNetheriteArcane = new Identifier("wizards", "staff_netherite_arcane");
        var staffNetheriteAqua = new Identifier("elemental_wizards_rpg", "staff_netherite_aqua");
        var staffNetheriteTerra = new Identifier("elemental_wizards_rpg", "staff_netherite_terra");
        var staffNetheriteWind = new Identifier("elemental_wizards_rpg", "staff_netherite_wind");

        createSmithingTransformRecipe(
            "glacial_staff_frost_smithing",
            Registries.ITEM.get(staffNetheriteFrost),
            frostMonarchTemplate,
            frozenSoul,
            Registries.ITEM.get(new Identifier("lne_wizards", "glacial_staff_frost")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "wither_staff_fire_smithing",
            Registries.ITEM.get(staffNetheriteFire),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_wizards", "wither_staff_fire")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "ender_dragon_staff_arcane_smithing",
            Registries.ITEM.get(staffNetheriteArcane),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_wizards", "ender_dragon_staff_arcane")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "elder_guardian_staff_aqua_smithing",
            Registries.ITEM.get(staffNetheriteAqua),
            guardianTemplate,
            guardianEye,
            Registries.ITEM.get(new Identifier("lne_wizards", "elder_guardian_staff_aqua")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
        createSmithingTransformRecipe(
            "wither_staff_terra_smithing",
            Registries.ITEM.get(staffNetheriteTerra),
            witherTemplate,
            witherSpine,
            Registries.ITEM.get(new Identifier("lne_wizards", "wither_staff_terra")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
        createSmithingTransformRecipe(
            "ender_dragon_staff_wind_smithing",
            Registries.ITEM.get(staffNetheriteWind),
            dragonTemplate,
            dragonScales,
            Registries.ITEM.get(new Identifier("lne_wizards", "ender_dragon_staff_wind")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
    }
}
