package com.lne_wizards.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class Effects {

    public static StatusEffect ARCANE_PRECISION = new ArcanePrecisionEffect(StatusEffectCategory.HARMFUL, SpellSchools.ARCANE.color)
            .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(0.05F, 0.05F, 0.1F));
    public static StatusEffect FLAME_RUSH = new FlameRushEffect(StatusEffectCategory.BENEFICIAL, SpellSchools.FIRE.color);
    public static StatusEffect ZEPHYRS_SPEED = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, MoreSpellSchools.AIR.color);
    public static StatusEffect OBSIDIAN_SHARDS = new ObsidianShardsEffect(StatusEffectCategory.BENEFICIAL, MoreSpellSchools.EARTH.color);

    public static void register() {
        FLAME_RUSH.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "5e58808d-6042-45c6-bb4d-f5fcd82f485e",
                0.75F, EntityAttributeModifier.Operation.MULTIPLY_BASE);
        ZEPHYRS_SPEED.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "5e58808d-6042-45c6-bb4d-f5fcd82f485e",
                        0.05F, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_CHANCE.attribute, "6575e62c-79f1-495e-9c6c-afbab3fe6396",
                        0.07F,EntityAttributeModifier.Operation.MULTIPLY_BASE);
        OBSIDIAN_SHARDS.addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "d20cbd0d-4101-4dc8-9bbc-140494951dc8",
                1.0F, EntityAttributeModifier.Operation.ADDITION);

        Synchronized.configure(ARCANE_PRECISION, true);
        Synchronized.configure(FLAME_RUSH, true);
        Synchronized.configure(ZEPHYRS_SPEED, true);
        Synchronized.configure(OBSIDIAN_SHARDS, true);
        int ID = 20100;
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "arcane_precision").toString(), ARCANE_PRECISION);
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "flame_rush").toString(), FLAME_RUSH);
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "zephyrs_speed").toString(), ZEPHYRS_SPEED);
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "obsidian_shards").toString(), OBSIDIAN_SHARDS);
    }
}
