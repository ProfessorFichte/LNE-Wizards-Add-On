package com.lne_wizards.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.item.weapon.StaffItem;

import java.util.List;

public class NetherflameStaff extends StaffItem {
    public NetherflameStaff(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.wither_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_wizards.netherflame_staff").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_wizards.netherflame_staff_1").formatted(Formatting.DARK_RED));
        tooltip.add(Text.translatable("passive.lne_wizards.netherflame_staff_2").formatted(Formatting.DARK_RED));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
