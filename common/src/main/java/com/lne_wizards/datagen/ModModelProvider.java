package com.lne_wizards.datagen;

import com.lne_wizards.entity.ModSpawnEggs;
import com.lne_wizards.item.WeaponRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    private static final Model SPAWN_EGG_MODEL = new Model(
            Optional.of(Identifier.of("minecraft", "item/template_spawn_egg")),
            Optional.empty()
    );

    private static final Model MEDIUM_STAFF_MODEL = new Model(
            Optional.of(Identifier.of("wizards", "item/medium_staff")),
            Optional.empty(),
            TextureKey.LAYER0
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        WeaponRegister.entries.forEach(entry -> {
            if (entry.item() != null) {
                itemModelGenerator.register(entry.item(), MEDIUM_STAFF_MODEL);
            }
        });

        itemModelGenerator.register(ModSpawnEggs.AIR_EVOKER_SPAWN_EGG,    SPAWN_EGG_MODEL);
        itemModelGenerator.register(ModSpawnEggs.ARCANE_EVOKER_SPAWN_EGG, SPAWN_EGG_MODEL);
        itemModelGenerator.register(ModSpawnEggs.EARTH_EVOKER_SPAWN_EGG,  SPAWN_EGG_MODEL);
        itemModelGenerator.register(ModSpawnEggs.FIRE_EVOKER_SPAWN_EGG,   SPAWN_EGG_MODEL);
        itemModelGenerator.register(ModSpawnEggs.FROST_EVOKER_SPAWN_EGG,  SPAWN_EGG_MODEL);
        itemModelGenerator.register(ModSpawnEggs.WATER_EVOKER_SPAWN_EGG,  SPAWN_EGG_MODEL);
    }
}
