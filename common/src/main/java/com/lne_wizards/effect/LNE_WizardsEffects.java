package com.lne_wizards.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.CustomStatusEffect;
import net.spell_engine.api.effect.Effects;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;
import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class LNE_WizardsEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static Effects.Entry FLAME_RUSH = add(new Effects.Entry(Identifier.of(MOD_ID, "flame_rush"),
            "Flamerush",
            "Increases movement speed and fire spell power.",
            new FlameRushStatusEffect(StatusEffectCategory.BENEFICIAL, SpellSchools.FIRE.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellSchools.FIRE.id.toString(),
                                    0.1F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.75F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static Effects.Entry ARCANE_PRECISION = add(new Effects.Entry(Identifier.of(MOD_ID, "arcane_precision"),
            "Arcane Precision",
            "Makes targets more vulnerable to Arcane Spell Damage & Crits",
            new ArcanePrecisionEffect(StatusEffectCategory.HARMFUL, SpellSchools.ARCANE.color)
                    .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(
                            0.025F, 0.05F, 0.1F)),
            new EffectConfig(
                    List.of(
                    )
            )
    ));
    public static Effects.Entry ZEPHYRS_SPEED = add(new Effects.Entry(Identifier.of(MOD_ID, "zephyrs_speed"),
            "Zephyrs Speed",
            "Increasing the Crit Chance & Movement Speed of the caster.",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, MoreSpellSchools.AIR.color),
            new EffectConfig(
                    List.of(
                            new AttributeModifier(
                                    SpellPowerMechanics.CRITICAL_CHANCE.id.toString(),
                                    0.03F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            ),
                            new AttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED.getIdAsString(),
                                    0.05F,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            )
                    )
            )
    ));

    public static void register(ConfigFile.Effects config) {
        for (var entry : entries) {
            Synchronized.configure(entry.effect, true);
        }

        net.spell_engine.api.effect.Effects.register(entries, config.effects);
    }
}
