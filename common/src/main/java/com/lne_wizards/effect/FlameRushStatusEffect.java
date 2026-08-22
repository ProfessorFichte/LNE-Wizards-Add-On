package com.lne_wizards.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.List;

public class FlameRushStatusEffect extends StatusEffect {
    protected FlameRushStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    /// V1: `new ParticleBatch(flame, Shape.CIRCLE, Origin.FEET, null, 20, 0.05F, 0.1F, 0, 0.25F)`
    /// — the 9-arg form, i.e. angle 0 and extent 0.25.
    private static final ParticleGroup particles = ParticleGroupBuilder.of(SpellEngineParticles.flame)
            .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                    .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                    .count(20).speed(0.05F, 0.1F)
                    .extent(0.25F));

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()) {
            ParticleHelper.sendBatches(playerEntity, List.of(particles));
        }
        return true;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 5;
        if (i > 0) {
            return duration % i == 0;
        }

        return false;
    }
}
