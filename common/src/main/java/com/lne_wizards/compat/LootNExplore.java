package com.lne_wizards.compat;

import more_rpg_loot.item.Group;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

public final class LootNExplore {
    private LootNExplore() {}

    public static RegistryKey<ItemGroup> itemGroupKey() {
        return Group.RPG_LOOT_KEY;
    }
}
