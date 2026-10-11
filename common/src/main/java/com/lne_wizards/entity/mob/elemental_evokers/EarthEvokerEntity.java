package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import com.mrpg_lib.entity.config.EntityAttributeConfig;

public class EarthEvokerEntity extends ElementalEvokerEntity {
    public static final Identifier WAND = Identifier.of("elemental_wizards_rpg", "wand_terra");

    public EarthEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "#lne_wizards:mob/earth_evoker/primary";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/earth_evoker/secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return WAND;
    }

    @Override
    protected EntityAttributeConfig.Entry spellPowerAttributes() {
        return new EntityAttributeConfig.Entry()
                .set(MoreSpellSchools.EARTH.attributeEntry.getIdAsString(), spellPowerBase(LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power), 1.0, 1.0);
    }

    public static DefaultAttributeContainer.Builder createEarthEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(MoreSpellSchools.EARTH.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
