package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

public class ArcaneEvokerEntity extends ElementalEvokerEntity {

    public ArcaneEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "wizards:arcane_bolt";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/evoker/arcane_secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return Identifier.of("wizards", "wand_arcane");
    }

    public static DefaultAttributeContainer.Builder createArcaneEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(SpellSchools.ARCANE.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
