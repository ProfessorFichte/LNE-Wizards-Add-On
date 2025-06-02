package com.lne_wizards.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.internals.target.EntityRelations;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellSchools;

public class FlameRushStatusEffect extends StatusEffect {
    protected FlameRushStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }
    public static boolean isProtected(Entity target, LivingEntity attacker) {
        var relation = EntityRelations.getRelation(attacker, target);
        switch (relation) {
            case ALLY, FRIENDLY -> {
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
            ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET, null,
            20, 0.05F, 0.2F, 0, 0.5F);
    public static final ParticleBatch smoke = new ParticleBatch(
            "smoke",
            ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
            ParticleBatch.Rotation.LOOK, 5, 0.1F, 0.8F, 360);

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof PlayerEntity playerEntity && !playerEntity.getWorld().isClient()) {
            ParticleHelper.sendBatches(playerEntity, new ParticleBatch[]{particles});
            float range = 2.5F;
            Box radius = new Box(entity.getX() + range,
                    entity.getY() + (float) range / 3,
                    entity.getZ() + range,
                    entity.getX() - range,
                    entity.getY() - (float) range / 3,
                    entity.getZ() - range);
            for(Entity entities : entity.getEntityWorld().getOtherEntities(entity, radius, EntityPredicates.VALID_LIVING_ENTITY)){
                if (entities != null) {
                    if(entities instanceof LivingEntity target){
                        if(!isProtected(target,entity)) {
                            if (!entity.getWorld().isClient()) {
                                ParticleHelper.sendBatches(target, new ParticleBatch[]{smoke});
                                target.setFireTicks(40);
                            }
                        }
                    }
                }
            }

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
