package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.spell_power.api.SpellSchools;
import com.mrpg_lib.entity.config.EntityAttributeConfig;

public class FireEvokerEntity extends ElementalEvokerEntity {
    public static final Identifier WAND = Identifier.of("wizards", "wand_fire");

    public FireEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "#lne_wizards:mob/fire_evoker/primary";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/fire_evoker/secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return WAND;
    }

    @Override
    protected EntityAttributeConfig.Entry spellPowerAttributes() {
        return new EntityAttributeConfig.Entry()
                .set(SpellSchools.FIRE.attributeEntry.getIdAsString(), spellPowerBase(LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power))
                .set(EntityAttributes.GENERIC_ATTACK_KNOCKBACK.getIdAsString(), 0.0);
    }

    public static DefaultAttributeContainer.Builder createFireEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(SpellSchools.FIRE.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.0);
    }
}
