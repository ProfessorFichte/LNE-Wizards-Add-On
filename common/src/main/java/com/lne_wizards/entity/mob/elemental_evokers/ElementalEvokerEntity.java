package com.lne_wizards.entity.mob.elemental_evokers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.more_rpg_classes.entity.ISpellCasterEntity;
import net.more_rpg_classes.entity.goal.MobSpellCastGoal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class ElementalEvokerEntity extends EvokerEntity implements ISpellCasterEntity {

    private List<MobSpellCastGoal> mobCastGoals;

    public ElementalEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    private List<MobSpellCastGoal> getMobCastGoals() {
        if (mobCastGoals == null) mobCastGoals = new ArrayList<>();
        return mobCastGoals;
    }

    public abstract String getPrimarySpell();

    public abstract String getSecondarySpells();

    public abstract Identifier getWandItemId();

    //Minimum distance from the combat target before the evoker backs away.
    protected float getFleeDistance() {
        return 4.0F;
    }

    //Flee radius used when health drops below 35%.
    protected float getLowHealthFleeDistance() {
        return 6.0F;
    }

    @Override
    public void startSpellCast(int ticks) {
        this.setSpell(net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.SUMMON_VEX);
        try {
            java.lang.reflect.Field spellTicksField = net.minecraft.entity.mob.SpellcastingIllagerEntity.class
                    .getDeclaredField("spellTicks");
            spellTicksField.setAccessible(true);
            spellTicksField.setInt(this, ticks);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void stopSpellCast() {
        this.setSpell(net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.NONE);
        try {
            java.lang.reflect.Field spellTicksField = net.minecraft.entity.mob.SpellcastingIllagerEntity.class
                    .getDeclaredField("spellTicks");
            spellTicksField.setAccessible(true);
            spellTicksField.setInt(this, 0);
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean isCastingSpell() {
        return this.getSpell() != net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.NONE;
    }

    @Override
    public MobEntity asMobEntity() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        for (MobSpellCastGoal goal : getMobCastGoals()) {
            goal.updateCooldown();
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        // Flee at low health — highest priority, overrides spell casting
        this.goalSelector.add(1, new LowHealthFleeGoal(this, getLowHealthFleeDistance()));
        // Back away just enough when target closes in, then stop so spells can fire
        this.goalSelector.add(2, new BackAwayGoal(getFleeDistance(), 1.2));

        MobSpellCastGoal primary   = new MobSpellCastGoal(this, getPrimarySpell(),   getMobCastGoals());
        MobSpellCastGoal secondary = new MobSpellCastGoal(this, getSecondarySpells(), getMobCastGoals());
        this.goalSelector.add(3, secondary);
        this.goalSelector.add(4, primary);
        getMobCastGoals().add(primary);
        getMobCastGoals().add(secondary);

        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

        this.targetSelector.add(1, new RevengeGoal(this, new Class[]{RaiderEntity.class}).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createElementalEvokerAttributes() {
        return EvokerEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0);
    }

    @Override
    protected SoundEvent getCastSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOKER_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_EVOKER_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(net.minecraft.entity.damage.DamageSource source) {
        return SoundEvents.ENTITY_EVOKER_HURT;
    }

    private static class RevengeGoal extends net.minecraft.entity.ai.goal.RevengeGoal {
        public RevengeGoal(ElementalEvokerEntity mob, Class<?>... noRevengeTypes) {
            super(mob, noRevengeTypes);
        }
    }

    private static class ActiveTargetGoal<T extends net.minecraft.entity.LivingEntity> extends net.minecraft.entity.ai.goal.ActiveTargetGoal<T> {
        public ActiveTargetGoal(ElementalEvokerEntity mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }
    }

    // Backs away from the current combat target until it is >= minDistance blocks away.
    // Stops immediately once safe — spells can fire right away after.
    private class BackAwayGoal extends Goal {
        private final float minDistance;
        private final double speed;
        private int navigationTimer;

        public BackAwayGoal(float minDistance, double speed) {
            this.minDistance = minDistance;
            this.speed = speed;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (ElementalEvokerEntity.this.isCastingSpell()) return false;
            LivingEntity target = ElementalEvokerEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            return ElementalEvokerEntity.this.squaredDistanceTo(target) < minDistance * minDistance;
        }

        @Override
        public boolean shouldContinue() {
            if (ElementalEvokerEntity.this.isCastingSpell()) return false;
            LivingEntity target = ElementalEvokerEntity.this.getTarget();
            if (target == null || !target.isAlive()) return false;
            return ElementalEvokerEntity.this.squaredDistanceTo(target) < minDistance * minDistance;
        }

        @Override
        public void start() {
            navigationTimer = 0;
            updateNavigation();
        }

        @Override
        public void tick() {
            if (--navigationTimer <= 0) {
                navigationTimer = 5;
                updateNavigation();
            }
        }

        private void updateNavigation() {
            LivingEntity target = ElementalEvokerEntity.this.getTarget();
            if (target == null) return;
            Vec3d awayDir = ElementalEvokerEntity.this.getPos().subtract(target.getPos()).normalize();
            Vec3d dest = ElementalEvokerEntity.this.getPos().add(awayDir.multiply(minDistance + 1.0));
            ElementalEvokerEntity.this.getNavigation().startMovingTo(dest.x, dest.y, dest.z, speed);
        }

        @Override
        public void stop() {
            ElementalEvokerEntity.this.getNavigation().stop();
        }
    }

    private static class LowHealthFleeGoal extends FleeEntityGoal<PlayerEntity> {
        private static final float LOW_HEALTH_THRESHOLD = 0.35f;
        private final ElementalEvokerEntity evoker;

        public LowHealthFleeGoal(ElementalEvokerEntity evoker, float fleeDistance) {
            super(evoker, PlayerEntity.class, fleeDistance, 1.0, 1.2);
            this.evoker = evoker;
        }

        @Override
        public boolean canStart() {
            if (evoker.getHealth() > evoker.getMaxHealth() * LOW_HEALTH_THRESHOLD) return false;
            return super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            if (evoker.getHealth() > evoker.getMaxHealth() * LOW_HEALTH_THRESHOLD) return false;
            return super.shouldContinue();
        }
    }
}
