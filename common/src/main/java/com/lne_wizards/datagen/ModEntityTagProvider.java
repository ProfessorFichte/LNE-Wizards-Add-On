package com.lne_wizards.datagen;

import com.lne_wizards.LNE_Wizards_Mod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // air_evoker/earth_evoker/water_evoker are only registered when elemental_wizards_rpg is
        // loaded, so they're added by id rather than by EntityType reference.
        getOrCreateTagBuilder(EntityTypeTags.ILLAGER)
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "arcane_evoker"))
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "fire_evoker"))
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "frost_evoker"))
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "air_evoker"))
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "earth_evoker"))
                .addOptional(Identifier.of(LNE_Wizards_Mod.MOD_ID, "water_evoker"));
    }
}
