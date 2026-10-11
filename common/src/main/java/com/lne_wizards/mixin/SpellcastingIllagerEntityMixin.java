package com.lne_wizards.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.lne_wizards.entity.mob.elemental_evokers.ElementalEvokerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpellcastingIllagerEntity.class)
public abstract class SpellcastingIllagerEntityMixin {

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private boolean lne_wizards$skipVanillaSpellParticles(World world, ParticleEffect particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        return !((Object) this instanceof ElementalEvokerEntity);
    }
}
