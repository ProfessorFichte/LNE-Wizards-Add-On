package com.lne_wizards;

import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.config.Default;
import com.lne_wizards.effect.LNE_WizardsEffects;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.entity.ModEntityAttributes;
import com.lne_wizards.entity.ModSpawnEggs;
import com.lne_wizards.entity.mob.elemental_evokers.*;
import com.lne_wizards.item.WeaponRegister;
import com.lne_wizards.spell.CustomSpellImpacts;
import net.spell_engine.Platform;
import net.spell_engine.rpg_series.config.ConfigFile;
import com.lne_wizards.config.TweaksConfig;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import net.spell_power.api.SpellSchools;
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import net.tiny_config.ConfigManager;
public class LNE_Wizards_Mod {
	public static final String MOD_ID = "lne_wizards";

	public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
			("items", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
			("tweaks_v2", new TweaksConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<ConfigFile.Effects> effectConfig = new ConfigManager<>
			("effects", new ConfigFile.Effects())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<EntityAttributeConfig> entityAttributeConfig = new ConfigManager<>
			("entity_attributes_v1", seededEntityAttributeDefaults())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static final String ELEMENTAL_WIZARDS_MOD_ID = "elemental_wizards_rpg";
	public static final String ELEMENTAL_WIZARDS_COMPAT_PACK_PATH = "resourcepacks/elemental_wizards_compat";

	private static EntityAttributeConfig seededEntityAttributeDefaults() {
		var config = new EntityAttributeConfig();
		float base = new TweaksConfig().elemental_evoker_default_spell_power;
		config.entries.put("lne_wizards:frost_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(SpellSchools.FROST.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base)), FrostEvokerEntity.WAND));
		config.entries.put("lne_wizards:fire_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(SpellSchools.FIRE.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base))
				.set("minecraft:generic.attack_knockback", 0.0), FireEvokerEntity.WAND));
		config.entries.put("lne_wizards:arcane_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(SpellSchools.ARCANE.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base)), ArcaneEvokerEntity.WAND));
		config.entries.put("lne_wizards:water_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(MoreSpellSchools.WATER.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base)), WaterEvokerEntity.WAND));
		config.entries.put("lne_wizards:earth_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(MoreSpellSchools.EARTH.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base)), EarthEvokerEntity.WAND));
		config.entries.put("lne_wizards:air_evoker", ElementalEvokerEntity.withWand(new EntityAttributeConfig.Entry()
				.set(MoreSpellSchools.AIR.attributeEntry.getIdAsString(), ElementalEvokerEntity.spellPowerBase(base)), AirEvokerEntity.WAND));
		return config;
	}

	public static void init() {
		tweaksConfig.refresh();
		effectConfig.refresh();
		entityAttributeConfig.refresh();
		entityAttributeConfig.save();
		CustomSpellImpacts.registerCustomImpacts();
	}

	public static void registerItems(){
		if(Platform.util().isModLoaded("loot_n_explore") || System.getProperty("fabric-api.datagen") != null) {
			itemConfig.refresh();
			WeaponRegister.register(itemConfig.value.weapons);
			itemConfig.save();
		}
	}

	public static void registerEffects(){
		LNE_WizardsEffects.register(effectConfig.value);
	}

	public static void registerBlocks() {
		ModBlocks.register();
	}

	public static void registerSpawnEggs() {
		ModSpawnEggs.registerItemGroup();
	}

	public static void registerEntities() {
		ModEntities.register();
	}

	public static void registerEntityAttributes(ModEntityAttributes.AttributeRegistrar registrar) {
		ModEntityAttributes.register(registrar);
	}
}