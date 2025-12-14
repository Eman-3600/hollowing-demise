package net.eman3600.hdemise.networking.s2c;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public record FocusSoundPayload(double x, double y, double z) implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "focus_sound");
    public static final Id<FocusSoundPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, FocusSoundPayload> CODEC = new PacketCodec<>() {
        @Override
        public FocusSoundPayload decode(ByteBuf buf) {
            return new FocusSoundPayload(buf.readDouble(), buf.readDouble(), buf.readDouble());
        }

        @Override
        public void encode(ByteBuf buf, FocusSoundPayload value) {
            buf.writeDouble(value.x);
            buf.writeDouble(value.y);
            buf.writeDouble(value.z);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public static void receive(FocusSoundPayload payload, ClientPlayNetworking.Context context) {
        if (context.client().world != null) {
            context.client().world.playSoundClient(payload.x, payload.y, payload.z, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS, 1f, 1f, true);
        }
    }
}
