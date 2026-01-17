package net.eman3600.hdemise.networking.s2c;

import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.event.KeyInputHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.nio.charset.Charset;

import static net.eman3600.hdemise.HDemise.MODID;

public record TutorialPayload(String key) implements CustomPayload {
    public static final Identifier ID = Identifier.of(MODID, "tutorial");
    public static final CustomPayload.Id<TutorialPayload> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<ByteBuf, TutorialPayload> CODEC = new PacketCodec<>() {
        @Override
        public TutorialPayload decode(ByteBuf buf) {
            int i = buf.readInt();
            return new TutorialPayload(buf.readString(i, Charset.defaultCharset()));
        }

        @Override
        public void encode(ByteBuf buf, TutorialPayload value) {
            buf.writeInt(value.key.length());
            buf.writeCharSequence(value.key, Charset.defaultCharset());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void send(ServerPlayerEntity player, String key) {
        ServerPlayNetworking.send(player, new TutorialPayload(key));
    }

    @Environment(EnvType.CLIENT)
    public static void receive(TutorialPayload payload, ClientPlayNetworking.Context context) {
        if (context.client().world != null) {
            Text text = Text.translatable(payload.key(), KeyInputHandler.focusKey.getBoundKeyLocalizedText(), KeyInputHandler.ghostKey.getBoundKeyLocalizedText());
            context.client().inGameHud.setOverlayMessage(text, false);
            context.client().getNarratorManager().narrateSystemImmediately(text);
        }
    }
}
