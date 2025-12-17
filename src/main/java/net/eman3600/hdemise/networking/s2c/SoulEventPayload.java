package net.eman3600.hdemise.networking.s2c;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.function.TriConsumer;

import static net.eman3600.hdemise.HDemise.MODID;

public record SoulEventPayload(double x, double y, double z, float variance, SoulEventType type) implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "focus_sound");
    public static final Id<SoulEventPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, SoulEventPayload> CODEC = new PacketCodec<>() {
        @Override
        public SoulEventPayload decode(ByteBuf buf) {
            return new SoulEventPayload(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), SoulEventType.fromId(buf.readInt()));
        }

        @Override
        public void encode(ByteBuf buf, SoulEventPayload value) {
            buf.writeDouble(value.x);
            buf.writeDouble(value.y);
            buf.writeDouble(value.z);
            buf.writeFloat(value.variance);
            buf.writeInt(value.type.getId());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    @Environment(EnvType.CLIENT)
    public static void receive(SoulEventPayload payload, ClientPlayNetworking.Context context) {
        if (context.client().world != null) {
            payload.type.event.accept(context.player(), new Vec3d(payload.x, payload.y, payload.z), payload.variance);
        }
    }

    public enum SoulEventType {
        FOCUS((player, pos, variance) -> {
            player.getEntityWorld().playSoundClient(pos.x, pos.y, pos.z, SoundEvents.ENTITY_WITCH_DRINK, SoundCategory.PLAYERS, 1f, .75f + (variance * .5f), true);
        }),
        VANISH((player, pos, variance) -> {
            player.getEntityWorld().playSoundClient(pos.x, pos.y, pos.z, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, 1f, .95f + (variance * .3f), true);
        }),
        REAPPEAR((player, pos, variance) -> {
            player.getEntityWorld().playSoundClient(pos.x, pos.y, pos.z, SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 1f, .95f + (variance * .3f), true);
        });




        private final TriConsumer<PlayerEntity, Vec3d, Float> event;

        SoulEventType(TriConsumer<PlayerEntity, Vec3d, Float> event) {
            this.event = event;
        }

        public static SoulEventType fromId(int id) {
            return SoulEventType.values()[id];
        }

        public int getId() {
            for (int i = 0; i < SoulEventType.values().length; i++) {
                if (this == SoulEventType.values()[i]) {
                    return i;
                }
            }
            return -1;
        }
    }
}
