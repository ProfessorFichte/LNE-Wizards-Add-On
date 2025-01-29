package com.lne_wizards.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class Effects {

    public static StatusEffect ARCANE_PRECISION = new ArcanePrecisionEffect(StatusEffectCategory.HARMFUL, 0xff4bdd)
            .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(0.05F, 0.05F, 0.1F));
    public static StatusEffect FLAME_RUSH = new FlameRushEffect(StatusEffectCategory.BENEFICIAL, 0xff4bdd);

    public static void register() {
        FLAME_RUSH.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "5e58808d-6042-45c6-bb4d-f5fcd82f485e",
                0.5F, EntityAttributeModifier.Operation.MULTIPLY_BASE);

        Synchronized.configure(ARCANE_PRECISION, true);
        Synchronized.configure(FLAME_RUSH, true);
        int ID = 20100;
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "arcane_precision").toString(), ARCANE_PRECISION);
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "flame_rush").toString(), FLAME_RUSH);
    }
}
