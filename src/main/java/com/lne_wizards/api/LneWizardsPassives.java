package com.lne_wizards.api;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.item.weapons.*;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.util.HelperMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Box;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellPowerTags;

public class LneWizardsPassives {
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
    private static final ParticleBatch particlesEverfrostStaff = new ParticleBatch(
            "loot_n_explore:freezing_snowflake",
            ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET, null,
            50, 0.1F, 0.3F, 0);
    private static final ParticleBatch particlesNetherflameStaff = new ParticleBatch(
            "spell_engine:flame_spark",
            ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET, null,
            15, 0.1F, 0.4F, 0,1);
    private static final ParticleBatch particlesDragonStaff = new ParticleBatch(
            "dragon_breath",
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT, null,
            5, 0.01F, 0.1F, 0,1.5F);
    private static final ParticleBatch particlesZephyrWingStaff = new ParticleBatch(
            "more_rpg_classes:small_gust",
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.LAUNCH_POINT, null,
            10, 0.1F, 0.3F, 0,0.5F);
    private static final ParticleBatch particlesTideCallerStaff1 = new ParticleBatch(
            "more_rpg_classes:big_splash",
            ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET, null,
            50, 0.5F, 0.8F, 0,1.5F);
    private static final ParticleBatch particlesTideCallerStaff2 = new ParticleBatch(
            "more_rpg_classes:big_splash",
            ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET, null,
            40, 0.05F, 0.2F, 0,0);

    public static void netherFlameStaffPassive(LivingEntity attacker, LivingEntity target, int max_amplifier, int duration, DamageSource source){
        if(attacker instanceof PlayerEntity player && source.isIn(SpellPowerTags.DamageType.ALL) && !target.isSpectator()
                && target.isLiving()&& target.isOnFire()){
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof NetherflameStaff){
                HelperMethods.applyStatusEffect(player,0,duration, SpellPowerMechanics.HASTE.boostEffect,
                        max_amplifier,true,true,true,0);
                if (!player.getWorld().isClient()) {
                    ParticleHelper.sendBatches(player, new ParticleBatch[]{particlesNetherflameStaff});
                }
                float range = 2.0F;
                Box radius = new Box(target.getX() + range,
                        target.getY() + (float) range / 3,
                        target.getZ() + range,
                        target.getX() - range,
                        target.getY() - (float) range / 3,
                        target.getZ() - range);
                for(Entity entities : target.getEntityWorld().getOtherEntities(target, radius, EntityPredicates.VALID_LIVING_ENTITY)){
                    if (entities != null) {
                        if(entities instanceof LivingEntity targets && !isProtected(targets,player)){
                            targets.setFireTicks(60);
                        }
                    }
                }
            }
        }
    }
    public static void everFrostStaffPassive(LivingEntity attacker, LivingEntity target, int freeze_ticks, int freeze_duration, DamageSource source){
        if(source.isIn(SpellPowerTags.DamageType.ALL) && !target.isSpectator() && target.isLiving()){
            ItemStack stack = attacker.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof EverfrostStaff){
                HelperMethods.stackFreezeStacks(target,freeze_ticks);
                HelperMethods.applyStatusEffect(target,0,freeze_duration, Effects.FREEZING,
                        0,true,true,false,0);
                float range = 2.0F;
                Box radius = new Box(target.getX() + range,
                        target.getY() + (float) range / 3,
                        target.getZ() + range,
                        target.getX() - range,
                        target.getY() - (float) range / 3,
                        target.getZ() - range);
                for(Entity entities : target.getEntityWorld().getOtherEntities(target, radius, EntityPredicates.VALID_LIVING_ENTITY)){
                    if (entities != null) {
                        if(entities instanceof LivingEntity targets && !isProtected(targets,attacker)){
                            HelperMethods.stackFreezeStacks(targets,freeze_ticks);
                            HelperMethods.applyStatusEffect(targets,0,freeze_duration,Effects.FREEZING,
                                    0,true,true,false,0);
                            if (!target.getWorld().isClient()) {
                                ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesEverfrostStaff});
                            }
                        }
                    }
                }
            }
        }
    }
    public static void arcaneDragonStaffPassive(LivingEntity attacker, LivingEntity target, int duration, int max_amplifier, DamageSource source){
        if(source.isIn(SpellPowerTags.DamageType.ALL) && !target.isSpectator()
                && target.isLiving()){
            ItemStack stack = attacker.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof DragonStaff){
                if (!target.getWorld().isClient()) {
                    ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesDragonStaff});
                }
                HelperMethods.applyStatusEffect(target,0,duration, com.lne_wizards.effect.Effects.ARCANE_PRECISION,
                        max_amplifier,true,true,true,0);
            }
        }
    }
    public static void seismicStaffPassive(LivingEntity attacker, LivingEntity target, int duration, int max_amplifier, DamageSource source){
        if(source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) && !target.isSpectator()
                && target.isLiving()){
            ItemStack stack = attacker.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof SeismicStaff){
                HelperMethods.applyStatusEffect(attacker,0,duration, com.lne_wizards.effect.Effects.OBSIDIAN_SHARDS,
                        max_amplifier,true,true,false,0);
            }
        }
    }
    public static void zephyrwingStaffPassive(LivingEntity attacker, LivingEntity target, int duration, int max_amplifier, DamageSource source){
        if(source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) && !target.isSpectator()
                && target.isLiving()){
            ItemStack stack = attacker.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof ZephyrwingStaff){
                if (!attacker.getWorld().isClient()) {
                    ParticleHelper.sendBatches(attacker, new ParticleBatch[]{particlesZephyrWingStaff});
                }
                HelperMethods.applyStatusEffect(attacker,0,duration, com.lne_wizards.effect.Effects.ZEPHYRS_SPEED,
                        max_amplifier,true,true,false,0);
            }
        }
    }
    public static void tidecallerStaffPassive(LivingEntity attacker, LivingEntity target, int duration, int max_amplifier, DamageSource source){
        if(source.isIn(DamageTypeTags.WITCH_RESISTANT_TO) && !target.isSpectator()
                && target.isLiving()){
            ItemStack stack = attacker.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof TidecallerStaff){
                float damage = (float) attacker.getAttributeBaseValue(MoreSpellSchools.WATER.attribute) * 0.2F;
                if (!target.getWorld().isClient()) {
                    ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesTideCallerStaff1});
                }
                float range = 5.0F;
                Box radius = new Box(target.getX() + range,
                        target.getY() + (float) range / 3,
                        target.getZ() + range,
                        target.getX() - range,
                        target.getY() - (float) range / 3,
                        target.getZ() - range);
                for(Entity entities : target.getEntityWorld().getOtherEntities(target, radius, EntityPredicates.VALID_LIVING_ENTITY)){
                    if (entities != null) {
                        if(entities instanceof LivingEntity targets && !isProtected(targets,attacker)){
                            if (!target.getWorld().isClient()) {
                                if (!target.getWorld().isClient()) {
                                    ParticleHelper.sendBatches(targets, new ParticleBatch[]{particlesTideCallerStaff2});
                                }
                                targets.damage(targets.getDamageSources().magic(), damage);
                            }
                        }
                    }
                }
            }
        }
    }
}
