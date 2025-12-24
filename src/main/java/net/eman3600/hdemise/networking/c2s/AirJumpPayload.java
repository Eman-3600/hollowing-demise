package net.eman3600.hdemise.networking.c2s;

import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public record AirJumpPayload(boolean beginning) implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "air_jump");
    public static final Id<AirJumpPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, AirJumpPayload> CODEC = new PacketCodec<>() {
        @Override
        public AirJumpPayload decode(ByteBuf buf) {
            return new AirJumpPayload(buf.readBoolean());
        }

        @Override
        public void encode(ByteBuf buf, AirJumpPayload value) {
            buf.writeBoolean(value.beginning());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(AirJumpPayload payload, ServerPlayNetworking.Context context) {
        SoulComponent sc = SoulComponent.of(context.player());

        if (sc != null && !context.player().isSpectator() && !context.player().getAbilities().flying) {
            sc.setJetting(sc.getSoul() > 0 && payload.beginning() && !context.player().isOnGround() && sc.getSoulType() == ModSoulTypes.CONSTRUCT);
        }
    }
}
