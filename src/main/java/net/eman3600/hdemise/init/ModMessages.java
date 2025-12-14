package net.eman3600.hdemise.init;

import net.eman3600.hdemise.networking.c2s.FocusPayload;
import net.eman3600.hdemise.networking.s2c.FocusSoundPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModMessages {

    public static void registerPackets() {
        PayloadTypeRegistry.playC2S().register(FocusPayload.TYPE, FocusPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(FocusSoundPayload.TYPE, FocusSoundPayload.CODEC);
    }

    public static void registerC2SReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(FocusPayload.TYPE, FocusPayload::receive);
    }

    @Environment(EnvType.CLIENT)
    public static void registerS2CReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(FocusSoundPayload.TYPE, FocusSoundPayload::receive);
    }
}
