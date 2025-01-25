package com.lne_wizards.mixin;

import com.lne_wizards.api.LneWizardsPassives;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.lne_wizards.LNE_Wizards_Mod.tweaksConfig;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At(value = "TAIL", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage$everfrostStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        int freeze_ticks = tweaksConfig.value.everfrost_staff_freeze_ticks_on_damage;
        int freeze_duration = tweaksConfig.value.everfrost_staff_freezing_effect_seconds;
        if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
            if(attacker instanceof PlayerEntity player){
                LneWizardsPassives.everFrostStaffPassive(player,entity,freeze_ticks,freeze_duration,source);
            }
        }
    }
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage$netherFlameStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        int max_amplifier = tweaksConfig.value.netherflame_staff_haste_max_amplifier -1;
        int duration = tweaksConfig.value.netherflame_staff_haste_duration;
        if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
            if(attacker instanceof PlayerEntity player){
                LneWizardsPassives.netherFlameStaffPassive(player,entity,max_amplifier,duration,source);
            }
        }
        return;
    }

    @Inject(method = "damage", at = @At(value = "TAIL", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private void damage$dragonStaff(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        LivingEntity entity = (LivingEntity)(Object)this;
        int max_amplifier = tweaksConfig.value.dragon_staff_arcane_precision_max_amplifier -1;
        int duration = tweaksConfig.value.dragon_staff_arcane_precision_duration;

        if(FabricLoader.getInstance().isModLoaded("loot_n_explore")) {
            if(attacker instanceof PlayerEntity player){
                LneWizardsPassives.arcaneDragonStaffPassive(player,entity,duration,max_amplifier,source);
            }
        }
    }
}
