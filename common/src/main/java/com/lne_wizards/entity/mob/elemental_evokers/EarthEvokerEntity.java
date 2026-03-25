package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.more_rpg_classes.custom.MoreSpellSchools;

public class EarthEvokerEntity extends ElementalEvokerEntity {

    public EarthEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "elemental_wizards_rpg:terra_stone_spear";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/evoker/earth_secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return Identifier.of("elemental_wizards_rpg", "wand_terra");
    }

    public static DefaultAttributeContainer.Builder createEarthEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(MoreSpellSchools.EARTH.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
