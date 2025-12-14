package net.eman3600.hdemise.init;

import net.eman3600.hdemise.networking.c2s.FocusPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModMessages {

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(FocusPayload.TYPE, FocusPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FocusPayload.TYPE, FocusPayload::receive);
    }
}
