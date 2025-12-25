package net.eman3600.hdemise.networking.c2s;

import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public record SpecialAbilityPayload(boolean beginning) implements CustomPayload {

    public static final Identifier ID = Identifier.of(MODID, "special_ability");
    public static final Id<SpecialAbilityPayload> TYPE = new Id<>(ID);
    public static final PacketCodec<ByteBuf, SpecialAbilityPayload> CODEC = new PacketCodec<>() {
        @Override
        public SpecialAbilityPayload decode(ByteBuf buf) {
            return new SpecialAbilityPayload(buf.readBoolean());
        }

        @Override
        public void encode(ByteBuf buf, SpecialAbilityPayload value) {
            buf.writeBoolean(value.beginning());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(SpecialAbilityPayload payload, ServerPlayNetworking.Context context) {
        SoulComponent sc = SoulComponent.of(context.player());

        if (sc != null && !context.player().isSpectator()) {

        }
    }
}
