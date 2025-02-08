package com.lne_wizards.item.weapons;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.spell_engine.api.item.weapon.StaffItem;

import java.util.List;

public class TidecallerStaff extends StaffItem {
    public TidecallerStaff(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("lore.loot_n_explore.elder_guardian_weapon").formatted(Formatting.GOLD));

        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("passive.lne_wizards.tidecaller_staff").formatted(Formatting.AQUA));
            tooltip.add(Text.translatable("passive.lne_wizards.tidecaller_staff_1").formatted(Formatting.AQUA));
            tooltip.add(Text.translatable("passive.lne_wizards.tidecaller_staff_2").formatted(Formatting.AQUA));
        }else{
            tooltip.add(Text.translatable("tooltip.loot_n_explore.shift_down"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}
