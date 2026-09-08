package com.lne_wizards.mixin;

import com.lne_wizards.LNE_Wizards_Mod;
import com.lne_wizards.entity.ModEntities;
import com.lne_wizards.entity.mob.elemental_evokers.ElementalEvokerEntity;
import net.spell_engine.Platform;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Raid.class)
public abstract class RaidMixin {

    @Shadow private ServerWorld world;

    @Shadow public abstract void addRaider(int wave, RaiderEntity raider, BlockPos pos, boolean isPatrolLeader);

    @Shadow public abstract int getGroupsSpawned();

    @Inject(method = "spawnNextWave", at = @At("RETURN"))
    private void spawnElementalEvokers(BlockPos pos, CallbackInfo ci) {
        if (!LNE_Wizards_Mod.tweaksConfig.value.custom_illager_variants_spawn_in_raids) return;

        int wave = getGroupsSpawned();
        if (wave < 4) return;

        boolean elementalWizardsLoaded = Platform.util().isModLoaded("elemental_wizards_rpg");

        List<EntityType<? extends ElementalEvokerEntity>> pool = new ArrayList<>();
        pool.add(ModEntities.FIRE_EVOKER);
        pool.add(ModEntities.FROST_EVOKER);
        pool.add(ModEntities.ARCANE_EVOKER);
        if (elementalWizardsLoaded) {
            pool.add(ModEntities.AIR_EVOKER);
            pool.add(ModEntities.EARTH_EVOKER);
            pool.add(ModEntities.WATER_EVOKER);
        }

        int count = wave >= 6 ? 2 : 1;
        for (int i = 0; i < count; i++) {
            EntityType<? extends ElementalEvokerEntity> type = pool.get(world.getRandom().nextInt(pool.size()));
            ElementalEvokerEntity evoker = type.create(world);
            if (evoker == null) continue;
            evoker.refreshPositionAndAngles(
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    world.getRandom().nextFloat() * 360f, 0f
            );
            evoker.initialize(world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null);
            world.spawnEntity(evoker);
            addRaider(wave, evoker, pos, false);
        }
    }
}
