package com.lne_wizards.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellSchool;
import org.joml.Vector3f;

public class MagicOrbBlock extends Block {

    private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 12, 12);
    private static final int EFFECT_DURATION_TICKS = 18000; // 15 minutes
    private static final int EFFECT_AMPLIFIER = 2;

    public final SpellSchool spellSchool;

    public MagicOrbBlock(SpellSchool spellSchool, AbstractBlock.Settings settings) {
        super(settings);
        this.spellSchool = spellSchool;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ParticleBatch burst = new ParticleBatch(
                SpellEngineParticles.MagicParticles.get(
                        SpellEngineParticles.MagicParticles.Shape.SPELL,
                        SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
                ParticleBatch.Shape.SPHERE,
                ParticleBatch.Origin.CENTER,
                null, 50, 0.5f, 0.8f, 0
        );
        burst.color_rgba = colorToRgba(spellSchool.color);
        burst.scale = 0.9f;
        burst.max_age = 1.5f;
        if (world.isClient()) return ActionResult.SUCCESS;
        if (spellSchool.ownedBoostEffect == null) return ActionResult.PASS;

        var effectId = Registries.STATUS_EFFECT.getId(spellSchool.ownedBoostEffect);
        if (effectId == null) return ActionResult.PASS;

        var effectEntryOpt = Registries.STATUS_EFFECT.getEntry(effectId);
        if (effectEntryOpt.isEmpty()) return ActionResult.PASS;

        var effectEntry = effectEntryOpt.get();
        if (player.hasStatusEffect(effectEntry)) return ActionResult.PASS;

        player.addStatusEffect(new StatusEffectInstance(effectEntry, EFFECT_DURATION_TICKS, EFFECT_AMPLIFIER, false, false, true));
        if(!world.isClient){
            ParticleHelper.sendBatches(player, new ParticleBatch[]{burst});
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(4) != 0) return;
        int c = spellSchool.color;
        float r = (c >> 16 & 0xFF) / 255f;
        float g = (c >> 8 & 0xFF) / 255f;
        float b = (c & 0xFF) / 255f;
        double x = pos.getX() + 0.1 + random.nextDouble() * 0.8;
        double y = pos.getY() + 0.1 + random.nextDouble() * 0.8;
        double z = pos.getZ() + 0.1 + random.nextDouble() * 0.8;
        world.addParticle(new DustParticleEffect(new Vector3f(r, g, b), 0.7f),
                x, y, z, 0, 0.04 + random.nextDouble() * 0.04, 0);
    }

    private static long colorToRgba(int rgb) {
        long r = (rgb >> 16 & 0xFF);
        long g = (rgb >> 8 & 0xFF);
        long b = (rgb & 0xFF);
        return (r << 24) | (g << 16) | (b << 8) | 0xFFL;
    }
}
