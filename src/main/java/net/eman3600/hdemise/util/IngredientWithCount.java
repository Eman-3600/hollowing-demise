package net.eman3600.hdemise.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.PairCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.StringIdentifiable;

public record IngredientWithCount(Ingredient ingredient, int count) {

    public static final MapCodec<IngredientWithCount> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("id").forGetter(IngredientWithCount::ingredient),
            StringIdentifiable.BasicCodec.INT.fieldOf("count").forGetter(IngredientWithCount::count)
    ).apply(inst, IngredientWithCount::new));

    public static final Codec<IngredientWithCount> CODEC = Codec.lazyInitialized(MAP_CODEC::codec);
    public static final PacketCodec<RegistryByteBuf, IngredientWithCount> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);


    public boolean test(ItemStack stack) {
        return ingredient().test(stack) && stack.getCount() >= this.count;
    }

    public void consume(ItemStack stack) {
        stack.decrement(this.count);
    }
}
