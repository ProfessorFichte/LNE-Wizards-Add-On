package com.lne_wizards.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;
import net.spell_engine.utils.TargetHelper;

public class ObsidianShardsEffect extends StatusEffect {
    protected ObsidianShardsEffect(StatusEffectCategory category, int color) {
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

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        float range = 1.65F;
        Box radius = new Box(livingEntity.getX() + range,
                livingEntity.getY() + (float) range / 3,
                livingEntity.getZ() + range,
                livingEntity.getX() - range,
                livingEntity.getY() - (float) range / 3,
                livingEntity.getZ() - range);

        for(Entity entities : livingEntity.getEntityWorld().getOtherEntities(livingEntity, radius, EntityPredicates.VALID_LIVING_ENTITY)){
            if (entities != null) {
                if(entities instanceof LivingEntity target && !isProtected(target,livingEntity)){
                    target.damage(target.getDamageSources().magic(), 3.0F * (amplifier +1 ));
                    livingEntity.removeStatusEffect(Effects.OBSIDIAN_SHARDS);
                }

            }
        }
    }
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 30 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
