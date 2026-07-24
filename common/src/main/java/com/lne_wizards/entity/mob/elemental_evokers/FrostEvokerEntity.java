package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

public class FrostEvokerEntity extends ElementalEvokerEntity {

    public FrostEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "#lne_wizards:mob/frost_evoker/primary";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/frost_evoker/secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return Identifier.of("wizards", "wand_frost");
    }

    public static DefaultAttributeContainer.Builder createFrostEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(SpellSchools.FROST.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
