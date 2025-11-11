package com.lne_wizards;

import com.lne_wizards.config.Default;
import com.lne_wizards.effect.Effects;
import com.lne_wizards.item.WeaponRegister;
import com.lne_wizards.spell.CustomSpellImpacts;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.api.config.ConfigFile;
import com.lne_wizards.config.TweaksConfig;
import net.tiny_config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LNE_Wizards_Mod {
	public static final String MOD_ID = "lne_wizards";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ConfigManager<ConfigFile.Equipment> itemConfig = new ConfigManager<>
			("items", Default.itemConfig)
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();
	public static ConfigManager<TweaksConfig> tweaksConfig = new ConfigManager<>
			("tweaks", new TweaksConfig())
			.builder()
			.setDirectory(MOD_ID)
			.sanitize(true)
			.build();

	public static void init() {
		tweaksConfig.refresh();
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
		Effects.register();
	}
}