package com.lne_wizards.spell;

import com.lne_wizards.effect.Effects;
import net.minecraft.util.Identifier;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;
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

    public static Spell createUnlearnableSpellActive() {
        Spell spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();
        return spell;
    }
    public static Entry fire_flamerush = add(fire_flamerush());
    private static Entry fire_flamerush() {
        var id = Identifier.of(MOD_ID, "fire_flamerush");
        var effect = Effects.FLAME_RUSH;
        var title = "";
        var description = "";
        var spell = createUnlearnableSpellActive();
        spell.school = SpellSchools.FIRE;
        spell.tier = 4;
        spell.range = 10F;

        spell.target.type = Spell.Target.Type.CASTER;

        spell.release.animation = "spell_engine:dual_handed_ground_release";
        spell.release.sound = Sound.withVolume(Identifier.of("spell_engine:generic_fire_release"), 1.0F);
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(SpellEngineParticles.flame_medium_a.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.02F, 0.3F).extent(1),
                new ParticleBatch(SpellEngineParticles.flame_medium_b.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.02F, 0.35F).extent(3)
        };

        var buff = SpellBuilder.Impacts.effectSet(effect.id.toString(), 4,0);

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
        SpellBuilder.Cost.item(spell,"runes:fire_stone",1);
        return new Entry(id, spell, title, description, null);
    }
    public static Entry flamerush_cloud = add(flamerush_cloud());
    private static Entry flamerush_cloud() {
        var id = Identifier.of(MOD_ID, "functional/flamerush_cloud");
        var name = "";
        var description = ".";

        var spell = SpellBuilder.createSpellActive();
        spell.range = 0;
        spell.tier = 4;
        spell.school = SpellSchools.FIRE;

        spell.deliver.type = Spell.Delivery.Type.CLOUD;

        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 1.0F;
        cloud.volume.area.vertical_range_multiplier = 0.5F;
        cloud.volume.sound = new Sound("wizards:fire_scorch_impact");
        cloud.delay_ticks = 0;
        cloud.impact_tick_interval = 15;
        cloud.time_to_live_seconds = 5;
        cloud.spawn = new Spell.Delivery.Cloud.Spawn();
        cloud.spawn.sound = new Sound("wizards:fire_wall_ignite");
        cloud.spawn.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.flame.id().toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        5, 0.05F, 0.1F)
        };
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[] {
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

        var damage = SpellBuilder.Impacts.damage(0.2F, 0.0F);
        damage.particles = new ParticleBatch[] {
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
}

