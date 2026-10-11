package com.lne_wizards.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.mrpg_lib.datagen.SmithingRecipeGenerator;

public class ModRecipeProvider extends SmithingRecipeGenerator {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output, "lne_wizards");
    }

    private static Item item(Identifier id) {
        return Registries.ITEM.getOrEmpty(id)
                .orElseThrow(() -> new IllegalStateException("Smithing recipe item not registered during datagen: " + id));
    }

    @Override
    public void generate() {
        var dragonTemplate = Identifier.of("loot_n_explore", "dragon_upgrade_smithing_template");
        var guardianTemplate = Identifier.of("loot_n_explore", "guardian_upgrade_smithing_template");
        var witherTemplate = Identifier.of("loot_n_explore", "wither_upgrade_smithing_template");
        var frostMonarchTemplate = Identifier.of("loot_n_explore", "frostmonarch_upgrade_smithing_template");

        var dragonScales = Identifier.of("loot_n_explore", "ender_dragon_scales");
        var guardianEye = Identifier.of("loot_n_explore", "elder_guardian_eye");
        var witherSpine = Identifier.of("loot_n_explore", "wither_spine");
        var frozenSoul = Identifier.of("loot_n_explore", "frozen_soul");

        var staffNetheriteFrost = Identifier.of("wizards", "staff_netherite_frost");
        var staffNetheriteFire = Identifier.of("wizards", "staff_netherite_fire");
        var staffNetheriteArcane = Identifier.of("wizards", "staff_netherite_arcane");
        var staffNetheriteAqua = Identifier.of("elemental_wizards_rpg", "staff_netherite_aqua");
        var staffNetheriteTerra = Identifier.of("elemental_wizards_rpg", "staff_netherite_terra");
        var staffNetheriteWind = Identifier.of("elemental_wizards_rpg", "staff_netherite_wind");

        createSmithingTransformRecipe(
            "glacial_staff_frost_smithing",
            item(staffNetheriteFrost),
            frostMonarchTemplate,
            frozenSoul,
            item(Identifier.of("lne_wizards", "glacial_staff_frost")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "wither_staff_fire_smithing",
            item(staffNetheriteFire),
            witherTemplate,
            witherSpine,
            item(Identifier.of("lne_wizards", "wither_staff_fire")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "ender_dragon_staff_arcane_smithing",
            item(staffNetheriteArcane),
            dragonTemplate,
            dragonScales,
            item(Identifier.of("lne_wizards", "ender_dragon_staff_arcane")),
            new String[]{"loot_n_explore"}
        );
        createSmithingTransformRecipe(
            "elder_guardian_staff_aqua_smithing",
            item(staffNetheriteAqua),
            guardianTemplate,
            guardianEye,
            item(Identifier.of("lne_wizards", "elder_guardian_staff_aqua")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
        createSmithingTransformRecipe(
            "wither_staff_terra_smithing",
            item(staffNetheriteTerra),
            witherTemplate,
            witherSpine,
            item(Identifier.of("lne_wizards", "wither_staff_terra")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
        createSmithingTransformRecipe(
            "ender_dragon_staff_wind_smithing",
            item(staffNetheriteWind),
            dragonTemplate,
            dragonScales,
            item(Identifier.of("lne_wizards", "ender_dragon_staff_wind")),
            new String[]{"loot_n_explore", "elemental_wizards_rpg"}
        );
    }
}
