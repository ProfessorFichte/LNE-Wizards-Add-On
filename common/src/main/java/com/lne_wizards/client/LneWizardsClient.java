package com.lne_wizards.client;

import net.minecraft.util.Identifier;
import net.spell_engine.api.render.CustomModels;

import java.util.List;

import static com.lne_wizards.LNE_Wizards_Mod.MOD_ID;

public class LneWizardsClient {

    public static void init() {
        CustomModels.registerModelIds(List.of(
                Identifier.of(MOD_ID, "projectile/starfall"),
                Identifier.of(MOD_ID, "projectile/obsidian_shards")
        ));
    }
}
