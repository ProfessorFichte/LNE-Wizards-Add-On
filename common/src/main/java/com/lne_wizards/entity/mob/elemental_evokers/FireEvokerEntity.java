package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;

public class FireEvokerEntity extends ElementalEvokerEntity {

    public FireEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "wizards:fireball";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/evoker/fire_secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return Identifier.of("wizards", "wand_fire");
    }

    public static DefaultAttributeContainer.Builder createFireEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(SpellSchools.FIRE.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
