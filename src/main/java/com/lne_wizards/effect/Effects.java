package com.lne_wizards.effect;

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
            .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(0, 0.05F, 0.1F));

    public static void register() {
        Synchronized.configure(ARCANE_PRECISION, true);
        int ID = 20100;
        Registry.register(Registries.STATUS_EFFECT, ID++, new Identifier(MOD_ID, "arcane_precision").toString(), ARCANE_PRECISION);
    }
}
