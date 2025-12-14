package net.eman3600.hdemise;

import net.eman3600.hdemise.event.KeyInputHandler;
import net.eman3600.hdemise.init.ModMessages;
import net.fabricmc.api.ClientModInitializer;

public class HDemiseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerBindings();
        KeyInputHandler.registerKeyInputs();

        ModMessages.registerS2CReceivers();
    }
}
