package com.lne_wizards.api;

import com.lne_wizards.item.weapons.DragonStaff;
import com.lne_wizards.item.weapons.EverfrostStaff;
import com.lne_wizards.item.weapons.NetherflameStaff;
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
import net.minecraft.util.math.Box;
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
            ParticleBatch.Shape.CIRCLE,
            ParticleBatch.Origin.FEET,
            null,
            50,
            0.1F,
            0.3F,
            0);
    private static final ParticleBatch particlesNetherflameStaff = new ParticleBatch(
            "spell_engine:flame_spark",
            ParticleBatch.Shape.PILLAR,
            ParticleBatch.Origin.FEET,
            null,
            15,
            0.1F,
            0.4F,
            0);
    private static final ParticleBatch particlesDragonStaff = new ParticleBatch(
            "dragon_breath",
            ParticleBatch.Shape.SPHERE,
            ParticleBatch.Origin.LAUNCH_POINT,
            null,
            5,
            0.01F,
            0.1F,
            0);

    public static void netherFlameStaffPassive(LivingEntity attacker, LivingEntity target, int max_amplifier, int duration, DamageSource source){
        if(attacker instanceof PlayerEntity player && source.isIn(SpellPowerTags.DamageType.ALL) && !target.isSpectator()
                && target.isLiving()&& target.isOnFire()){
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof NetherflameStaff){
                HelperMethods.applyStatusEffect(player,0,duration, SpellPowerMechanics.HASTE.boostEffect,
                        max_amplifier,true,true,true,0);
                ParticleHelper.sendBatches(player, new ParticleBatch[]{particlesNetherflameStaff});
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
                            ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesEverfrostStaff});
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
                ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesDragonStaff});
                HelperMethods.applyStatusEffect(target,0,duration, com.lne_wizards.effect.Effects.ARCANE_PRECISION,
                        max_amplifier,true,true,true,0);
            }
        }
    }
}
