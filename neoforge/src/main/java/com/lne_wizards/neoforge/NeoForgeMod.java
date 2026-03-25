package com.lne_wizards.neoforge;

import com.lne_wizards.LNE_Wizards_Mod;
import net.elemental_wizards_rpg.entity.ModEntitiesRegistry;
import net.minecraft.registry.RegistryKeys;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(LNE_Wizards_Mod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        LNE_Wizards_Mod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
    }
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.ITEM, reg -> {
            LNE_Wizards_Mod.registerItems();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            LNE_Wizards_Mod.registerEffects();
        });
        event.register(RegistryKeys.ENTITY_TYPE, reg -> {
            LNE_Wizards_Mod.registerEntities();
            LNE_Wizards_Mod.registerEntityAttributes();
        });
    }
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        ModEntitiesRegistry.registerEntityAttributes((entityType, builder) -> event.put(entityType, builder.build()));
    }
}
