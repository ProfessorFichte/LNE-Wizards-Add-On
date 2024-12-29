package com.lne_wizards.mixin;

import com.lne_wizards.item.weapons.EverfrostStaff;
import more_rpg_loot.effects.Effects;
import more_rpg_loot.util.HelperMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.Box;

import static com.lne_wizards.LNE_Wizards_Mod.tweaksConfig;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private int lastAttack = 0;
    private static final ParticleBatch particlesEverfrostStaff = new ParticleBatch(
            "loot_n_explore:freezing_snowflake",
            ParticleBatch.Shape.CIRCLE,
            ParticleBatch.Origin.FEET,
            null,
            50,
            0.1F,
            0.3F,
            0);


    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void onAttacking_everfrostStaff(Entity target, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        ItemStack stack = entity .getEquippedStack(EquipmentSlot.MAINHAND);
        Item item = stack.getItem();
        int freeze_ticks = tweaksConfig.value.everfrost_staff_freeze_ticks_on_damage;
        int freeze_duration = tweaksConfig.value.everfrost_staff_freezing_effect_seconds;

        if(item instanceof EverfrostStaff
                && target instanceof LivingEntity
                && !target.isSpectator()
                && lastAttack != entity.age
                && target.isLiving()){
            ParticleHelper.sendBatches(target, new ParticleBatch[]{particlesEverfrostStaff});
            HelperMethods.stackFreezeStacks((LivingEntity) target,freeze_ticks);
            HelperMethods.applyStatusEffect((LivingEntity) target,0,freeze_duration,Effects.FREEZING,
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
                    if(entities instanceof LivingEntity targets){
                        HelperMethods.stackFreezeStacks(targets,freeze_ticks);
                        HelperMethods.applyStatusEffect(targets,0,freeze_duration,Effects.FREEZING,
                                0,true,true,false,0);
                    }

                }
            }

        }

    }

}
