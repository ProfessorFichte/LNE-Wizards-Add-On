package com.lne_wizards.entity.mob.elemental_evokers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
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
import net.minecraft.world.World;
import net.more_rpg_classes.entity.ISpellCasterEntity;
import net.more_rpg_classes.entity.goal.MobSpellCastGoal;

import java.util.ArrayList;
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
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.0));

        MobSpellCastGoal primary   = new MobSpellCastGoal(this, getPrimarySpell(),   getMobCastGoals());
        MobSpellCastGoal secondary = new MobSpellCastGoal(this, getSecondarySpells(), getMobCastGoals());
        this.goalSelector.add(2, secondary);
        this.goalSelector.add(3, primary);
        getMobCastGoals().add(primary);
        getMobCastGoals().add(secondary);

        this.goalSelector.add(4, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6, 1.0));
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

    public boolean canParticipateInRaid() {
        return false;
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
}
