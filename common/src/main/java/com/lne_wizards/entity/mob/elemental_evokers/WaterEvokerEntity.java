package com.lne_wizards.entity.mob.elemental_evokers;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.more_rpg_classes.custom.MoreSpellSchools;

public class WaterEvokerEntity extends ElementalEvokerEntity {

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
        return Identifier.of("elemental_wizards_rpg", "wand_aqua");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isSubmergedInWater()) {
            this.setAir(this.getMaxAir());
        }
    }

    public static DefaultAttributeContainer.Builder createWaterEvokerAttributes() {
        return createElementalEvokerAttributes()
                .add(MoreSpellSchools.WATER.attributeEntry, LNE_Wizards_Mod.tweaksConfig.value.elemental_evoker_default_spell_power);
    }
}
