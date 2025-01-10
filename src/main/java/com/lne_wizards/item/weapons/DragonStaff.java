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

public class DragonStaff extends StaffItem {
    public DragonStaff(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.ender_dragon_weapon").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("passive.lne_wizards.dragon_staff").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("passive.lne_wizards.dragon_staff_1").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("passive.lne_wizards.dragon_staff_2").formatted(Formatting.LIGHT_PURPLE));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
