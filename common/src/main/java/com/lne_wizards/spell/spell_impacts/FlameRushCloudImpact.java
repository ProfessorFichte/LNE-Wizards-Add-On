package com.lne_wizards.spell.spell_impacts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.event.SpellHandlers;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.internals.SpellExecution;
import net.spell_engine.internals.delivery.CloudPlacer;
import net.spell_power.api.SpellPower;
import net.spell_power.api.SpellSchools;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class FlameRushCloudImpact implements SpellHandlers.CustomImpact {

    @Override
    public SpellHandlers.ImpactResult onSpellImpact(
            RegistryEntry<Spell> spell,
            SpellPower.Result powerResult,
            LivingEntity caster,
            Entity target,
            SpellExecution.ImpactContext context
    ) {
        RegistryEntry<Spell> flamerush_cloud = SpellRegistry.from(caster.getWorld()).getEntry(Identifier.of(MOD_ID, "helper/flamerush_cloud")).get();
        CloudPlacer.placeCloud(caster.getWorld(), caster, null, caster.getPos(), flamerush_cloud,
                new SpellExecution.ImpactContext()
                        .power(SpellPower.getSpellPower(SpellSchools.FIRE, caster))
                        .position(caster.getPos()));

        return new SpellHandlers.ImpactResult(true, false);
    }
}
