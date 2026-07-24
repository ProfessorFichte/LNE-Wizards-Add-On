package com.lne_wizards.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
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
        return FabricBlockSettings.create()
                .mapColor(MapColor.PURPLE)
                .strength(1.5F, 6.0F)
                .sounds(BlockSoundGroup.GLASS)
                .luminance(9)
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

    public static void register() {
        for (var e : all) {
            Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, e.name()), e.block());
            Registry.register(Registries.ITEM, Identifier.of(MOD_ID, e.name()), e.item());
        }
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            for (var e : all) content.add(e.item());
        });
    }
}
