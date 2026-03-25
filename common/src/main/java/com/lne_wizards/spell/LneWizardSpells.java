package com.lne_wizards.spell;

import com.lne_wizards.effect.LNE_WizardsEffects;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.sounds.MRPGLibSounds;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.entity.SpellEntityPredicates;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class LneWizardSpells {
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    // ACTIVE SPELLS
    public static Entry fire_flamerush = add(fire_flamerush());
    private static Entry fire_flamerush() {
        var id = Identifier.of(MOD_ID, "fire_flamerush");
        var effect = LNE_WizardsEffects.FLAME_RUSH;
        var title = "Flamerush";
        var description = "Rushes forward and leaves flame clouds on the trail that deals {flamecloud_damage} damage. " +
                "Also increasing movement speed by {bonus2} and fire spell power by {bonus} for {effect_duration}.";
        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.FIRE;
        spell.tier = 5;
        spell.range = 10F;

        spell.target.type = Spell.Target.Type.CASTER;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_ground_release");
        spell.release.sound = Sound.withVolume(Identifier.of("spell_engine:generic_fire_release"), 1.0F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.02F, 0.3F).extent(1),
                new ParticleBatch(SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.02F, 0.35F).extent(3)
        };

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);

        var custom = new Spell.Impact();
        custom.action = new Spell.Impact.Action();
        custom.action.custom = new Spell.Impact.Action.Custom();
        custom.action.type = Spell.Impact.Action.Type.CUSTOM;
        custom.action.custom.intent = SpellTarget.Intent.HELPFUL;
        custom.action.custom.handler = "more_rpg_classes:forward_dash_range";

        var customCloud = new Spell.Impact();
        customCloud.action = new Spell.Impact.Action();
        customCloud.action.custom = new Spell.Impact.Action.Custom();
        customCloud.action.type = Spell.Impact.Action.Type.CUSTOM;
        customCloud.action.custom.intent = SpellTarget.Intent.HELPFUL;
        customCloud.action.custom.handler = "lne_wizards:flamerush_cloud";

        spell.impacts = List.of(custom, customCloud, buff);
        SpellBuilder.Cost.cooldown(spell, 30);
        SpellBuilder.Cost.item(spell, "runes:fire_stone", 1);

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var world = args.player().getWorld();
            if (world == null) return args.description();
            var modifier = effect.config().attributes().get(1);
            var modifier2 = effect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var bonus2 = SpellTooltip.bonus(modifier2.value, modifier2.operation);
            var optional = SpellRegistry.from(world).getEntry(Identifier.of(MOD_ID, "helper/flamerush_cloud"));
            if (optional.isEmpty()) return args.description();
            var estimated = SpellHelper.estimate(optional.get().value(), args.player(), ItemStack.EMPTY);
            var desc = args.description();
            if (!estimated.damage().isEmpty()) {
                var dmg = estimated.damage().get(0);
                desc = desc.replace("{flamecloud_damage}", SpellTooltip.formattedRange(dmg.min(), dmg.max()));
            }
            return desc.replace("{bonus}", bonus).replace("{bonus2}", bonus2);
        };

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry frost_ray = add(frost_ray());
    private static Entry frost_ray() {
        var id = Identifier.of(MOD_ID, "frost_ray");
        var title = "Ray of Frost";
        var description = "Casts a cold beam, causing {damage} damage and slowly freezes the target.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.FROST;
        spell.range = 32;
        spell.tier = 5;

        SpellBuilder.Casting.channel(spell, 7, 8);
        spell.active.cast.animation = PlayerAnimation.of("more_rpg_classes:left_handed_channeling");
        spell.active.cast.sound = Sound.withVolume(Identifier.of("more_rpg_classes:frost_crackle_long"), 1.3F);
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.CONE, ParticleBatch.Origin.LAUNCH_POINT,
                        ParticleBatch.Rotation.LOOK,
                        4, 1.0F, 3.0F, 3)
        };

        spell.target.type = Spell.Target.Type.BEAM;
        spell.target.beam = new Spell.Target.Beam();
        spell.target.beam.texture_id = "lne_wizards:textures/entity/frost_ray.png";
        spell.target.beam.width = 0.02F;
        spell.target.beam.flow = 200;
        spell.target.beam.block_hit_particles = new ParticleBatch[]{
                new ParticleBatch(
                        "lne_wizards:frost_ray",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        13, 0.1F, 0.3F
                ).rotate(ParticleBatch.Rotation.LOOK)
        };

        var frozenHurts = SpellBuilder.ImpactModifiers.create("#minecraft:freeze_hurts_extra_types");
        frozenHurts.modifier = new Spell.Impact.Modifier();
        frozenHurts.modifier.power_multiplier = 0.3F;

        var frozenImmune = SpellBuilder.ImpactModifiers.create("#minecraft:freeze_immune_entity_types");
        frozenImmune.modifier = new Spell.Impact.Modifier();
        frozenImmune.modifier.power_multiplier = -0.3F;

        var damage = SpellBuilder.Impacts.damage(1.0F);
        damage.target_modifiers = List.of(frozenHurts, frozenImmune);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "spell_engine:magic_frost_impact_burst",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.2F, 0.7F
                ),
                new ParticleBatch(
                        SpellEngineParticles.snowflake.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        20, 0.1F, 0.4F
                )
        };
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:frost_crackle"), 0.4F);

        var frosted = SpellBuilder.Impacts.effectAdd("more_rpg_classes:frosted", 5, 1, 5);
        frosted.action.status_effect.show_particles = false;

        spell.impacts = List.of(damage, frosted);

        spell.cost.exhaust = 0.5F;
        SpellBuilder.Cost.item(spell, "runes:frost_stone");
        SpellBuilder.Cost.cooldown(spell, 22);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description, null);
    }
    public static Entry arcane_starfall = add(arcane_starfall());
    private static Entry arcane_starfall() {
        var id = Identifier.of(MOD_ID, "arcane_starfall");
        var title = "Falling Star";
        var description = "Calls a falling astral star, causing {damage} arcane spell damage. Consumes all Arcane Charges.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.ARCANE;
        spell.range = 22.0F;
        spell.tier = 5;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("more_rpg_classes:two_handed_jump_release");
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                        1, 0.05F, 0.1F
                ).color(4284940287L)
        };
        spell.release.sound = new Sound(Identifier.of("wizards:arcane_missile_release").toString());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = true;

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        spell.deliver.meteor = new Spell.Delivery.Meteor();
        spell.deliver.meteor.launch_height = 15;
        spell.deliver.meteor.launch_radius = 4;
        spell.deliver.meteor.launch_properties.velocity = 2.5F;

        var meteorProjectile = new Spell.ProjectileData();
        meteorProjectile.client_data = new Spell.ProjectileData.Client();
        meteorProjectile.client_data.light_level = 12;
        meteorProjectile.client_data.travel_particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.MagicParticles.get(
                                SpellEngineParticles.MagicParticles.Shape.SPELL,
                                SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        10, 0.0F, 0.1F
                ).rotate(ParticleBatch.Rotation.LOOK).color(4284940287L),
                new ParticleBatch(
                        "dragon_breath",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        5, 0.1F, 0.3F
                ).rotate(ParticleBatch.Rotation.LOOK)
        };
        meteorProjectile.client_data.model = new Spell.ProjectileModel();
        meteorProjectile.client_data.model.model_id = "lne_wizards:spell_projectile/starfall";
        spell.deliver.meteor.projectile = meteorProjectile;

        var damage = SpellBuilder.Impacts.damage(1.5F, 2.0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.area_effect_293.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0.0F, 0.0F
                ).scale(0.8F).color(4284940287L),
                new ParticleBatch(
                        "spell_engine:magic_arcane_impact_burst",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        30, 0.5F, 1.5F
                ).extent(2.0F).color(4284940287L),
                new ParticleBatch(
                        "spell_engine:magic_arcane_impact_burst",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.2F, 0.7F
                ).extent(2.0F).color(4284940287L),
                new ParticleBatch(
                        "spell_engine:magic_arcane_impact_burst",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        20, 0.1F, 0.4F
                ).extent(2.0F).color(4284940287L)
        };
        damage.sound = new Sound(Identifier.of("more_rpg_classes:arcane_strong_impact").toString());

        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 4.0F;
        spell.area_impact.area = new Spell.Target.Area();
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.sound = Sound.withVolume(Identifier.of("wizards:arcane_blast_impact"), 1.5F);

        spell.cost.exhaust = 0.5F;
        spell.cost.effect_id = "wizards:arcane_charge";
        SpellBuilder.Cost.item(spell, "runes:arcane_stone");
        SpellBuilder.Cost.cooldown(spell, 20);

        return new Entry(id, spell, title, description, null);
    }
    public static final Entry aqua_explosive_bubbles = add(aqua_explosive_bubbles());
    private static Entry aqua_explosive_bubbles() {
        var id = Identifier.of(MOD_ID, "aqua_explosive_bubbles");
        var title = "Explosive Bubbles";
        var description = "Spawns explosive Bubbles behind the caster that deal {bubb_damage} damage or heal allies by {bubb_heal} when they explode on contact.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = MoreSpellSchools.WATER;
        spell.range = 10;
        spell.tier = 5;

        SpellBuilder.Casting.channel(spell, 4, 8);
        spell.active.cast.animation = PlayerAnimation.of("more_rpg_classes:floating_spawn_channel");
        spell.active.cast.sound = new Sound (Identifier.of("more_rpg_classes:water_bubbles"));
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:bubble",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1.0F, 0.001F, 0.1F)
        };
        spell.active.cast.movement_speed = 1.0F;

        spell.release = new Spell.Release();

        int delay = 0;
        int toLiveSeconds = 8;
        String entityId = "lne_wizards:explosive_bubble";
        var spawn = new Spell.Impact();
        spawn.action = new Spell.Impact.Action();
        spawn.action.type = Spell.Impact.Action.Type.SPAWN;
        var bubble = new Spell.Impact.Action.Spawn();
        bubble.entity_type_id = entityId;
        bubble.delay_ticks = delay;
        bubble.time_to_live_seconds = toLiveSeconds;
        bubble.placement.apply_yaw = true;
        bubble.placement.location_offset_by_look = 2;
        bubble.placement.location_yaw_offset = -160;
        spawn.action.spawns = List.of(bubble);

        spell.impacts = List.of(spawn);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 32);
        spell.cost.cooldown.haste_affected = true;
        SpellBuilder.Cost.item(spell, "more_rpg_classes:aqua_stone", 1);

        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var world = args.player().getWorld();
            if (world == null) return args.description();
            var optional = SpellRegistry.from(world).getEntry(Identifier.of(MOD_ID, "helper/explosive_bubbles_impact"));
            if (optional.isEmpty()) return args.description();
            var estimated = SpellHelper.estimate(optional.get().value(), args.player(), ItemStack.EMPTY);
            var desc = args.description();
            if (!estimated.damage().isEmpty()) {
                var dmg = estimated.damage().get(0);
                desc = desc.replace("{bubb_damage}", SpellTooltip.formattedRange(dmg.min(), dmg.max()));
            }
            if (!estimated.heal().isEmpty()) {
                var heal = estimated.heal().get(0);
                desc = desc.replace("{bubb_heal}", SpellTooltip.formattedRange(heal.min(), heal.max()));
            }
            return desc;
        };
        return new Entry(id, spell, title, description,mutator);
    }
    public static final Entry wind_aeroburst = add(wind_aeroburst());
    private static Entry wind_aeroburst() {
        var id = Identifier.of(MOD_ID, "wind_aeroburst");
        var title = "Aeroburst";
        var description = "Release an explosive burst of air in all directions, knocking back all nearby enemies with tremendous force and dealing {damage} damage.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = MoreSpellSchools.AIR;
        spell.range = 8;
        spell.tier = 5;

        spell.active.cast.duration = 1.25F;
        spell.active.cast.animation = PlayerAnimation.of("more_rpg_classes:two_handed_sky_channeling");
        spell.active.cast.sound = new Sound("spell_engine:generic_wind_charging");
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:small_gust",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        20, 0.2F, 0.5F),
                new ParticleBatch("more_rpg_classes:small_gust",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        5.0F, 0.05F, 0.2F)
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.target.area.angle_degrees = 360.0F;

        spell.release = new Spell.Release();
        spell.release.animation = PlayerAnimation.of("more_rpg_classes:two_handed_ground_release");
        spell.release.sound = new Sound("more_rpg_classes:air_explosion");
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch("spell_engine:smoke_medium",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        50.0F, 0.6F, 2.0F).extent(2).invert(),
                new ParticleBatch("more_rpg_classes:water_mist",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        100.0F, 0.6F, 2.0F).extent(2).invert(),
                new ParticleBatch("spell_engine:smoke_medium",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        50.0F, 1.0F, 2.4F).extent(4),
                new ParticleBatch("spell_engine:smoke_medium",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        50.0F, 1.4F, 3.0F).extent(6)
        };

        var damage = SpellBuilder.Impacts.damage(0.8F, 10.0F);
        damage.particles = new ParticleBatch[]{};
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:air_magic_impact2"), 0.4F);

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 25);
        spell.cost.cooldown.haste_affected = true;
        SpellBuilder.Cost.item(spell, "more_rpg_classes:storm_stone", 1);

        return new Entry(id, spell, title, description,null);
    }
    public static Entry terra_rock_crash = add(terra_rock_crash());
    private static Entry terra_rock_crash() {
        var id = Identifier.of(MOD_ID, "terra_rock_crash");
        var spell = SpellBuilder.createSpellActive();
        var title = "Rock Crash";
        var description = "Summons a giant rock above the target with a large area impact dealing {damage} damage.";
        spell.school = MoreSpellSchools.EARTH;
        spell.tier = 5;
        spell.range = 32;

        spell.learn = new Spell.Learn();

        spell.active.cast.duration = 1.5F;
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_sky_charge");
        spell.active.cast.sound = new Sound(MRPGLibSounds.EARTH_MAGIC_CAST_1.id());
        spell.active.cast.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:stone_particle",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        3.0F, 0.01F, 0.05F)
        };

        spell.release = new Spell.Release();
        spell.release.sound = new Sound(MRPGLibSounds.EARTH_MAGIC_IMPACT_2.id().toString());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = true;
        spell.target.aim.use_caster_as_fallback = true;

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        spell.deliver.meteor = new Spell.Delivery.Meteor();
        spell.deliver.meteor.launch_height = 15;
        spell.deliver.meteor.launch_radius = 4;
        spell.deliver.meteor.launch_properties.velocity = 0.6F;

        var projectile = new Spell.ProjectileData();
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        "smoke",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 10, 0.1F, 0.5F, 0),
                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK, 10, 0.1F, 0.0F, 0)
        };
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "elemental_wizards_rpg:spell_projectile/meteor";
        projectile.client_data.model.scale = 1.7F;
        spell.deliver.meteor.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(1.0F, 3.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(SpellEngineParticles.smoke_large.id().toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        5, 0.5F, 3F)
        };
        damage.sound = new Sound("more_rpg_classes:earth_magic_impact1");
        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 10;
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.particles = new ParticleBatch[] {
                new ParticleBatch("smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        90, 1.0F, 3.0F)
        };
        spell.area_impact.sound = new Sound("more_rpg_classes:earth_magic_impact3");

        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.item(spell, "more_rpg_classes:terra_stone", 1);
        SpellBuilder.Cost.cooldown(spell, 32);

        return new Entry(id, spell, title, description,null);
    }
    // FUNCTIONAL ACTIVE HELPER
    public static Entry flamerush_cloud = add(flamerush_cloud());
    private static Entry flamerush_cloud() {
        var id = Identifier.of(MOD_ID, "helper/flamerush_cloud");
        var name = "";
        var description = "";

        var spell = SpellBuilder.createSpellActive();
        spell.range = 0;
        spell.tier = 5;
        spell.school = SpellSchools.FIRE;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 1.0F;
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.volume.sound = new Sound("wizards:fire_scorch_impact");
        cloud.delay_ticks = 0;
        cloud.impact_tick_interval = 15;
        cloud.time_to_live_seconds = 6;
        cloud.spawn.sound = new Sound("wizards:fire_wall_ignite");
        cloud.spawn.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.05F, 0.1F)
        };
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_ground.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        2, 0, 0),
                new ParticleBatch(SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        3, 0.02F, 0.1F),
                new ParticleBatch(SpellEngineParticles.flame_spark.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        4, 0.05F, 0.1F),
                new ParticleBatch("campfire_cosy_smoke",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        0.1F, 0.05F, 0.1F),
        };

        cloud.placement = SpellBuilder.Deliver.placementByLook(0.5f, 0, 0);
        cloud.additional_placements = List.of(
                SpellBuilder.Deliver.placementByLook(1.5f, 0, 1),
                SpellBuilder.Deliver.placementByLook(3f, 0, 2),
                SpellBuilder.Deliver.placementByLook(4.5f, 0, 3),
                SpellBuilder.Deliver.placementByLook(6f, 0, 4),
                SpellBuilder.Deliver.placementByLook(7.5f, 0, 5),
                SpellBuilder.Deliver.placementByLook(9.0f, 0, 6)
        );

        spell.deliver.clouds = List.of(cloud);

        var damage = SpellBuilder.Impacts.damage(0.35F, 0.0F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.01F, 0.1F),
                new ParticleBatch("flame",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.01F, 0.1F)
        };
        damage.sound = new Sound("wizards:fire_scorch_impact");
        var fire = SpellBuilder.Impacts.fire(2);
        spell.impacts = List.of(damage, fire);
        SpellBuilder.Cost.cooldown(spell, 20);

        return new Entry(id, spell, name, description, null);
    }
    public static final Entry explosive_bubbles_impact = add(explosive_bubbles_impact());
    private static Entry explosive_bubbles_impact() {
        var id = Identifier.of(MOD_ID, "helper/explosive_bubbles_impact");
        var title = "";
        var description = "";

        var spell = SpellBuilder.createSpellActive();
        spell.school = MoreSpellSchools.WATER;
        spell.range = 3;
        spell.tier = 5;

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.target.area.angle_degrees = 360.0F;

        spell.release = new Spell.Release();

        var damage = SpellBuilder.Impacts.damage(0.85F, 0);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:bubble_pop",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        10.0F, 0.05F, 0.2F),
                new ParticleBatch("more_rpg_classes:bubble",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10.0F, 0.001F, 0.1F)
        };
        damage.sound = Sound.withVolume(Identifier.of(MRPGLibSounds.WATER_BUBBLE_EXPLODE.id().toString()), 2.0F);

        var soaked = SpellBuilder.Impacts.effectSet("more_rpg_classes:soaked", 4,1);
        soaked.action.status_effect.show_particles = false;
        var soakedModifier = createImpactModifier("#more_rpg_classes:resistant_to_water");
        soakedModifier.execute = TriState.DENY;
        soaked.target_modifiers = List.of(soakedModifier);

        var heal = SpellBuilder.Impacts.heal(0.5F);
        heal.particles = new ParticleBatch[]{
                new ParticleBatch("more_rpg_classes:water_heal",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        10, 0.05F, 0.1F)
        };
        heal.sound = new Sound("spell_engine:generic_healing_impact_1");

        spell.impacts = List.of(damage, soaked, heal);

        return new Entry(id, spell, title, description,null);
    }
    // PASSIVE SPELLS
    public static Entry arcane_precision = add(arcane_precision());
    private static Entry arcane_precision() {
        var id = Identifier.of(MOD_ID, "arcane_precision");
        var title = "Arcane Precision";
        var description = "On dealing a critical hit with an active spell, apply a stack of Arcane Precision to the target for {effect_duration} seconds.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.ARCANE;
        spell.tier = 8;

        var trigger = SpellBuilder.Triggers.activeSpellCrit();
        trigger.impact.impact_type = Spell.Impact.Action.Type.DAMAGE.toString();
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var effect = SpellBuilder.Impacts.effectAdd(LNE_WizardsEffects.ARCANE_PRECISION.id.toString(), 10, 1, 9);
        effect.action.status_effect.refresh_duration = true;
        effect.action.status_effect.show_particles = false;
        effect.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "dragon_breath",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        40, 0.6F, 0.8F
                ),
                new ParticleBatch(
                        "spell_engine:magic_arcane_impact_burst",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.FEET,
                        10, 0.05F, 0.2F
                ).extent(2.0F).color(4284940287L)
        };

        spell.impacts = List.of(effect);
        SpellBuilder.Cost.cooldown(spell, 1);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }

    public static Entry pyromaniac = add(pyromaniac());
    private static Entry pyromaniac() {
        var id = Identifier.of(MOD_ID, "pyromaniac");
        var title = "Pyromaniac";
        var description = "When dealing spell damage to a burning target, reduce fire spell cooldowns and deal bonus damage.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FIRE;
        spell.tier = 8;

        var trigger = SpellBuilder.Triggers.spellHit(0.2F, null);
        trigger.target_conditions = List.of(
                SpellBuilder.TargetConditions.ofPredicate(SpellEntityPredicates.IS_ON_FIRE)
        );
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var cooldownImpact = new Spell.Impact();
        cooldownImpact.action = new Spell.Impact.Action();
        cooldownImpact.action.type = Spell.Impact.Action.Type.COOLDOWN;
        cooldownImpact.action.cooldown = new Spell.Impact.Action.Cooldown();
        cooldownImpact.action.cooldown.actives = new Spell.Impact.Action.Cooldown.Modify();
        cooldownImpact.action.cooldown.actives.school = SpellSchools.FIRE.id.toString();
        cooldownImpact.action.cooldown.actives.duration_multiplier = 0.7F;
        cooldownImpact.action.apply_to_caster = true;
        cooldownImpact.particles = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.sign_hourglass.id().toString(),
                        ParticleBatch.Shape.LINE_VERTICAL, ParticleBatch.Origin.CENTER,
                        1, 0.75F, 0.75F
                ).scale(1.2F).color(4282850047L).followEntity(true)
        };

        var damage = SpellBuilder.Impacts.damage(0.3F);

        spell.impacts = List.of(cooldownImpact, damage);
        SpellBuilder.Cost.cooldown(spell, 10);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }
    public static Entry rimefrost = add(rimefrost());
    private static Entry rimefrost() {
        var id = Identifier.of(MOD_ID, "rimefrost");
        var title = "Rimefrost";
        var description = "When dealing damage with an active frost spell, spawn a freezing cloud at the target.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = SpellSchools.FROST;
        spell.tier = 8;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.3F, SpellSchools.FROST.id.toString());
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 3.0F;
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.volume.sound = new Sound("more_rpg_classes:frost_crackle_long");
        cloud.time_to_live_seconds = 7.0F;
        cloud.impact_tick_interval = 5;
        cloud.client_data.light_level = 6;
        cloud.client_data.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "loot_n_explore:freezing_snowflake",
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        10, 0.1F, 0.12F
                )
        };
        cloud.client_data.particle_spawn_interval = 12;
        cloud.spawn.sound = new Sound(SpellEngineSounds.GENERIC_FROST_CASTING.id().toString());

        spell.deliver.clouds = List.of(cloud);

        var denyModifier = new Spell.Impact.TargetModifier();
        var freezeImmuneCondition = new Spell.TargetCondition();
        freezeImmuneCondition.entity_type = "#minecraft:freeze_immune_entity_types";
        denyModifier.conditions = List.of(freezeImmuneCondition);
        denyModifier.execute = TriState.DENY;

        var freezing = SpellBuilder.Impacts.effectAdd("loot_n_explore:freezing", 2, 1, 4);
        freezing.action.status_effect.refresh_duration = true;
        freezing.action.status_effect.show_particles = false;
        freezing.target_modifiers = List.of(denyModifier);
        freezing.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "loot_n_explore:freezing_snowflake",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 0.25F
                )
        };

        spell.impacts = List.of(freezing);
        SpellBuilder.Cost.cooldown(spell, 4);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }
    public static Entry water_flow = add(water_flow());
    private static Entry water_flow() {
        var id = Identifier.of(MOD_ID, "water_flow");
        var title = "Water Flow";
        var description = "When casting a spell, deal water damage and heal nearby allies in a small area.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = MoreSpellSchools.WATER;
        spell.range = 5.0F;
        spell.tier = 8;

        var trigger = new Spell.Trigger();
        trigger.type = Spell.Trigger.Type.SPELL_CAST;
        trigger.chance = 0.25F;
        trigger.chance_batching = true;
        trigger.equipment_condition = EquipmentSlot.MAINHAND;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.include_caster = true;
        spell.target.area.vertical_range_multiplier = 1.0F;

        spell.release.sound = new Sound(Identifier.ofVanilla("ambient.underwater.exit").toString());
        spell.release.particles_scaled_with_ranged = new ParticleBatch[]{
                new ParticleBatch(
                        SpellEngineParticles.area_effect_293.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.GROUND,
                        1, 0.0F, 0.0F
                ).scale(0.8F).color(2816865791L)
        };

        var vulnerableModifier = SpellBuilder.ImpactModifiers.create("#more_rpg_classes:vulnerable_to_water_spells");
        vulnerableModifier.modifier = new Spell.Impact.Modifier();
        vulnerableModifier.modifier.critical_chance_bonus = 0.3F;

        var resistantModifier = SpellBuilder.ImpactModifiers.create("#more_rpg_classes:resistant_to_water_spells");
        resistantModifier.modifier = new Spell.Impact.Modifier();
        resistantModifier.modifier.power_multiplier = -0.3F;

        var damage = SpellBuilder.Impacts.damage(0.2F);
        damage.target_modifiers = List.of(vulnerableModifier, resistantModifier);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:splash",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        15, 0.05F, 0.2F
                )
        };
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:water_magic_impact1"), 0.4F);

        var heal = SpellBuilder.Impacts.heal(0.15F);
        heal.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:water_heal",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        5, 0.01F, 0.05F
                ),
                new ParticleBatch(
                        "more_rpg_classes:water_heal",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.05F, 0.1F
                ),
                new ParticleBatch(
                        "more_rpg_classes:water_circle",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.FEET,
                        1, 0.2F, 1.0F
                )
        };
        heal.sound = Sound.withVolume(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id(), 1.2F);

        spell.impacts = List.of(damage, heal);
        SpellBuilder.Cost.cooldown(spell, 5);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }
    public static Entry zephyrs_speed = add(zephyrs_speed());
    private static Entry zephyrs_speed() {
        var id = Identifier.of(MOD_ID, "zephyrs_speed");
        var title = "Zephyr's Speed";
        var description = "On dealing damage with an active spell, gain a stack of Zephyr's Speed for {effect_duration} seconds. " +
                "Increasing spell crit chance by {bonus} and movement speed by {bonus2}.";
        var effect = LNE_WizardsEffects.ZEPHYRS_SPEED;

        var spell = SpellBuilder.createSpellPassive();
        spell.school = MoreSpellSchools.AIR;
        spell.tier = 8;
        SpellTooltip.DescriptionMutator mutator = (args) -> {
            var modifier = effect.config().attributes().get(1);
            var modifier2 = effect.config().attributes().get(0);
            var bonus = SpellTooltip.bonus(modifier.value, modifier.operation);
            var bonus2 = SpellTooltip.bonus(modifier2.value, modifier2.operation);
            return args.description()
                    .replace("{bonus}", bonus)
                    .replace("{bonus2}", bonus2);
        };

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.35F, null);
        trigger.target_override = Spell.Trigger.TargetSelector.CASTER;
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        var buff = SpellBuilder.Impacts.effectAdd(effect.id.toString(), 10, 1, 9);
        buff.action.status_effect.refresh_duration = true;
        buff.action.status_effect.show_particles = false;
        buff.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "more_rpg_classes:gust",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        4, 0.5F, 0.8F
                ).extent(1.0F)
        };

        spell.impacts = List.of(buff);
        SpellBuilder.Cost.cooldown(spell, 5);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, mutator);
    }
    public static Entry obsidian_shards = add(obsidian_shards());
    private static Entry obsidian_shards() {
        var id = Identifier.of(MOD_ID, "obsidian_shards");
        var title = "Obsidian Shards";
        var description = "On dealing damage with an active spell, shoot obsidian shards outward in all directions from the target.";

        var spell = SpellBuilder.createSpellPassive();
        spell.school = MoreSpellSchools.EARTH;
        spell.range = 20.0F;
        spell.tier = 8;

        var trigger = SpellBuilder.Triggers.activeSpellHit(0.25F, null);
        spell.passive.triggers = List.of(trigger);

        spell.target.type = Spell.Target.Type.FROM_TRIGGER;

        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.direction_offsets = new Spell.Delivery.ShootProjectile.DirectionOffset[]{
                new Spell.Delivery.ShootProjectile.DirectionOffset(),
                new Spell.Delivery.ShootProjectile.DirectionOffset(-360.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(270.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(-270.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(180.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(-180.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(90.0F, 0),
                new Spell.Delivery.ShootProjectile.DirectionOffset(-90.0F, 0)
        };
        spell.deliver.projectile.direct_towards_target = true;
        spell.deliver.projectile.launch_properties.velocity = 1.3F;
        spell.deliver.projectile.launch_properties.extra_launch_count = 7;
        spell.deliver.projectile.launch_properties.extra_launch_delay = 0;
        spell.deliver.projectile.launch_properties.sound =
                Sound.withVolume(Identifier.of("more_rpg_classes:earth_magic_cast1"), 0.6F);

        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 0.0F;
        projectile.perks = new Spell.ProjectileData.Perks();
        projectile.perks.pierce = 999;
        projectile.hitbox = new Spell.ProjectileData.HitBox();
        projectile.hitbox.width = 0.5F;
        projectile.hitbox.height = 0.5F;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.model = new Spell.ProjectileModel();
        projectile.client_data.model.model_id = "lne_wizards:spell_projectile/obsidian_shards";
        projectile.client_data.model.scale = 0.5F;
        projectile.client_data.model.rotate_degrees_per_tick = 0.0F;
        spell.deliver.projectile.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(0.25F, 0.5F);
        damage.particles = new ParticleBatch[]{
                new ParticleBatch(
                        "campfire_cosy_smoke",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        3, 0.005F, 0.008F
                )
        };
        damage.sound = Sound.withVolume(Identifier.ofVanilla("block.pointed_dripstone.break"), 1.5F);

        spell.impacts = List.of(damage);
        SpellBuilder.Cost.cooldown(spell, 5);
        spell.cost.cooldown.hosting_item = false;

        return new Entry(id, spell, title, description, null);
    }
}
