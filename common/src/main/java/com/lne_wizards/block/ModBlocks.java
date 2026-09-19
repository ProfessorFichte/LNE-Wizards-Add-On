package com.lne_wizards.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class ModBlocks {

    public record Entry(String name, Block block, BlockItem item, String translation) {
        public Entry(String name, Block block, String translation) {
            this(name, block, new BlockItem(block, new Item.Settings()), translation);
        }
    }

    public static final ArrayList<Entry> all = new ArrayList<>();

    private static Entry entry(String name, Block block, String translation) {
        var e = new Entry(name, block, translation);
        all.add(e);
        return e;
    }

    private static AbstractBlock.Settings orbSettings() {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.PURPLE)
                .strength(1.5F, 6.0F)
                .sounds(BlockSoundGroup.GLASS)
                .luminance(state -> 9)
                .nonOpaque();
    }

    public static final Entry AIR_MAGIC_ORB = entry("air_magic_orb",
            new MagicOrbBlock(MoreSpellSchools.AIR, orbSettings()), "Air Magic Orb");

    public static final Entry ARCANE_MAGIC_ORB = entry("arcane_magic_orb",
            new MagicOrbBlock(SpellSchools.ARCANE, orbSettings()), "Arcane Magic Orb");

    public static final Entry EARTH_MAGIC_ORB = entry("earth_magic_orb",
            new MagicOrbBlock(MoreSpellSchools.EARTH, orbSettings()), "Earth Magic Orb");

    public static final Entry FIRE_MAGIC_ORB = entry("fire_magic_orb",
            new MagicOrbBlock(SpellSchools.FIRE, orbSettings()), "Fire Magic Orb");

    public static final Entry FROST_MAGIC_ORB = entry("frost_magic_orb",
            new MagicOrbBlock(SpellSchools.FROST, orbSettings()), "Frost Magic Orb");

    public static final Entry WATER_MAGIC_ORB = entry("water_magic_orb",
            new MagicOrbBlock(MoreSpellSchools.WATER, orbSettings()), "Water Magic Orb");

    /**
     * Blocks and their {@code BlockItem}s register in two steps: on Forge 47 each {@code RegisterEvent}
     * window unlocks exactly one registry, so a single loop touching both dies on the locked {@code ITEM}
     * registry while the {@code block} window is open.
     */
    public static void registerBlocks() {
        for (var e : all) {
            Registry.register(Registries.BLOCK, new Identifier(MOD_ID, e.name()), e.block());
        }
    }

    public static void registerBlockItems() {
        for (var e : all) {
            Registry.register(Registries.ITEM, new Identifier(MOD_ID, e.name()), e.item());
        }
    }
}
