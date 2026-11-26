package com.lne_wizards.datagen;

import com.mrpg_lib.compat.spell_engine.loot.SpecificSpellScrollPoolLootFunction;
import com.mrpg_lib.util.loot.ConditionalItemEntry;
import com.mrpg_lib.util.loot.ConditionalItemLootFunction;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public abstract class ModLootTableProvider extends SimpleFabricLootTableProvider {
    private BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter;

    protected ModLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup, LootContextType type) {
        super(output, registryLookup, type);
    }

    protected abstract void generate();

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> exporter) {
        this.exporter = exporter;
        generate();
    }

    protected void table(String path, LootPool.Builder... pools) {
        var table = LootTable.builder();
        for (var pool : pools) table.pool(pool);
        exporter.accept(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(MOD_ID, path)), table);
    }

    protected static LootPool.Builder pool(LootNumberProvider rolls, LootPoolEntry.Builder<?>... entries) {
        var pool = LootPool.builder().rolls(rolls);
        for (var entry : entries) pool.with(entry);
        return pool;
    }

    protected static LootNumberProvider constant(float value) {
        return ConstantLootNumberProvider.create(value);
    }

    protected static LootNumberProvider uniform(float min, float max) {
        return UniformLootNumberProvider.create(min, max);
    }

    protected static LeafEntry.Builder<?> item(String id, int weight, LootFunction.Builder... functions) {
        Item item = Registries.ITEM.get(Identifier.of(id));
        if (item == Items.AIR) throw new IllegalStateException("Loot table item is not registered: " + id);
        var entry = ItemEntry.builder(item).weight(weight);
        for (var function : functions) entry.apply(function);
        return entry;
    }

    protected static LeafEntry.Builder<?> conditional(String id, int weight, LootFunction.Builder... functions) {
        var entry = ConditionalItemEntry.builder(Identifier.of(id), Optional.empty()).weight(weight);
        for (var function : functions) entry.apply(function);
        return entry;
    }

    protected static LeafEntry.Builder<?> empty(int weight) {
        return EmptyEntry.builder().weight(weight);
    }

    protected static LeafEntry.Builder<?> nested(String id, int weight) {
        return LootTableEntry.builder(RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(id))).weight(weight);
    }

    protected static ConditionalLootFunction.Builder<?> count(LootNumberProvider count) {
        return SetCountLootFunction.builder(count);
    }

    protected static EnchantWithLevelsLootFunction.Builder enchant(float min, float max) {
        return new EnchantWithLevelsLootFunction.Builder(uniform(min, max));
    }

    protected static LootFunction.Builder scroll(int tierMin, int tierMax, int count, String... pools) {
        return SpecificSpellScrollPoolLootFunction.builder(List.of(pools), constant(tierMin), constant(tierMax), constant(count), List.of());
    }

    protected static LootFunction.Builder conditionalItem(String id) {
        return ConditionalItemLootFunction.builder(Identifier.of(id));
    }

    protected static LootCondition.Builder chance(float chance) {
        return RandomChanceLootCondition.builder(chance);
    }

    protected static LootCondition.Builder killedByPlayer() {
        return KilledByPlayerLootCondition.builder();
    }

    protected static LootCondition.Builder survivesExplosion() {
        return SurvivesExplosionLootCondition.builder();
    }

    public static class Chests extends ModLootTableProvider {
        public Chests(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.CHEST);
        }

        @Override
        protected void generate() {
            chestsArcaneTier1();
            chestsArcaneTier2();
            chestsArcaneTier3();
            chestsArcaneTier4();
            chestsArcaneTier5();
            chestsEarthTier1();
            chestsEarthTier2();
            chestsEarthTier3();
            chestsEarthTier4();
            chestsEarthTier5();
            chestsFireTier1();
            chestsFireTier2();
            chestsFireTier3();
            chestsFireTier4();
            chestsFireTier5();
            chestsFrostTier1();
            chestsFrostTier2();
            chestsFrostTier3();
            chestsFrostTier4();
            chestsFrostTier5();
            chestsGeneral0();
            chestsGeneral1();
            chestsTier1();
            chestsTier2();
            chestsTier3();
            chestsTier4();
            chestsTier5();
            chestsWaterTier1();
            chestsWaterTier2();
            chestsWaterTier3();
            chestsWaterTier4();
            chestsWaterTier5();
            chestsWindTier1();
            chestsWindTier2();
            chestsWindTier3();
            chestsWindTier4();
            chestsWindTier5();
        }

        private void chestsArcaneTier1() {
            table("chests/arcane/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:gold_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:amethyst_shard", 3, count(uniform(1, 3))),
                        item("runes:arcane_stone", 2, count(uniform(1, 8))),
                        item("minecraft:gold_ingot", 2, count(uniform(1, 2))),
                        item("wizards:wand_arcane", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/arcane"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_topaz", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsArcaneTier2() {
            table("chests/arcane/tier2",
                pool(uniform(1, 3),
                        item("minecraft:gold_ingot", 3, count(uniform(2, 5))),
                        item("runes:arcane_stone", 3, count(uniform(1, 10))),
                        item("minecraft:amethyst_shard", 3, count(uniform(2, 5))),
                        item("minecraft:ender_pearl", 2, count(uniform(1, 2))),
                        item("wizards:wand_arcane", 2),
                        item("wizards:wizard_robe_head", 1),
                        item("wizards:wizard_robe_chest", 1),
                        item("wizards:wizard_robe_legs", 1),
                        item("wizards:wizard_robe_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/arcane"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_topaz", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsArcaneTier3() {
            table("chests/arcane/tier3",
                pool(uniform(2, 3),
                        item("minecraft:gold_ingot", 4, count(uniform(1, 4))),
                        item("runes:arcane_stone", 4, count(uniform(6, 16))),
                        item("minecraft:amethyst_shard", 4, count(uniform(2, 5))),
                        item("minecraft:ender_pearl", 4, count(uniform(2, 3))),
                        item("wizards:wand_arcane", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_wizard", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_arcane", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:arcane_robe_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:arcane_robe_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:arcane_robe_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:arcane_robe_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/arcane"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("relics_rpgs:lesser_proc_arcane_fire", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:chorus_extract", 1)));
        }

        private void chestsArcaneTier4() {
            table("chests/arcane/tier4",
                pool(uniform(2, 4),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("runes:arcane_stone", 4, count(uniform(10, 20))),
                        item("minecraft:ender_pearl", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("wizards:staff_arcane", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:arcane_robe_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:arcane_robe_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:arcane_robe_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:arcane_robe_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#wizards:spell_scroll/arcane"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_topaz_ring", 1),
                        conditional("jewelry:netherite_topaz_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("relics_rpgs:medium_proc_arcane_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:chorus_extract", 1)));
        }

        private void chestsArcaneTier5() {
            table("chests/arcane/tier5",
                pool(uniform(2, 4),
                        item("runes:arcane_stone", 3, count(uniform(15, 26))),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:ender_pearl", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        item("wizards:staff_arcane", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:arcane_robe_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:arcane_robe_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:arcane_robe_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:arcane_robe_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#wizards:spell_scroll/arcane"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_topaz_ring", 1),
                        conditional("jewelry:netherite_topaz_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:chorus_extract", 1)));
        }

        private void chestsEarthTier1() {
            table("chests/earth/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:iron_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:raw_iron", 3, count(uniform(1, 3))),
                        item("mrpg_lib:terra_stone", 2, count(uniform(1, 8))),
                        item("minecraft:iron_ingot", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_kelp", 3),
                        conditional("elemental_wizards_rpg:wand_terra", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("additional_rpg_jewelry:malachite_ring", 1),
                        conditional("additional_rpg_jewelry:malachite_necklace", 1),
                        conditional("more_relics:jewel_figurine_malachite", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsEarthTier2() {
            table("chests/earth/tier2",
                pool(uniform(1, 3),
                        item("minecraft:iron_ingot", 3, count(uniform(2, 5))),
                        item("mrpg_lib:terra_stone", 3, count(uniform(1, 10))),
                        item("minecraft:raw_iron", 3, count(uniform(2, 5))),
                        item("minecraft:obsidian", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_terra", 2),
                        conditional("elemental_wizards_rpg:elemental_head", 1),
                        conditional("elemental_wizards_rpg:elemental_chest", 1),
                        conditional("elemental_wizards_rpg:elemental_legs", 1),
                        conditional("elemental_wizards_rpg:elemental_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("additional_rpg_jewelry:malachite_ring", 1),
                        conditional("additional_rpg_jewelry:malachite_necklace", 1),
                        conditional("more_relics:jewel_figurine_malachite", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsEarthTier3() {
            table("chests/earth/tier3",
                pool(uniform(2, 3),
                        item("minecraft:iron_ingot", 4, count(uniform(1, 4))),
                        item("mrpg_lib:terra_stone", 4, count(uniform(6, 16))),
                        item("minecraft:raw_iron", 4, count(uniform(2, 5))),
                        item("minecraft:obsidian", 4, count(uniform(2, 3))),
                        conditional("elemental_wizards_rpg:wand_terra", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_elemental", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_terra", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:dripstone_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:dripstone_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:dripstone_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:dripstone_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("additional_rpg_jewelry:malachite_ring", 1),
                        conditional("additional_rpg_jewelry:malachite_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("more_relics:lesser_proc_earth_nature", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:green_chilli", 1)));
        }

        private void chestsEarthTier4() {
            table("chests/earth/tier4",
                pool(uniform(2, 4),
                        item("minecraft:iron_ingot", 3, count(uniform(3, 7))),
                        item("mrpg_lib:terra_stone", 4, count(uniform(10, 20))),
                        item("minecraft:obsidian", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        conditional("elemental_wizards_rpg:staff_terra", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:dripstone_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:dripstone_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:dripstone_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:dripstone_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("additional_rpg_jewelry:netherite_malachite_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_malachite_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("more_relics:medium_use_earth_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:green_chilli", 1)));
        }

        private void chestsEarthTier5() {
            table("chests/earth/tier5",
                pool(uniform(2, 4),
                        item("mrpg_lib:terra_stone", 3, count(uniform(15, 26))),
                        item("minecraft:iron_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:obsidian", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        conditional("elemental_wizards_rpg:staff_terra", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:dripstone_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:dripstone_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:dripstone_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:dripstone_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(uniform(1, 1),
                        conditional("additional_rpg_jewelry:netherite_malachite_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_malachite_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:green_chilli", 1)));
        }

        private void chestsFireTier1() {
            table("chests/fire/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:gold_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:gunpowder", 3, count(uniform(1, 3))),
                        item("runes:fire_stone", 2, count(uniform(1, 8))),
                        item("minecraft:gold_ingot", 2, count(uniform(1, 2))),
                        item("wizards:wand_fire", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/fire"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_topaz", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsFireTier2() {
            table("chests/fire/tier2",
                pool(uniform(1, 3),
                        item("minecraft:gold_ingot", 3, count(uniform(2, 5))),
                        item("runes:fire_stone", 3, count(uniform(1, 10))),
                        item("minecraft:gunpowder", 3, count(uniform(2, 5))),
                        item("minecraft:blaze_powder", 2, count(uniform(1, 2))),
                        item("wizards:wand_fire", 2),
                        item("wizards:wizard_robe_head", 1),
                        item("wizards:wizard_robe_chest", 1),
                        item("wizards:wizard_robe_legs", 1),
                        item("wizards:wizard_robe_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/fire"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_topaz", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsFireTier3() {
            table("chests/fire/tier3",
                pool(uniform(2, 3),
                        item("minecraft:gold_ingot", 4, count(uniform(1, 4))),
                        item("runes:fire_stone", 4, count(uniform(6, 16))),
                        item("minecraft:gunpowder", 4, count(uniform(2, 5))),
                        item("minecraft:blaze_powder", 4, count(uniform(2, 3))),
                        item("wizards:wand_fire", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_wizard", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_fire", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:fire_robe_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:fire_robe_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:fire_robe_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:fire_robe_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/fire"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:topaz_ring", 1),
                        conditional("jewelry:topaz_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("relics_rpgs:lesser_proc_arcane_fire", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:hot_chilli", 1)));
        }

        private void chestsFireTier4() {
            table("chests/fire/tier4",
                pool(uniform(2, 4),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("runes:fire_stone", 4, count(uniform(10, 20))),
                        item("minecraft:blaze_powder", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("wizards:staff_fire", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:fire_robe_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:fire_robe_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:fire_robe_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:fire_robe_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#wizards:spell_scroll/fire"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_topaz_ring", 1),
                        conditional("jewelry:netherite_topaz_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("relics_rpgs:medium_proc_fire_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:hot_chilli", 1)));
        }

        private void chestsFireTier5() {
            table("chests/fire/tier5",
                pool(uniform(2, 4),
                        item("runes:fire_stone", 3, count(uniform(15, 26))),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:blaze_powder", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        item("wizards:staff_fire", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:fire_robe_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:fire_robe_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:fire_robe_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:fire_robe_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#wizards:spell_scroll/fire"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_topaz_ring", 1),
                        conditional("jewelry:netherite_topaz_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:hot_chilli", 1)));
        }

        private void chestsFrostTier1() {
            table("chests/frost/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:iron_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:snowball", 3, count(uniform(1, 3))),
                        item("runes:frost_stone", 2, count(uniform(1, 8))),
                        item("minecraft:iron_ingot", 2, count(uniform(1, 2))),
                        item("wizards:wand_frost", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/frost"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("jewelry:tanzanite_ring", 1),
                        conditional("jewelry:tanzanite_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_tanzanite", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsFrostTier2() {
            table("chests/frost/tier2",
                pool(uniform(1, 3),
                        item("minecraft:iron_ingot", 3, count(uniform(2, 5))),
                        item("runes:frost_stone", 3, count(uniform(1, 10))),
                        item("minecraft:snowball", 3, count(uniform(2, 5))),
                        item("minecraft:prismarine_shard", 2, count(uniform(1, 2))),
                        item("wizards:wand_frost", 2),
                        item("wizards:wizard_robe_head", 1),
                        item("wizards:wizard_robe_chest", 1),
                        item("wizards:wizard_robe_legs", 1),
                        item("wizards:wizard_robe_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#wizards:spell_scroll/frost"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("jewelry:tanzanite_ring", 1),
                        conditional("jewelry:tanzanite_necklace", 1),
                        conditional("relics_rpgs:jewel_figurine_tanzanite", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsFrostTier3() {
            table("chests/frost/tier3",
                pool(uniform(2, 3),
                        item("minecraft:iron_ingot", 4, count(uniform(1, 4))),
                        item("runes:frost_stone", 4, count(uniform(6, 16))),
                        item("minecraft:snowball", 4, count(uniform(2, 5))),
                        item("minecraft:prismarine_shard", 4, count(uniform(2, 3))),
                        item("wizards:wand_frost", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_wizard", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:wizard_robe_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:staff_frost", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:frost_robe_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:frost_robe_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:frost_robe_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("wizards:frost_robe_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/frost"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("jewelry:tanzanite_ring", 1),
                        conditional("jewelry:tanzanite_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("relics_rpgs:lesser_proc_frost_healing", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:fruit_icewater", 1)));
        }

        private void chestsFrostTier4() {
            table("chests/frost/tier4",
                pool(uniform(2, 4),
                        item("minecraft:iron_ingot", 3, count(uniform(3, 7))),
                        item("runes:frost_stone", 4, count(uniform(10, 20))),
                        item("minecraft:prismarine_shard", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        item("wizards:staff_frost", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:frost_robe_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:frost_robe_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:frost_robe_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("wizards:frost_robe_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#wizards:spell_scroll/frost"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("jewelry:netherite_tanzanite_ring", 1),
                        conditional("jewelry:netherite_tanzanite_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("relics_rpgs:medium_proc_frost_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:fruit_icewater", 1)));
        }

        private void chestsFrostTier5() {
            table("chests/frost/tier5",
                pool(uniform(2, 4),
                        item("runes:frost_stone", 3, count(uniform(15, 26))),
                        item("minecraft:iron_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:prismarine_shard", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        item("wizards:staff_frost", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:frost_robe_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:frost_robe_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:frost_robe_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("wizards:frost_robe_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#wizards:spell_scroll/frost"))),
                pool(uniform(1, 1),
                        conditional("jewelry:netherite_tanzanite_ring", 1),
                        conditional("jewelry:netherite_tanzanite_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:fruit_icewater", 1)));
        }

        private void chestsGeneral0() {
            table("chests/general_0",
                pool(uniform(2, 4),
                        item("minecraft:gold_nugget", 1, count(uniform(3, 8))),
                        item("minecraft:paper", 1, count(uniform(1, 4))),
                        item("minecraft:white_wool", 1, count(uniform(1, 4))),
                        item("minecraft:honeycomb", 1, count(uniform(1, 2))),
                        item("minecraft:iron_nugget", 1, count(uniform(3, 8)))));
        }

        private void chestsGeneral1() {
            table("chests/general_1",
                pool(uniform(2, 4),
                        item("minecraft:bread", 1, count(uniform(1, 4))),
                        item("minecraft:wheat", 1, count(uniform(3, 7))),
                        item("minecraft:cooked_porkchop", 1, count(uniform(1, 2))),
                        item("minecraft:gold_nugget", 1, count(uniform(2, 5))),
                        item("minecraft:apple", 1, count(uniform(1, 3))),
                        item("minecraft:lapis_lazuli", 1, count(uniform(1, 2))),
                        item("runes:healing_stone", 1, count(uniform(1, 10)))));
        }

        private void chestsTier1() {
            table("chests/tier1",
                pool(constant(1),
                        nested("lne_wizards:chests/arcane/tier1", 1),
                        nested("lne_wizards:chests/earth/tier1", 1),
                        nested("lne_wizards:chests/fire/tier1", 1),
                        nested("lne_wizards:chests/frost/tier1", 1),
                        nested("lne_wizards:chests/water/tier1", 1),
                        nested("lne_wizards:chests/wind/tier1", 1)));
        }

        private void chestsTier2() {
            table("chests/tier2",
                pool(constant(1),
                        nested("lne_wizards:chests/arcane/tier2", 1),
                        nested("lne_wizards:chests/earth/tier2", 1),
                        nested("lne_wizards:chests/fire/tier2", 1),
                        nested("lne_wizards:chests/frost/tier2", 1),
                        nested("lne_wizards:chests/water/tier2", 1),
                        nested("lne_wizards:chests/wind/tier2", 1)));
        }

        private void chestsTier3() {
            table("chests/tier3",
                pool(constant(1),
                        nested("lne_wizards:chests/arcane/tier3", 1),
                        nested("lne_wizards:chests/earth/tier3", 1),
                        nested("lne_wizards:chests/fire/tier3", 1),
                        nested("lne_wizards:chests/frost/tier3", 1),
                        nested("lne_wizards:chests/water/tier3", 1),
                        nested("lne_wizards:chests/wind/tier3", 1)));
        }

        private void chestsTier4() {
            table("chests/tier4",
                pool(constant(1),
                        nested("lne_wizards:chests/arcane/tier4", 1),
                        nested("lne_wizards:chests/earth/tier4", 1),
                        nested("lne_wizards:chests/fire/tier4", 1),
                        nested("lne_wizards:chests/frost/tier4", 1),
                        nested("lne_wizards:chests/water/tier4", 1),
                        nested("lne_wizards:chests/wind/tier4", 1)));
        }

        private void chestsTier5() {
            table("chests/tier5",
                pool(constant(1),
                        nested("lne_wizards:chests/arcane/tier5", 1),
                        nested("lne_wizards:chests/earth/tier5", 1),
                        nested("lne_wizards:chests/fire/tier5", 1),
                        nested("lne_wizards:chests/frost/tier5", 1),
                        nested("lne_wizards:chests/water/tier5", 1),
                        nested("lne_wizards:chests/wind/tier5", 1)));
        }

        private void chestsWaterTier1() {
            table("chests/water/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:gold_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:glow_ink_sac", 3, count(uniform(1, 3))),
                        item("mrpg_lib:aqua_stone", 2, count(uniform(1, 8))),
                        item("minecraft:gold_ingot", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_kelp", 3),
                        conditional("elemental_wizards_rpg:wand_aqua", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("more_relics:jewel_figurine_aquamarine", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsWaterTier2() {
            table("chests/water/tier2",
                pool(uniform(1, 3),
                        item("minecraft:gold_ingot", 3, count(uniform(2, 5))),
                        item("mrpg_lib:aqua_stone", 3, count(uniform(1, 10))),
                        item("minecraft:glow_ink_sac", 3, count(uniform(2, 5))),
                        item("minecraft:prismarine_crystals", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_aqua", 2),
                        conditional("elemental_wizards_rpg:elemental_head", 1),
                        conditional("elemental_wizards_rpg:elemental_chest", 1),
                        conditional("elemental_wizards_rpg:elemental_legs", 1),
                        conditional("elemental_wizards_rpg:elemental_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("more_relics:jewel_figurine_aquamarine", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsWaterTier3() {
            table("chests/water/tier3",
                pool(uniform(2, 3),
                        item("minecraft:gold_ingot", 4, count(uniform(1, 4))),
                        item("mrpg_lib:aqua_stone", 4, count(uniform(6, 16))),
                        item("minecraft:glow_ink_sac", 4, count(uniform(2, 5))),
                        item("minecraft:prismarine_crystals", 4, count(uniform(2, 3))),
                        conditional("elemental_wizards_rpg:wand_aqua", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_elemental", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_aqua", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:kelp_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:kelp_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:kelp_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:kelp_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("more_relics:lesser_proc_air_water", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:watermelon_drink", 1)));
        }

        private void chestsWaterTier4() {
            table("chests/water/tier4",
                pool(uniform(2, 4),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("mrpg_lib:aqua_stone", 4, count(uniform(10, 20))),
                        item("minecraft:prismarine_crystals", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        conditional("elemental_wizards_rpg:staff_aqua", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:kelp_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:kelp_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:kelp_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:kelp_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("more_relics:medium_use_water_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:watermelon_drink", 1)));
        }

        private void chestsWaterTier5() {
            table("chests/water/tier5",
                pool(uniform(2, 4),
                        item("mrpg_lib:aqua_stone", 3, count(uniform(15, 26))),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:prismarine_crystals", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        conditional("elemental_wizards_rpg:staff_aqua", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:kelp_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:kelp_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:kelp_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:kelp_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(uniform(1, 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:watermelon_drink", 1)));
        }

        private void chestsWindTier1() {
            table("chests/wind/tier1",
                pool(uniform(1, 2),
                        item("minecraft:white_wool", 5, count(uniform(2, 6))),
                        item("minecraft:gold_nugget", 4, count(uniform(2, 6))),
                        item("minecraft:lapis_lazuli", 3, count(uniform(1, 2))),
                        item("minecraft:phantom_membrane", 3, count(uniform(1, 3))),
                        item("mrpg_lib:storm_stone", 2, count(uniform(1, 8))),
                        item("minecraft:gold_ingot", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_feather", 3),
                        conditional("elemental_wizards_rpg:wand_wind", 1)),
                pool(uniform(1, 1),
                        empty(7),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(uniform(1, 1),
                        empty(15),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("more_relics:jewel_figurine_aquamarine", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsWindTier2() {
            table("chests/wind/tier2",
                pool(uniform(1, 3),
                        item("minecraft:gold_ingot", 3, count(uniform(2, 5))),
                        item("mrpg_lib:storm_stone", 3, count(uniform(1, 10))),
                        item("minecraft:phantom_membrane", 3, count(uniform(2, 5))),
                        item("minecraft:wind_charge", 2, count(uniform(1, 2))),
                        conditional("elemental_wizards_rpg:wand_wind", 2),
                        conditional("elemental_wizards_rpg:elemental_head", 1),
                        conditional("elemental_wizards_rpg:elemental_chest", 1),
                        conditional("elemental_wizards_rpg:elemental_legs", 1),
                        conditional("elemental_wizards_rpg:elemental_feet", 1),
                        item("runes:small_rune_pouch", 1)),
                pool(uniform(1, 1),
                        empty(5),
                        item("spell_engine:spell_scroll", 1, scroll(2, 2, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(uniform(1, 1),
                        empty(10),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("more_relics:jewel_figurine_aquamarine", 1),
                        conditional("loot_n_explore:orange_juice", 1)));
        }

        private void chestsWindTier3() {
            table("chests/wind/tier3",
                pool(uniform(2, 3),
                        item("minecraft:gold_ingot", 4, count(uniform(1, 4))),
                        item("mrpg_lib:storm_stone", 4, count(uniform(6, 16))),
                        item("minecraft:phantom_membrane", 4, count(uniform(2, 5))),
                        item("minecraft:wind_charge", 4, count(uniform(2, 3))),
                        conditional("elemental_wizards_rpg:wand_wind", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_elemental", 3, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_head", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_chest", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_legs", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:elemental_feet", 2, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:staff_wind", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:wind_head", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:wind_chest", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:wind_legs", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        conditional("elemental_wizards_rpg:wind_feet", 1, enchant(1, 13).conditionally(chance(0.35F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.25F)),
                pool(uniform(1, 1),
                        empty(3),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(uniform(1, 1),
                        empty(8),
                        conditional("additional_rpg_jewelry:aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:aquamarine_necklace", 1),
                        conditional("relics_rpgs:lesser_use_spell_power", 1),
                        conditional("relics_rpgs:lesser_use_spell_haste", 1),
                        conditional("relics_rpgs:lesser_proc_spell_crit", 1),
                        conditional("relics_rpgs:lesser_proc_crit_damage", 1),
                        conditional("more_relics:lesser_proc_air_water", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:blue_berry_punch", 1)));
        }

        private void chestsWindTier4() {
            table("chests/wind/tier4",
                pool(uniform(2, 4),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("mrpg_lib:storm_stone", 4, count(uniform(10, 20))),
                        item("minecraft:wind_charge", 3, count(uniform(2, 3))),
                        item("minecraft:diamond", 2, count(uniform(1, 4))),
                        conditional("elemental_wizards_rpg:staff_wind", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:wind_head", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:wind_chest", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:wind_legs", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        conditional("elemental_wizards_rpg:wind_feet", 1, enchant(6, 18).conditionally(chance(0.5F))),
                        item("runes:medium_rune_pouch", 1)).bonusRolls(constant(0.5F)),
                pool(uniform(1, 1),
                        empty(1),
                        item("spell_engine:spell_scroll", 1, scroll(3, 5, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(uniform(1, 1),
                        empty(3),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_necklace", 1),
                        conditional("relics_rpgs:medium_proc_spell_power", 1),
                        conditional("relics_rpgs:medium_proc_spell_haste", 1),
                        conditional("more_relics:medium_use_air_power", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:blue_berry_punch", 1)));
        }

        private void chestsWindTier5() {
            table("chests/wind/tier5",
                pool(uniform(2, 4),
                        item("mrpg_lib:storm_stone", 3, count(uniform(15, 26))),
                        item("minecraft:gold_ingot", 3, count(uniform(3, 7))),
                        item("minecraft:diamond", 2, count(uniform(3, 6))),
                        item("minecraft:wind_charge", 2, count(uniform(3, 4))),
                        item("minecraft:golden_apple", 1),
                        conditional("elemental_wizards_rpg:staff_wind", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:wind_head", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:wind_chest", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:wind_legs", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        conditional("elemental_wizards_rpg:wind_feet", 1, enchant(13, 24).conditionally(chance(0.75F))),
                        item("runes:large_rune_pouch", 1)).bonusRolls(constant(0.75F)),
                pool(uniform(1, 1),
                        item("spell_engine:spell_scroll", 1, scroll(4, 5, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(uniform(1, 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_ring", 1),
                        conditional("additional_rpg_jewelry:netherite_aquamarine_necklace", 1),
                        conditional("relics_rpgs:greater_proc_spell_trance", 1),
                        conditional("relics_rpgs:greater_perk_spell_stun", 1),
                        conditional("loot_n_explore:sweet_chilli", 1),
                        conditional("loot_n_explore:enchanted_ale", 1),
                        conditional("loot_n_explore:blue_berry_punch", 1)));
        }
    }

    public static class Entities extends ModLootTableProvider {
        public Entities(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.ENTITY);
        }

        @Override
        protected void generate() {
            entitiesAirEvoker();
            entitiesArcaneEvoker();
            entitiesEarthEvoker();
            entitiesFireEvoker();
            entitiesFrostEvoker();
            entitiesWaterEvoker();
        }

        private void entitiesAirEvoker() {
            table("entities/air_evoker",
                pool(uniform(1, 2),
                        item("minecraft:phantom_membrane", 4, count(uniform(2, 5))),
                        item("mrpg_lib:storm_stone", 3, count(uniform(1, 3))),
                        item("minecraft:wind_charge", 2, count(uniform(0, 1)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/wind"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }

        private void entitiesArcaneEvoker() {
            table("entities/arcane_evoker",
                pool(uniform(1, 2),
                        item("minecraft:amethyst_shard", 4, count(uniform(2, 4))),
                        item("runes:arcane_stone", 3, count(uniform(1, 3))),
                        item("minecraft:ender_pearl", 2, count(uniform(0, 1)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/arcane"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }

        private void entitiesEarthEvoker() {
            table("entities/earth_evoker",
                pool(uniform(1, 2),
                        item("minecraft:raw_iron", 4, count(uniform(2, 4))),
                        item("mrpg_lib:terra_stone", 3, count(uniform(1, 3))),
                        item("minecraft:obsidian", 2, count(uniform(2, 4)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/terra"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }

        private void entitiesFireEvoker() {
            table("entities/fire_evoker",
                pool(uniform(1, 2),
                        item("minecraft:gunpowder", 4, count(uniform(1, 3))),
                        item("runes:fire_stone", 3, count(uniform(1, 3))),
                        item("minecraft:blaze_powder", 2, count(uniform(0, 1)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/fire"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }

        private void entitiesFrostEvoker() {
            table("entities/frost_evoker",
                pool(uniform(1, 2),
                        item("minecraft:snowball", 4, count(uniform(2, 5))),
                        item("runes:frost_stone", 3, count(uniform(1, 3))),
                        item("minecraft:prismarine_shard", 2, count(uniform(0, 1)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#wizards:spell_scroll/frost"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }

        private void entitiesWaterEvoker() {
            table("entities/water_evoker",
                pool(uniform(1, 2),
                        item("minecraft:prismarine_crystals", 4, count(uniform(1, 3))),
                        item("mrpg_lib:aqua_stone", 3, count(uniform(1, 3))),
                        item("minecraft:glow_ink_sac", 2, count(uniform(1, 2)))),
                pool(constant(1),
                        empty(4),
                        item("spell_engine:spell_scroll", 1, scroll(2, 3, 1, "#elemental_wizards_rpg:spell_scroll/aqua"))),
                pool(constant(1),
                        item("minecraft:emerald", 1, count(uniform(1, 2)))).conditionally(killedByPlayer()));
        }
    }

    public static class Blocks extends ModLootTableProvider {
        public Blocks(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup, LootContextTypes.BLOCK);
        }

        @Override
        protected void generate() {
            blocksAirMagicOrb();
            blocksArcaneMagicOrb();
            blocksEarthMagicOrb();
            blocksFireMagicOrb();
            blocksFrostMagicOrb();
            blocksWaterMagicOrb();
        }

        private void blocksAirMagicOrb() {
            table("blocks/air_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }

        private void blocksArcaneMagicOrb() {
            table("blocks/arcane_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }

        private void blocksEarthMagicOrb() {
            table("blocks/earth_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }

        private void blocksFireMagicOrb() {
            table("blocks/fire_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }

        private void blocksFrostMagicOrb() {
            table("blocks/frost_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }

        private void blocksWaterMagicOrb() {
            table("blocks/water_magic_orb",
                pool(constant(1),
                        item("minecraft:glass", 1)).conditionally(survivesExplosion()));
        }
    }
}
