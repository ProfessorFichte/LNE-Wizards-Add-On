package com.lne_wizards.config;

import com.lne_wizards.item.WeaponRegister;
import net.spell_engine.api.item.ItemConfig;

public class Default {
    public final static ItemConfig itemConfig;
    static {
        itemConfig = new ItemConfig();
        for (var weapon: WeaponRegister.entries) {
            itemConfig.weapons.put(weapon.name(), weapon.defaults());
        }

    }
}
