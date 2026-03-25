package com.lne_wizards.entity.spell_spawned;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.SpellHelper;
import net.minecraft.util.math.Vec3d;

import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.SpellPower;

import java.util.List;
import java.util.UUID;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class ExplosiveBubbleEntity extends Entity implements SpellEntity.Spawned {
    public static EntityType<ExplosiveBubbleEntity> ENTITY_TYPE;

    public static final Color WATER_SPELL_COLOR = Color.from(0xa7ffed);
    private static final ParticleBatch areaExplodeParticles = new ParticleBatch(
            SpellEngineParticles.area_effect_293.id().toString(),
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
            1, 0, 0)
            .scale(1.5F).color(WATER_SPELL_COLOR.toRGBA());
    private static final ParticleBatch bubblePopParticles = new ParticleBatch("more_rpg_classes:bubble",
            ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
            50.0F, 0.3F, 1.0F);
    private static final ParticleBatch bubbleDespawnParticles = new ParticleBatch("more_rpg_classes:bubble",
            ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
            10.0F, 0.01F, 0.2F);

    private LivingEntity owner;
    private UUID ownerUuid;
    private int timeToLive;
    private boolean exploded = false;

    public ExplosiveBubbleEntity(EntityType<? extends ExplosiveBubbleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        this.owner = args.owner();
        this.ownerUuid = owner.getUuid();
        this.timeToLive = (args.spawnData().time_to_live_seconds * 20);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.timeToLive = nbt.getInt("TimeToLive");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TimeToLive", timeToLive);
    }

    public LivingEntity getOwner() {
        if (owner == null && ownerUuid != null && getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(ownerUuid);
            if (e instanceof LivingEntity living) owner = living;
        }
        return owner;
    }

    private int idleSoundTick = 0;

    @Override
    public void tick() {
        super.tick();
        var world = this.getWorld();

        if (world.isClient) return;

        if (idleSoundTick-- <= 0) {
            world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
            idleSoundTick = 60;
        }

        if (exploded) return;

        LivingEntity owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }

        if (--timeToLive <= 0) {
            if(!this.getWorld().isClient()) {
                ParticleHelper.sendBatches(this, new ParticleBatch[]{bubbleDespawnParticles});
            }
            world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.PLAYERS, 1F, 1F);
            discard();
            return;
        }

        List<LivingEntity> nearby = getWorld().getNonSpectatingEntities(
            LivingEntity.class, getBoundingBox().expand(0.3)
        );
        for (LivingEntity entity : nearby) {
            if (entity == owner) continue;
            explode(owner);
            return;
        }
    }

    private void explode(LivingEntity owner) {
        RegistryEntry<Spell> helper_spell = SpellRegistry.from(owner.getWorld()).getEntry(Identifier.of(MOD_ID, "helper/explosive_bubbles_impact")).get();
        exploded = true;
        if(!this.getWorld().isClient()) {
            ParticleHelper.sendBatches(this, new ParticleBatch[]{areaExplodeParticles});
            ParticleHelper.sendBatches(this, new ParticleBatch[]{bubblePopParticles});
        }
        Spell spell = helper_spell.value();
        SpellHelper.ImpactContext ctx = new SpellHelper.ImpactContext()
                .power(SpellPower.getSpellPower(spell.school, owner))
                .position(this.getPos());
        for (Entity entity : TargetHelper.targetsFromArea(owner.getWorld(), null, this.getPos(),
                Vec3d.ZERO, spell.range,
                spell.target != null ? spell.target.area : new Spell.Target.Area(),
                e -> e != owner)) {
            SpellHelper.performImpacts(owner.getWorld(), owner, entity, owner, helper_spell, spell.impacts, ctx, false, null);
            if (!spell.impacts.isEmpty()) {
                ParticleHelper.sendBatches(entity, spell.impacts.get(0).particles);
            }
        }
        discard();
    }

    @Override
    public boolean isCollidable() { return false; }

    @Override
    public boolean isPushable() { return false; }
}
