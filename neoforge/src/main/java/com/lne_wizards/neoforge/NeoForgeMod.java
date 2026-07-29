package com.lne_wizards.neoforge;

import com.lne_wizards.LNE_Wizards_Mod;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(LNE_Wizards_Mod.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod(IEventBus modBus) {
        LNE_Wizards_Mod.init();
        modBus.addListener(RegisterEvent.class, NeoForgeMod::register);
        modBus.addListener(EntityAttributeCreationEvent.class, NeoForgeMod::registerAttributes);
        modBus.addListener(AddPackFindersEvent.class, NeoForgeMod::addPackFinders);
    }
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != ResourceType.SERVER_DATA) {
            return;
        }
        if (!ModList.get().isLoaded(LNE_Wizards_Mod.ELEMENTAL_WIZARDS_MOD_ID)) {
            return;
        }
        event.addPackFinders(
                Identifier.of(LNE_Wizards_Mod.MOD_ID, LNE_Wizards_Mod.ELEMENTAL_WIZARDS_COMPAT_PACK_PATH),
                ResourceType.SERVER_DATA,
                Text.literal("LNE Wizards - Elemental Wizards Compat"),
                ResourcePackSource.BUILTIN,
                true,
                ResourcePackProfile.InsertionPosition.TOP);
    }
    public static void register(RegisterEvent event) {
        event.register(RegistryKeys.BLOCK, reg -> {
            LNE_Wizards_Mod.registerBlocks();
        });
        event.register(RegistryKeys.ITEM, reg -> {
            LNE_Wizards_Mod.registerItems();
            LNE_Wizards_Mod.registerSpawnEggs();
        });
        event.register(RegistryKeys.STATUS_EFFECT, reg -> {
            LNE_Wizards_Mod.registerEffects();
        });
        event.register(RegistryKeys.ENTITY_TYPE, reg -> {
            LNE_Wizards_Mod.registerEntities();
        });
    }
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        LNE_Wizards_Mod.registerEntityAttributes();
    }
}
