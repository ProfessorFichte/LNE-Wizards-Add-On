package com.lne_wizards.fabric.client;

import com.lne_wizards.client.LneWizardsClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LneWizardsClient.init();
    }
}
