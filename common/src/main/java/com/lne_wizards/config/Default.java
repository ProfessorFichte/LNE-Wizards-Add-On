package com.lne_wizards.config;

import com.lne_wizards.item.WeaponRegister;
import net.spell_engine.rpg_series.config.ConfigFile;

public class Default {
    public static final ConfigFile.Equipment itemConfig;

    static {
        itemConfig = new ConfigFile.Equipment();
        for (var weapon: WeaponRegister.entries) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }

    }
}
