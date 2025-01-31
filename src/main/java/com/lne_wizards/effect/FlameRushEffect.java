package com.lne_wizards.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;

import java.util.List;
import java.util.function.Predicate;

public class FlameRushEffect extends StatusEffect {
    protected FlameRushEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public static boolean isProtected(Entity target, LivingEntity caster) {
        var relation = TargetHelper.getRelation(caster, target);
        switch (relation) {
            case FRIENDLY, SEMI_FRIENDLY -> {
                return true;
            }
            case NEUTRAL, MIXED, HOSTILE -> {
                return false;
            }
        }
        return false;
    }
    private static final ParticleBatch particles = new ParticleBatch(
            "spell_engine:flame_ground",
            ParticleBatch.Shape.PILLAR,
            ParticleBatch.Origin.FEET,
            null,
            7,
            0.05F,
            0.2F,
            0);
    private static final ParticleBatch particles1 = new ParticleBatch(
            "lava",
            ParticleBatch.Shape.CIRCLE,
            ParticleBatch.Origin.CENTER,
            null,
            1,
            0.1F,
            0.5F,
            0);
    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int pAmplifier) {
        if(livingEntity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()) {
            ParticleHelper.sendBatches(playerEntity, new ParticleBatch[]{particles});
            Predicate<Entity> selectionPredicate = (target2) -> {
                return (TargetHelper.actionAllowed(TargetHelper.TargetingMode.DIRECT, TargetHelper.Intent.HARMFUL, playerEntity, target2)
                );
            };
            List<Entity> list = playerEntity.getWorld().getOtherEntities(playerEntity, playerEntity.getBoundingBox().expand(2.0F),
                    selectionPredicate);
            for (Entity entity : list) {
                if(entity instanceof LivingEntity && !isProtected(entity,playerEntity)){
                    ParticleHelper.sendBatches(entity, new ParticleBatch[]{particles1});
                    entity.setFireTicks(40);
                }
            }
        }
        super.applyUpdateEffect(livingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
