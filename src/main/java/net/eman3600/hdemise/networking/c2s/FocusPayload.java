package net.eman3600.hdemise.networking.c2s;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import static net.eman3600.hdemise.HDemise.MODID;

public record FocusPayload(boolean beginning) implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "focus");
    public static final Id<FocusPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, FocusPayload> CODEC = new PacketCodec<>() {
        @Override
        public FocusPayload decode(ByteBuf buf) {
            return new FocusPayload(buf.readBoolean());
        }

        @Override
        public void encode(ByteBuf buf, FocusPayload value) {
            buf.writeBoolean(value.beginning());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(FocusPayload payload, ServerPlayNetworking.Context context) {
        SoulComponent sc = SoulComponent.of(context.player());

        if (sc != null && !context.player().isSpectator()) {
            sc.setFocusing(payload.beginning() && sc.canFocus());
        }
    }
}
