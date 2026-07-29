package com.lne_wizards;

import com.lne_wizards.block.ModBlocks;
import com.lne_wizards.config.Default;
import com.lne_wizards.effect.LNE_WizardsEffects;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.entity.ModEntityAttributes;
import com.lne_wizards.entity.ModSpawnEggs;
import com.lne_wizards.item.WeaponRegister;
import com.lne_wizards.spell.CustomSpellImpacts;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.api.config.ConfigFile;
import com.lne_wizards.config.TweaksConfig;
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

	public static final String ELEMENTAL_WIZARDS_MOD_ID = "elemental_wizards_rpg";
	public static final String ELEMENTAL_WIZARDS_COMPAT_PACK_PATH = "resourcepacks/elemental_wizards_compat";

	public static void init() {
		tweaksConfig.refresh();
		effectConfig.refresh();
		CustomSpellImpacts.registerCustomImpacts();
	}

	public static void registerItems(){
		if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
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

	public static void registerEntityAttributes() {
		ModEntityAttributes.register();
	}
}