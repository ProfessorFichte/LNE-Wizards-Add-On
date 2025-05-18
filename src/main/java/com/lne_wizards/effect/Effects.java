package com.lne_wizards.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.ArrayList;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class Effects {
    private static final ArrayList<Entry> entries = new ArrayList<Entry>();
    public static class Entry {
        public final Identifier id;
        public final StatusEffect effect;
        public RegistryEntry<StatusEffect> registryEntry;

        public Entry(String name, StatusEffect effect) {
            this.id = Identifier.of(MOD_ID, name);
            this.effect = effect;
            entries.add(this);
        }

        public void register() {
            registryEntry = Registry.registerReference(Registries.STATUS_EFFECT, id, effect);
        }

        public Identifier modifierId() {
            return Identifier.of(MOD_ID, "effect." + id.getPath());
        }
    }
    public static final Entry ARCANE_PRECISION =  new Entry("arcane_precision",
            new ArcanePrecisionEffect(StatusEffectCategory.HARMFUL, SpellSchools.ARCANE.color)
                    .setVulnerability(SpellSchools.ARCANE, new SpellPower.Vulnerability(
                            0.025F, 0.05F, 0.1F)));
    public static final Entry FLAME_RUSH =  new Entry("flame_rush",
            new FlameRushStatusEffect(StatusEffectCategory.BENEFICIAL,SpellSchools.FIRE.color));
    public static final Entry ZEPHYRS_SPEED =  new Entry("zephyrs_speed",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, MoreSpellSchools.AIR.color));

    public static void register() {
        FLAME_RUSH.effect
                .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, FLAME_RUSH.modifierId(),
                0.75F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(SpellSchools.FIRE.attributeEntry, FLAME_RUSH.modifierId(),
                        0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        ZEPHYRS_SPEED.effect.
                addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, ZEPHYRS_SPEED.modifierId(),
                        0.05F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                .addAttributeModifier(SpellPowerMechanics.CRITICAL_CHANCE.attributeEntry, ZEPHYRS_SPEED.modifierId(),
                        0.03F,EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);

        Synchronized.configure(ARCANE_PRECISION.effect, true);
        Synchronized.configure(FLAME_RUSH.effect, true);
        Synchronized.configure(ZEPHYRS_SPEED.effect, true);

        for (Entry entry: entries) {
            entry.register();
        }
    }
}
