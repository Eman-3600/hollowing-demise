package net.eman3600.hdemise.init.event;

import net.eman3600.hdemise.networking.c2s.FocusPayload;
import net.eman3600.hdemise.networking.c2s.GhostPayload;
import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModMessages {

    public static void registerPackets() {
        LOGGER.info("Registering Packets for " + MODID);

        PayloadTypeRegistry.playC2S().register(FocusPayload.TYPE, FocusPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(GhostPayload.TYPE, GhostPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SoulEventPayload.TYPE, SoulEventPayload.CODEC);
    }

    public static void registerC2SReceivers() {
        LOGGER.info("Registering C2S Packet Receivers for " + MODID);

        ServerPlayNetworking.registerGlobalReceiver(FocusPayload.TYPE, FocusPayload::receive);
        ServerPlayNetworking.registerGlobalReceiver(GhostPayload.TYPE, GhostPayload::receive);
    }

    @Environment(EnvType.CLIENT)
    public static void registerS2CReceivers() {
        LOGGER.info("Registering S2C Packet Receivers for " + MODID);

        ClientPlayNetworking.registerGlobalReceiver(SoulEventPayload.TYPE, SoulEventPayload::receive);
    }
}
