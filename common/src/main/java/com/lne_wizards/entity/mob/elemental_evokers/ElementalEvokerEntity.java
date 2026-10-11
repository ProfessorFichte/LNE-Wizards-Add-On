package com.lne_wizards.entity.mob.elemental_evokers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import com.mrpg_lib.entity.ISpellCasterEntity;
import com.mrpg_lib.entity.config.EntityAttributeApplier;
import com.mrpg_lib.entity.config.EntityAttributeConfig;
import com.mrpg_lib.entity.goal.BackAwayGoal;
import com.mrpg_lib.entity.goal.IConditionalFleeEntity;
import com.mrpg_lib.entity.goal.LowHealthFleeGoal;
import com.mrpg_lib.compat.spell_engine.MobSpellCastGoal;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ElementalEvokerEntity extends EvokerEntity implements ISpellCasterEntity, IConditionalFleeEntity {
    public static final String WAND_FALLBACK = "minecraft:stick";
    public static final double WAND_SPELL_POWER = 5.0;

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
    public float getFleeDistance() {
        return 4.0F;
    }

    @Override
    public float getLowHealthFleeDistance() {
        return 6.0F;
    }

    @Override
    public List<Identifier> getFleeImmuneEffects() {
        return Collections.emptyList();
    }

    @Override
    public List<Identifier> getFleeIgnoreIfTargetHasEffects() {
        return Collections.emptyList();
    }

    @Override
    public float getFleeIgnoreTargetHpThreshold() {
        return 0.0f;
    }

    @Override
    public void startSpellCast(int ticks) {
        this.setSpell(net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.SUMMON_VEX);
        this.spellTicks = ticks;
    }

    @Override
    public void stopSpellCast() {
        this.setSpell(net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.NONE);
        this.spellTicks = 0;
    }

    @Override
    public boolean isMobCasting() {
        return this.getSpell() != net.minecraft.entity.mob.SpellcastingIllagerEntity.Spell.NONE;
    }

    @Override
    public MobEntity asMobEntity() {
        return this;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LowHealthFleeGoal<>(this));
        this.goalSelector.add(2, new BackAwayGoal<>(this, getFleeDistance(), 1.2));

        MobSpellCastGoal primary   = new MobSpellCastGoal(this, getPrimarySpell(),    getMobCastGoals()).withAnimations(true);
        MobSpellCastGoal secondary = new MobSpellCastGoal(this, getSecondarySpells(), getMobCastGoals()).withAnimations(true);
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

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData data = super.initialize(world, difficulty, spawnReason, entityData);
        EntityAttributeApplier.apply(this,
                com.lne_wizards.LNE_Wizards_Mod.entityAttributeConfig != null ? com.lne_wizards.LNE_Wizards_Mod.entityAttributeConfig.value : null,
                withWand(spellPowerAttributes(), getWandItemId()));
        return data;
    }

    protected EntityAttributeConfig.Entry spellPowerAttributes() {
        return new EntityAttributeConfig.Entry();
    }

    public static double spellPowerBase(double totalSpellPower) {
        return totalSpellPower - WAND_SPELL_POWER;
    }

    public static EntityAttributeConfig.Entry withWand(EntityAttributeConfig.Entry entry, Identifier wandId) {
        return entry.setEquipment(EquipmentSlot.MAINHAND, EntityAttributeConfig.EquipmentValue.of(wandId.toString())
                .requiresAnyMod(wandId.getNamespace())
                .fallback(WAND_FALLBACK)
                .dropChance(0.0));
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

    private static class ActiveTargetGoal<T extends LivingEntity> extends net.minecraft.entity.ai.goal.ActiveTargetGoal<T> {
        public ActiveTargetGoal(ElementalEvokerEntity mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }
    }
}
