package com.lne_wizards.datagen;

import com.lne_wizards.LNE_Wizards_Mod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    // 1.20.1's `EntityTypeTags` has no ILLAGER constant (the vanilla `minecraft:illager` tag is 1.21
    // content). The key is built by hand so the generated file is unchanged from the 1.21.1 branch.
    private static final TagKey<EntityType<?>> ILLAGER =
            TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("minecraft", "illager"));

    public ModEntityTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // air_evoker/earth_evoker/water_evoker are only registered when elemental_wizards_rpg is
        // loaded, so they're added by id rather than by EntityType reference.
        getOrCreateTagBuilder(ILLAGER)
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "arcane_evoker"))
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "fire_evoker"))
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "frost_evoker"))
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "air_evoker"))
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "earth_evoker"))
                .addOptional(new Identifier(LNE_Wizards_Mod.MOD_ID, "water_evoker"));
    }
}
