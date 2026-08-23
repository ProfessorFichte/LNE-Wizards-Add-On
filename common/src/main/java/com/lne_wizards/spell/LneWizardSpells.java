package com.lne_wizards.spell;

import com.lne_wizards.effect.LNE_WizardsEffects;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.more_rpg_classes.client.particle.MoreParticles;
import net.more_rpg_classes.custom.MoreSpellSchools;
import net.more_rpg_classes.sounds.MRPGLibSounds;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.Easing;
import net.spell_engine.api.spell.fx.Fx;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ModelEffectBuilder;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.spell.registry.SpellRegistry;
import net.spell_engine.api.spell.tooltip.TooltipTokens;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.internals.impact.SpellEstimation;
import net.spell_engine.internals.target.SpellTarget;
import net.spell_power.api.SpellSchools;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class LneWizardSpells {
    public record Entry(Identifier id, Spell spell, String title, String description) {
    }

    public static final List<Entry> entries = new ArrayList<>();

    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static Identifier attributeId(RegistryEntry<EntityAttribute> attribute) {
        return Identifier.of(attribute.getIdAsString());
    }

    private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    private static List<Spell.Impact> explosiveBubbleImpacts() {
        var damage = SpellBuilder.Impacts.damage(0.85F, 0.5F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.BUBBLE_POP)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(10.0F).speed(0.05F, 0.2F)),
                ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10.0F).speed(0.001F, 0.1F)));
        damage.sound = Sound.withVolume(Identifier.of(MRPGLibSounds.WATER_BUBBLE_EXPLODE.id().toString()), 2.0F);

        var soaked = SpellBuilder.Impacts.effectSet("more_rpg_classes:soaked", 4, 1);
        soaked.action.status_effect.show_particles = false;
        var soakedModifier = createImpactModifier("#more_rpg_classes:resistant_to_water");
        soakedModifier.execute = TriState.DENY;
        soaked.target_modifiers = List.of(soakedModifier);

        var heal = SpellBuilder.Impacts.heal(0.5F);
        heal.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(MoreParticles.WATER_HEAL)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.05F, 0.1F)));
        heal.sound = new Sound("spell_engine:generic_healing_impact_1");

        return List.of(damage, soaked, heal);
    }

    private static Spell.EntityPlacement floatingPlacement(float distance, float yawOffset, int delayTicks) {
        var placement = SpellBuilder.Deliver.placementByLook(distance, yawOffset, delayTicks);
        placement.force_onto_ground = false;
        return placement;
    }

    private static void addOscillation(ModelEffect effect, String operation, float x, float y, float z,
                                       int start, int periodTicks, int cycles, Easing easing) {
        int half = periodTicks / 2;
        for (int i = 0; i < cycles * 2; i++) {
            var segment = new ModelEffect.Animation();
            segment.operation = operation;
            var sign = (i % 2 == 0) ? 1F : -1F;
            segment.x = x * sign;
            segment.y = y * sign;
            segment.z = z * sign;
            segment.start = start + i * half;
            segment.end = start + (i + 1) * half;
            segment.easing = easing;
            effect.animations.add(segment);
        }
    }

    // ACTIVE SPELLS
    public static Entry fire_flamerush = add(fire_flamerush());
    private static Entry fire_flamerush() {
        var id = Identifier.of(MOD_ID, "fire_flamerush");
        var effect = LNE_WizardsEffects.FLAME_RUSH;
        var title = "Flamerush";
        var description = "Rushes forward and leaves flame clouds on the trail that deals {flamecloud_damage} damage. "
                + "Also increasing movement speed by "
                + TooltipTokens.effect(effect.id, 0, attributeId(EntityAttributes.GENERIC_MOVEMENT_SPEED))
                + " and fire spell power by "
                + TooltipTokens.effect(effect.id, 0, SpellSchools.FIRE.id)
                + " for {effect_duration}.";
        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.FIRE;
        spell.tier = 5;
        spell.range = 10F;

        spell.target.type = Spell.Target.Type.CASTER;

        spell.release.animation = PlayerAnimation.of("spell_engine:dual_handed_ground_release");
        spell.release.sound = Sound.withVolume(Identifier.of("spell_engine:generic_fire_release"), 1.0F);
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_a)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5).speed(0.02F, 0.3F)
                                .extent(1F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_medium_b)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5).speed(0.02F, 0.35F)
                                .extent(3F)));

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4, 0);

        var dash = SpellBuilder.Impacts.velocity(
                Spell.Impact.Action.Velocity.Frame.ORIGIN, new Vector3f(0, 0.3F, 2.0F));
        dash.action.velocity.reset_velocity = true;
        dash.action.apply_to_caster = true;

        var customCloud = new Spell.Impact();
        customCloud.action = new Spell.Impact.Action();
        customCloud.action.custom = new Spell.Impact.Action.Custom();
        customCloud.action.type = Spell.Impact.Action.Type.CUSTOM;
        customCloud.action.custom.intent = SpellTarget.Intent.HELPFUL;
        customCloud.action.custom.handler = "lne_wizards:flamerush_cloud";

        spell.impacts = List.of(dash, customCloud, buff);
        SpellBuilder.Cost.cooldown(spell, 30);
        SpellBuilder.Cost.item(spell, "runes:fire_stone", 1);

        return new Entry(id, spell, title, description);
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
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.CONE)
                                .anchor(ParticleGroup.Anchor.LAUNCH_POINT)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(4).speed(1.0F, 3.0F)
                                .angle(3F)));

        spell.target.type = Spell.Target.Type.BEAM;
        spell.target.beam = new Spell.Target.Beam();
        spell.target.beam.texture_id = "lne_wizards:textures/entity/frost_ray.png";
        spell.target.beam.width = 0.02F;
        spell.target.beam.flow = 200;
        // NOTE: `lne_wizards:frost_ray` is NOT a registered particle type — this mod registers no particles
        // at all, and the id is the *spell's* id. It resolved to null and rendered nothing in 1.9 as well.
        // Ported verbatim rather than substituted, because there is no way to tell what was intended.
        spell.target.beam.block_hit = Fx.Visuals.of(
                ParticleGroupBuilder.of("lne_wizards:frost_ray")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(13).speed(0.1F, 0.3F)));

        var frozenHurts = SpellBuilder.ImpactModifiers.create("#minecraft:freeze_hurts_extra_types");
        frozenHurts.modifier = new Spell.Impact.Modifier();
        frozenHurts.modifier.power_multiplier = 0.3F;

        var frozenImmune = SpellBuilder.ImpactModifiers.create("#minecraft:freeze_immune_entity_types");
        frozenImmune.modifier = new Spell.Impact.Modifier();
        frozenImmune.modifier.power_multiplier = -0.3F;

        var damage = SpellBuilder.Impacts.damage(1.0F);
        damage.target_modifiers = List.of(frozenHurts, frozenImmune);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_frost, ParticleGroup.Motion.BURST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(40).speed(0.2F, 0.7F)),
                ParticleGroupBuilder.of(SpellEngineParticles.snowflake)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(20).speed(0.1F, 0.4F)));
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:frost_crackle"), 0.4F);

        var frosted = SpellBuilder.Impacts.effectAdd("more_rpg_classes:frosted", 5, 1, 5);
        frosted.action.status_effect.show_particles = false;

        spell.impacts = List.of(damage, frosted);

        spell.cost.exhaust = 0.5F;
        SpellBuilder.Cost.item(spell, "runes:frost_stone");
        SpellBuilder.Cost.cooldown(spell, 22);
        spell.cost.cooldown.proportional = true;

        return new Entry(id, spell, title, description);
    }

    private static final long STARFALL_COLOR = 4284940287L;

    private static ParticleGroupBuilder starfallArcaneBurst() {
        return ParticleGroupBuilder.magic(SpellEngineParticles.magic_arcane, ParticleGroup.Motion.BURST)
                .color(STARFALL_COLOR);
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
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND)
                        .color(STARFALL_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(1).speed(0.05F, 0.1F)));
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
        meteorProjectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.magic(SpellEngineParticles.magic_spell, ParticleGroup.Motion.ASCEND)
                        .color(STARFALL_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(10).speed(0.0F, 0.1F)),
                ParticleGroupBuilder.of("dragon_breath")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(5).speed(0.1F, 0.3F)));
        meteorProjectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("lne_wizards:spell_projectile/starfall");
        spell.deliver.meteor.projectile = meteorProjectile;

        var damage = SpellBuilder.Impacts.damage(1.5F, 2.0F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_293)
                        .scale(0.8F).color(STARFALL_COLOR)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .anchor(ParticleGroup.Anchor.GROUND)
                                .count(1).speed(0.0F, 0.0F)),
                starfallArcaneBurst()
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(30).speed(0.5F, 1.5F)
                                .extent(2.0F)),
                starfallArcaneBurst()
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(40).speed(0.2F, 0.7F)
                                .extent(2.0F)),
                starfallArcaneBurst()
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.1F, 0.4F)
                                .extent(2.0F)));
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

        return new Entry(id, spell, title, description);
    }
    public static final Entry aqua_explosive_bubbles = add(aqua_explosive_bubbles());
    private static Entry aqua_explosive_bubbles() {
        var id = Identifier.of(MOD_ID, "aqua_explosive_bubbles");
        var title = "Explosive Bubbles";
        var description = "Spawns explosive Bubbles behind the caster that deal {damage} damage or heal allies by {heal} when they explode on contact.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = MoreSpellSchools.WATER;
        spell.range = 10;
        spell.tier = 5;

        SpellBuilder.Casting.channel(spell, 7.5F, 15);
        spell.active.cast.animation = PlayerAnimation.of("more_rpg_classes:floating_spawn_channel");
        spell.active.cast.sound = new Sound (Identifier.of("more_rpg_classes:water_bubbles"));
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(1.0F).speed(0.001F, 0.1F)));
        spell.active.cast.movement_speed = 1.0F;

        spell.release = new Spell.Release();

        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var bubbleModelId = Identifier.of("elemental_wizards_rpg", "spell_projectile/big_bubble").toString();
        var bubbleColor = Color.from(0xa7ffed).toRGBA();

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 2.0F;
        cloud.spawn_ticks = 20;
        cloud.despawn_ticks = 5;
        cloud.time_to_live_seconds = 8F;
        cloud.impact_tick_interval = 1;
        cloud.impact_cap = 1;

        var activeTicks = Math.round(cloud.time_to_live_seconds * 20F);
        var totalTicks = cloud.spawn_ticks + activeTicks + cloud.despawn_ticks;

        var bubbleHoverHeight = 0.5F;
        var bubbleFx = ModelEffectBuilder.create(bubbleModelId).light(LightEmission.GLOW_TRANSLUCENT)
                .scale(2.5F)
                .duration(totalTicks)
                .initialTranslate(0, bubbleHoverHeight, 0)
                .scaleIn(0, cloud.spawn_ticks, Easing.EASE_OUT_BACK)
                .build();
        addOscillation(bubbleFx, "translate", 0, 0.12F, 0,
                cloud.spawn_ticks, 40, activeTicks / 40, Easing.EASE_IN_OUT_SINE);
        addOscillation(bubbleFx, "scale", 0.035F, 0.035F, 0.035F,
                cloud.spawn_ticks, 32, activeTicks / 32, Easing.EASE_IN_OUT_SINE);
        cloud.client_data.model_fx = List.of(bubbleFx);
        cloud.client_data.light_level = 8;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(1F).chance(0.4F).speed(0.01F, 0.15F)));
        cloud.client_data.particle_spawn_interval = 15;

        var popFx = ModelEffectBuilder.create(bubbleModelId).light(LightEmission.GLOW_TRANSLUCENT)
                .scale(2.5F)
                .duration(cloud.despawn_ticks)
                .initialTranslate(0, bubbleHoverHeight, 0)
                .scaleOut(0, cloud.despawn_ticks, Easing.EASE_IN_BACK)
                .build();
        cloud.despawn.visuals = Fx.Visuals.ofParticles(List.of(
                        ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                                .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                        .count(10.0F).speed(0.01F, 0.2F))))
                .models(popFx);
        cloud.despawn.sound = new Sound(Identifier.ofVanilla("block.bubble_column.bubble_pop").toString());

        cloud.impact = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.area_effect_293)
                        .scale(1.5F).color(bubbleColor)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .anchor(ParticleGroup.Anchor.GROUND)
                                .count(1).speed(0F, 0F)),
                ParticleGroupBuilder.of(MoreParticles.BUBBLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(50.0F).speed(0.3F, 1.0F)));

        cloud.placement = floatingPlacement(2.0F, -160, 0);

        spell.deliver.clouds = List.of(cloud);
        spell.impacts = explosiveBubbleImpacts();
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 2.0F;

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 32);
        spell.cost.cooldown.haste_affected = true;
        SpellBuilder.Cost.item(spell, "more_rpg_classes:aqua_stone", 1);

        return new Entry(id, spell, title, description);
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
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.of(MoreParticles.SMALL_GUST)
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(20).speed(0.2F, 0.5F)),
                ParticleGroupBuilder.of(MoreParticles.SMALL_GUST)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5.0F).speed(0.05F, 0.2F)));

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.target.area.angle_degrees = 360.0F;

        spell.release = new Spell.Release();
        spell.release.animation = PlayerAnimation.of("more_rpg_classes:two_handed_ground_release");
        spell.release.sound = new Sound("more_rpg_classes:air_explosion");
        spell.release.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(50.0F).speed(0.6F, 2.0F)
                                .extent(2F).invert(true)),
                ParticleGroupBuilder.of(MoreParticles.WATER_MIST)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(100.0F).speed(0.6F, 2.0F)
                                .extent(2F).invert(true)),
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(50.0F).speed(1.0F, 2.4F)
                                .extent(4F)),
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_medium)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(50.0F).speed(1.4F, 3.0F)
                                .extent(6F)));

        var damage = SpellBuilder.Impacts.damage(0.8F, 10.0F);
        damage.sound = Sound.withVolume(Identifier.of("more_rpg_classes:air_magic_impact2"), 0.4F);

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.exhaust(spell, 0.4F);
        SpellBuilder.Cost.cooldown(spell, 25);
        spell.cost.cooldown.haste_affected = true;
        SpellBuilder.Cost.item(spell, "more_rpg_classes:storm_stone", 1);

        return new Entry(id, spell, title, description);
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
        spell.active.cast.particles = List.of(
                ParticleGroupBuilder.of(MoreParticles.STONE_PARTICLE)
                        .batch(b -> b.shape(ParticleGroup.Shape.PIPE)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(3.0F).speed(0.01F, 0.05F)));

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
        projectile.client_data.travel_particles = List.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(10).speed(0.1F, 0.5F)),
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_large)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .alignment(ParticleGroup.Alignment.LOOK)
                                .count(10).speed(0.1F, 0.0F)));
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("elemental_wizards_rpg:spell_projectile/meteor", 1.7F);
        spell.deliver.meteor.projectile = projectile;

        var damage = SpellBuilder.Impacts.damage(1.0F, 3.5F);
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.smoke_large)
                        .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE)
                                .count(5).speed(0.5F, 3F)));
        damage.sound = new Sound("more_rpg_classes:earth_magic_impact1");
        spell.impacts = List.of(damage);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 10;
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(90).speed(1.0F, 3.0F)));
        spell.area_impact.sound = new Sound("more_rpg_classes:earth_magic_impact3");

        SpellBuilder.Cost.exhaust(spell, 0.3F);
        SpellBuilder.Cost.item(spell, "more_rpg_classes:terra_stone", 1);
        SpellBuilder.Cost.cooldown(spell, 32);

        return new Entry(id, spell, title, description);
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
        cloud.spawn.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(5).speed(0.05F, 0.1F)));
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = List.of(
                ParticleGroupBuilder.of(SpellEngineParticles.flame_ground)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(2).speed(0F, 0F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(3).speed(0.02F, 0.1F)),
                ParticleGroupBuilder.of(SpellEngineParticles.flame_spark)
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(4).speed(0.05F, 0.1F)),
                ParticleGroupBuilder.of("campfire_cosy_smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.PILLAR)
                                .verticalOrigin(ParticleGroupBuilder.Batches.FEET)
                                .count(1F).chance(0.1F).speed(0.05F, 0.1F)));

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
        damage.visuals = Fx.Visuals.of(
                ParticleGroupBuilder.of("smoke")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(15).speed(0.01F, 0.1F)),
                ParticleGroupBuilder.of("flame")
                        .batch(b -> b.shape(ParticleGroup.Shape.SPHERE)
                                .count(10).speed(0.01F, 0.1F)));
        damage.sound = new Sound("wizards:fire_scorch_impact");
        var fire = SpellBuilder.Impacts.fire(2);
        spell.impacts = List.of(damage, fire);
        SpellBuilder.Cost.cooldown(spell, 20);

        return new Entry(id, spell, name, description);
    }

    public static void registerTooltipTokens() {
        TooltipTokens.registerCustom(fire_flamerush.id(), args -> {
            var description = args.description();
            var world = args.player().getWorld();
            if (world == null) return description;
            var cloudSpell = SpellRegistry.from(world).getEntry(Identifier.of(MOD_ID, "helper/flamerush_cloud"));
            if (cloudSpell.isEmpty()) return description;
            var estimated = SpellEstimation.estimate(cloudSpell.get().value(), args.player(), ItemStack.EMPTY);
            if (estimated.damage().isEmpty()) return description;
            var damage = estimated.damage().get(0);
            return description.replace("{flamecloud_damage}", formattedRange(damage.min(), damage.max()));
        });
    }

    private static String formattedRange(double min, double max) {
        if (min == max) {
            return TooltipTokens.formattedNumber((float) min);
        }
        return TooltipTokens.formattedNumber((float) min) + " - " + TooltipTokens.formattedNumber((float) max);
    }
}
