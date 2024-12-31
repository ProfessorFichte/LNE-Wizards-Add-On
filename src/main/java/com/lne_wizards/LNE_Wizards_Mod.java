package com.lne_wizards;

import com.lne_wizards.config.Default;
import com.lne_wizards.effect.Effects;
import com.lne_wizards.item.WeaponRegister;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.spell_engine.api.item.ItemConfig;
import com.lne_wizards.config.TweaksConfig;
import net.tinyconfig.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LNE_Wizards_Mod implements ModInitializer {
	public static final String MOD_ID = "lne_wizards";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ConfigManager<ItemConfig> itemConfig = new ConfigManager<>
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

	@Override
	public void onInitialize() {
		tweaksConfig.refresh();
		Effects.register();
		if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
			itemConfig.refresh();
			WeaponRegister.register(itemConfig.value.weapons);
			itemConfig.save();
		}

	}
}