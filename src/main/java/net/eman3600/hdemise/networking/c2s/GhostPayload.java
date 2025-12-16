package net.eman3600.hdemise.networking.c2s;

import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public record GhostPayload() implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "ghost");
    public static final Id<GhostPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, GhostPayload> CODEC = new PacketCodec<>() {
        @Override
        public GhostPayload decode(ByteBuf buf) {
            return new GhostPayload();
        }

        @Override
        public void encode(ByteBuf buf, GhostPayload value) {

        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(GhostPayload payload, ServerPlayNetworking.Context context) {
        SoulComponent sc = SoulComponent.of(context.player());

        if (sc != null) {
            if (sc.isGhost()) {
                sc.setGhost(false);
            } else if (!sc.isVanishing() && sc.canVanish()) {
                sc.beginVanishing();
            }
        }
    }
}
