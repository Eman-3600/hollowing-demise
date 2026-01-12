package net.eman3600.hdemise.data_component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

public record XPStorageComponent(int totalXP, int displayLevel, int displayPoints) {
    public static final Codec<XPStorageComponent> CODEC = Codec.INT_STREAM.comapFlatMap(
                    stream -> Util.decodeFixedLengthArray(stream, 3).map(values -> new XPStorageComponent(values[0], values[1], values[2])),
                    xp -> IntStream.of(xp.totalXP, xp.displayLevel, xp.displayPoints)
            ).stable();

    public static final PacketCodec<ByteBuf, XPStorageComponent> PACKET_CODEC = new PacketCodec<ByteBuf, XPStorageComponent>() {
        public XPStorageComponent decode(ByteBuf byteBuf) {
            return new XPStorageComponent(byteBuf.readInt(), byteBuf.readInt(), byteBuf.readInt());
        }

        public void encode(ByteBuf byteBuf, XPStorageComponent xp) {
            byteBuf.writeInt(xp.totalXP);
            byteBuf.writeInt(xp.displayLevel);
            byteBuf.writeInt(xp.displayPoints);
        }
    };

    public static XPStorageComponent extractExperience(PlayerEntity player) {
        int displayLevel = player.experienceLevel;
        int displayPoints = (int)(player.getNextLevelExperience() * player.experienceProgress);
        int totalXP = displayPoints;

        while (player.experienceLevel > 0) {
            player.experienceLevel--;

            totalXP += player.getNextLevelExperience();
        }

        player.totalExperience = 0;
        player.experienceProgress = 0;
        player.experienceLevel = 0;

        return new XPStorageComponent(totalXP, displayLevel, displayPoints);
    }
}
