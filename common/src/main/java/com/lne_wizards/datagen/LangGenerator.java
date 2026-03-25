package com.lne_wizards.datagen;

import com.lne_wizards.effect.LNE_WizardsEffects;
import com.lne_wizards.entity.ModSpawnEggs;
import com.lne_wizards.item.WeaponRegister;
import com.lne_wizards.spell.LneWizardSpells;
import net.elemental_wizards_rpg.spell.ElementalWizardSpells;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder builder) {
        // Weapons
        WeaponRegister.entries.forEach(entry -> {
            if (entry.item() != null && entry.translatedName() != null && !entry.translatedName().isEmpty()) {
                builder.add(entry.item(), entry.translatedName());
            }
        });

        // Effects
        LNE_WizardsEffects.entries.forEach(entry -> {
            if (entry.title != null && !entry.title.isEmpty()) {
                builder.add(entry.effect.getTranslationKey(), entry.title);
            }
            if (entry.description != null && !entry.description.isEmpty()) {
                builder.add(entry.effect.getTranslationKey() + ".description", entry.description);
            }
        });

        // Spells
        LneWizardSpells.entries.stream().filter(entry -> !entry.id().getPath().startsWith("helper/")).forEach(entry -> {
            var id = entry.id();
            builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".name", entry.title());
            builder.add("spell." + id.getNamespace() + "." + id.getPath() + ".description", entry.description());
        });

        // Elemental Evoker entity names
        builder.add("entity." + MOD_ID + ".air_evoker",    "Air Evoker");
        builder.add("entity." + MOD_ID + ".arcane_evoker", "Arcane Evoker");
        builder.add("entity." + MOD_ID + ".earth_evoker",  "Earth Evoker");
        builder.add("entity." + MOD_ID + ".fire_evoker",   "Fire Evoker");
        builder.add("entity." + MOD_ID + ".frost_evoker",  "Frost Evoker");
        builder.add("entity." + MOD_ID + ".water_evoker",  "Water Evoker");

        // Elemental Evoker spawn eggs
        builder.add(ModSpawnEggs.AIR_EVOKER_SPAWN_EGG,    "Air Evoker Spawn Egg");
        builder.add(ModSpawnEggs.ARCANE_EVOKER_SPAWN_EGG, "Arcane Evoker Spawn Egg");
        builder.add(ModSpawnEggs.EARTH_EVOKER_SPAWN_EGG,  "Earth Evoker Spawn Egg");
        builder.add(ModSpawnEggs.FIRE_EVOKER_SPAWN_EGG,   "Fire Evoker Spawn Egg");
        builder.add(ModSpawnEggs.FROST_EVOKER_SPAWN_EGG,  "Frost Evoker Spawn Egg");
        builder.add(ModSpawnEggs.WATER_EVOKER_SPAWN_EGG,  "Water Evoker Spawn Egg");
    }
}
