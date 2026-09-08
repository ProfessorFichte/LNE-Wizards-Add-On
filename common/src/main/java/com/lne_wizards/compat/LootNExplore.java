package com.lne_wizards.compat;

import more_rpg_loot.item.Group;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

/**
 * The ONLY place in this mod that names a {@code more_rpg_loot} class.
 *
 * <p>Loot &amp; Explore is Fabric-only (no Forge build exists on the 1.20.1 or the 1.21.1 line), so on
 * Forge these classes are simply not on the classpath. Keeping every reference behind this holder means
 * the JVM never has to resolve them unless {@code Platform.util().isModLoaded("loot_n_explore")} is true —
 * the same shape SpellEngine's {@code ExternalSpellSchools} uses.
 *
 * <p>Never touch this class from a static initialiser or from a code path that runs unconditionally.
 */
public final class LootNExplore {
    private LootNExplore() {}

    /** Loot &amp; Explore's "RPG Loot" creative tab, which this mod's staves are inserted into. */
    public static RegistryKey<ItemGroup> itemGroupKey() {
        return Group.RPG_LOOT_KEY;
    }
}
