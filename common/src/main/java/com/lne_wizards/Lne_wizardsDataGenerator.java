package com.lne_wizards;

import com.lne_wizards.datagen.LangGenerator;
import com.lne_wizards.datagen.WeaponAttributesGenerator;
import com.lne_wizards.datagen.ModModelProvider;
import com.lne_wizards.datagen.ModRecipeProvider;
import com.lne_wizards.item.WeaponRegister;
import com.lne_wizards.spell.LneWizardSpells;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellGenerator;
import net.spell_engine.rpg_series.item.Equipment;
import net.spell_engine.rpg_series.datagen.RPGSeriesDataGen;
import net.spell_engine.rpg_series.tags.RPGSeriesItemTags;

import java.util.concurrent.CompletableFuture;

public class Lne_wizardsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ItemTagGenerator::new);
		pack.addProvider(SpellGen::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(LangGenerator::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(WeaponAttributesGenerator::new);
	}
	public static class SpellGen extends SpellGenerator {
		public SpellGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateSpells(Builder builder) {
			for (var entry: LneWizardSpells.entries) {
				builder.add(entry.id(), entry.spell());
			}
		}
	}

	public static class ItemTagGenerator extends RPGSeriesDataGen.ItemTagGenerator {
		public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			generateWeaponTags(WeaponRegister.entries);
			getOrCreateTagBuilder(RPGSeriesItemTags.WeaponType.get(Equipment.WeaponType.DAMAGE_STAFF))
					.addOptional(Identifier.of("lne_wizards", "elder_guardian_staff_aqua"))
					.addOptional(Identifier.of("lne_wizards", "wither_staff_terra"))
					.addOptional(Identifier.of("lne_wizards", "ender_dragon_staff_wind"));
		}
	}
}
