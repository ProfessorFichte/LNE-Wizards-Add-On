package com.lne_wizards.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class FlameRushStatusEffect extends StatusEffect {
    protected FlameRushStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    private static final ParticleBatch particles = new ParticleBatch(
            SpellEngineParticles.flame.id().toString(),
            ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET, null,
            20, 0.05F, 0.1F, 0, 0.25F);

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()) {
            ParticleHelper.sendBatches(playerEntity, new ParticleBatch[]{particles});
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
