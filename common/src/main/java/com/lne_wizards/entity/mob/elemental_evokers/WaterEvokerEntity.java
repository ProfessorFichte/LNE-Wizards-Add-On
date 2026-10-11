package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import com.mrpg_lib.compat.spell_power.MoreSpellSchools;
import com.mrpg_lib.entity.config.EntityAttributeConfig;

public class WaterEvokerEntity extends ElementalEvokerEntity {
    public static final Identifier WAND = Identifier.of("elemental_wizards_rpg", "wand_aqua");

    public WaterEvokerEntity(EntityType<? extends EvokerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public String getPrimarySpell() {
        return "#lne_wizards:mob/water_evoker/primary";
    }

    @Override
    public String getSecondarySpells() {
        return "#lne_wizards:mob/water_evoker/secondary";
    }

    @Override
    public Identifier getWandItemId() {
        return WAND;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isSubmergedInWater()) {
            this.setAir(this.getMaxAir());
        }
    }

    @Override
    protected EntityAttributeConfig.Entry spellPowerAttributes() {
        return new EntityAttributeConfig.Entry()
                .set(MoreSpellSchools.WATER.attributeEntry.getIdAsString(), spellPowerBase(LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power), 1.0, 1.0);
    }

    public static DefaultAttributeContainer.Builder createWaterEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(MoreSpellSchools.WATER.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
