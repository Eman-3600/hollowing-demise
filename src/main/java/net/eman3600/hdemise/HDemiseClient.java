package net.eman3600.hdemise;

import net.eman3600.hdemise.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class HDemiseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerBindings();
        KeyInputHandler.registerKeyInputs();


    }
}
