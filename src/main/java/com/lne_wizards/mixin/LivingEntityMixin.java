package com.lne_wizards.mixin;

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
import net.more_rpg_classes.effect.MRPGCEffects;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellPowerTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.lne_wizards.LNE_Wizards_Mod.tweaksConfig;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow @Nullable private LivingEntity attacker;
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
    public boolean isProtected(Entity target, LivingEntity caster) {
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




    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage_everfrostStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        int freeze_ticks = tweaksConfig.value.everfrost_staff_freeze_ticks_on_damage;
        int freeze_duration = tweaksConfig.value.everfrost_staff_freezing_effect_seconds;

        if(attacker instanceof PlayerEntity player && source.isIn(SpellPowerTags.DamageType.ALL) && !entity.isSpectator() && entity.isLiving()){
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof EverfrostStaff){
                ParticleHelper.sendBatches(entity, new ParticleBatch[]{particlesEverfrostStaff});

                HelperMethods.stackFreezeStacks((LivingEntity) entity,freeze_ticks);
                HelperMethods.applyStatusEffect((LivingEntity) entity,0,freeze_duration,Effects.FREEZING,
                        0,true,true,false,0);
                float range = 2.0F;
                Box radius = new Box(entity.getX() + range,
                        entity.getY() + (float) range / 3,
                        entity.getZ() + range,
                        entity.getX() - range,
                        entity.getY() - (float) range / 3,
                        entity.getZ() - range);
                for(Entity entities : entity.getEntityWorld().getOtherEntities(entity, radius, EntityPredicates.VALID_LIVING_ENTITY)){
                    if (entities != null) {
                        if(entities instanceof LivingEntity targets && !isProtected(targets,player)){
                            HelperMethods.stackFreezeStacks(targets,freeze_ticks);
                            HelperMethods.applyStatusEffect(targets,0,freeze_duration,Effects.FREEZING,
                                    0,true,true,false,0);
                            HelperMethods.applyStatusEffect(targets,0,freeze_duration, MRPGCEffects.STUNNED,
                                    0,true,true,false,0);
                        }
                    }
                }
            }
        }

    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage_netherFlameStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        float multiplier = tweaksConfig.value.netherflame_staff_fire_increasing_multiplier;
        int max_amplifier = tweaksConfig.value.netherflame_staff_haste_max_amplifier -1;
        int duration = tweaksConfig.value.netherflame_staff_haste_duration;

        if(attacker instanceof PlayerEntity player && source.isIn(SpellPowerTags.DamageType.ALL) && !entity.isSpectator()
                && entity.isLiving()&&entity.isOnFire()){
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof NetherflameStaff){
                ParticleHelper.sendBatches(player, new ParticleBatch[]{particlesNetherflameStaff});
                HelperMethods.applyStatusEffect(player,0,duration, SpellPowerMechanics.HASTE.boostEffect,
                        max_amplifier,true,true,false,0);
                amount = amount * multiplier;
            }
        }
        return;
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage_dragonStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        int max_amplifier = tweaksConfig.value.dragon_staff_arcane_precision_max_amplifier -1;
        int duration = tweaksConfig.value.dragon_staff_arcane_precision_duration;

        if(attacker instanceof PlayerEntity player && source.isIn(SpellPowerTags.DamageType.ALL) && !entity.isSpectator()
                && entity.isLiving()){
            ItemStack stack = player.getEquippedStack(EquipmentSlot.MAINHAND);
            Item item = stack.getItem();
            if(item instanceof DragonStaff){
                ParticleHelper.sendBatches(entity, new ParticleBatch[]{particlesDragonStaff});
                HelperMethods.applyStatusEffect(entity,0,duration, com.lne_wizards.effect.Effects.ARCANE_PRECISION,
                        max_amplifier,true,true,false,0);
            }
        }
    }
}
