package com.lne_wizards.entity.spell_spawned;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.spell_engine.api.entity.SpellEntity;
import net.spell_engine.internals.target.EntityRelation;
import net.spell_engine.internals.target.EntityRelations;

import java.util.List;
import java.util.UUID;

public class IceWallEntity extends Entity implements SpellEntity.Spawned {
    public static EntityType<IceWallEntity> ENTITY_TYPE;

    // Emerge/submerge animation tick counts
    public static final int EMERGE_TICKS = 20;
    public static final int SUBMERGE_TICKS = 20;

    private LivingEntity owner;
    private UUID ownerUuid;
    private int timeToLive;
    private int maxTimeToLive;
    private int damageCooldown = 0;

    public IceWallEntity(EntityType<? extends IceWallEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    public void onSpawnedBySpell(Args args) {
        this.owner = args.owner();
        this.ownerUuid = owner.getUuid();
        this.timeToLive = (int)(args.spawnData().time_to_live_seconds * 20);
        this.maxTimeToLive = this.timeToLive;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) this.ownerUuid = nbt.getUuid("Owner");
        this.timeToLive = nbt.getInt("TimeToLive");
        this.maxTimeToLive = nbt.getInt("MaxTimeToLive");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (ownerUuid != null) nbt.putUuid("Owner", ownerUuid);
        nbt.putInt("TimeToLive", timeToLive);
        nbt.putInt("MaxTimeToLive", maxTimeToLive);
    }

    public LivingEntity getOwner() {
        if (owner == null && ownerUuid != null && getWorld() instanceof ServerWorld sw) {
            Entity e = sw.getEntity(ownerUuid);
            if (e instanceof LivingEntity living) owner = living;
        }
        return owner;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public float getEmergeProgress(float tickDelta) {
        int elapsed = maxTimeToLive - timeToLive;
        if (elapsed < EMERGE_TICKS) {
            return (elapsed + tickDelta) / (float) EMERGE_TICKS;
        }
        if (timeToLive <= SUBMERGE_TICKS) {
            return (timeToLive - tickDelta) / (float) SUBMERGE_TICKS;
        }
        return 1.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) return;

        LivingEntity owner = getOwner();
        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }

        if (--timeToLive <= 0) {
            discard();
            return;
        }

        if (damageCooldown > 0) damageCooldown--;

        // Push all entities out of the wall (solid behavior for everyone including owner/allies)
        List<Entity> inside = getWorld().getOtherEntities(this, getBoundingBox());
        for (Entity entity : inside) {
            if (entity instanceof LivingEntity) {
                Vec3d push = entity.getPos().subtract(getPos());
                push = new Vec3d(push.x, 0, push.z);
                if (push.length() < 0.001) push = new Vec3d(1, 0, 0);
                push = push.normalize().multiply(0.25);
                entity.setVelocity(entity.getVelocity().add(push));
                entity.velocityModified = true;
            }
        }

        // Damage enemies on contact
        if (damageCooldown == 0) {
            List<LivingEntity> enemies = getWorld().getNonSpectatingEntities(
                LivingEntity.class, getBoundingBox().expand(0.1)
            );
            for (LivingEntity entity : enemies) {
                if (entity == owner) continue;
                var relation = EntityRelations.getRelation(owner, entity);
                if (relation == EntityRelation.ALLY || relation == EntityRelation.FRIENDLY) continue;

                // TODO: Apply Spell Engine Impact here for enemies touching the wall
                damageCooldown = 10;
                break;
            }
        }
    }

    @Override
    public boolean canHit() { return true; }

    @Override
    public boolean isCollidable() { return true; }

    @Override
    public boolean isPushable() { return false; }
}
