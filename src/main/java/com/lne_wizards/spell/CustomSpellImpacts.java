package com.lne_wizards.spell;

import com.lne_wizards.spell.spell_impacts.FlameRushCloudImpact;
import net.minecraft.util.Identifier;
import net.spell_engine.api.spell.event.SpellHandlers;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class CustomSpellImpacts {
    public static void registerCustomImpacts(){
        SpellHandlers.registerCustomImpact(
                Identifier.of(MOD_ID, "flamerush_cloud"),
                new FlameRushCloudImpact()
        );
    }
}
