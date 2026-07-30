package net.eman3600.hdemise.networking.s2c;

import io.netty.buffer.ByteBuf;
import net.eman3600.hdemise.event.KeyInputHandler;
import net.eman3600.hdemise.recipe.InfusionRecipe;
import net.eman3600.hdemise.screen.InfusionScreen;
import net.eman3600.hdemise.util.IngredientWithCount;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import net.minecraft.util.context.ContextType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static net.eman3600.hdemise.HDemise.MODID;

public record InfusionRecipePayload(List<ItemStack> firstStacks, List<ItemStack> secondStacks) implements CustomPayload {
    public static final Identifier ID = Identifier.of(MODID, "infusion_recipe");
    public static final Id<InfusionRecipePayload> TYPE = new Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, InfusionRecipePayload> CODEC = new PacketCodec<>() {
        @Override
        public InfusionRecipePayload decode(RegistryByteBuf buf) {

            int i = buf.readInt();
            List<ItemStack> firstStacks = new ArrayList<>(i);
            while (i-- > 0) {
                firstStacks.add(ItemStack.PACKET_CODEC.decode(buf));
            }

            i = buf.readInt();
            List<ItemStack> secondStacks = new ArrayList<>(i);
            while (i-- > 0) {
                secondStacks.add(ItemStack.PACKET_CODEC.decode(buf));
            }

            return new InfusionRecipePayload(firstStacks, secondStacks);
        }

        @Override
        public void encode(RegistryByteBuf buf, InfusionRecipePayload value) {
            buf.writeInt(value.firstStacks.size());

            for (ItemStack stack : value.firstStacks) {
                ItemStack.PACKET_CODEC.encode(buf, stack);
            }

            buf.writeInt(value.secondStacks.size());

            for (ItemStack stack : value.secondStacks) {
                ItemStack.PACKET_CODEC.encode(buf, stack);
            }
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void send(ServerPlayerEntity player, InfusionRecipe recipe) {

        List<ItemStack> firstStacks = new ArrayList<>();
        List<ItemStack> secondStacks = new ArrayList<>();

        if (recipe != null) {

            IngredientWithCount countedIngredient = recipe.ingredient();
            int count = countedIngredient.count();
            Ingredient ingredient = countedIngredient.ingredient();

            Stream<RegistryEntry<Item>> itemStream = ingredient.getMatchingItems();
            for (Iterator<RegistryEntry<Item>> it = itemStream.iterator(); it.hasNext(); ) {
                RegistryEntry<Item> item = it.next();

                firstStacks.add(new ItemStack(item.value(), count));
            }

            if (recipe.repair()) {

                for (IngredientWithCount possibleIngredient : InfusionRecipe.repairIngredients) {
                    count = possibleIngredient.count();
                    ingredient = possibleIngredient.ingredient();

                    itemStream = ingredient.getMatchingItems();
                    for (Iterator<RegistryEntry<Item>> it = itemStream.iterator(); it.hasNext(); ) {
                        RegistryEntry<Item> item = it.next();

                        secondStacks.add(new ItemStack(item.value(), count));
                    }
                }
            }
        }

        ServerPlayNetworking.send(player, new InfusionRecipePayload(firstStacks, secondStacks));
    }

    @Environment(EnvType.CLIENT)
    public static void receive(InfusionRecipePayload payload, ClientPlayNetworking.Context context) {
        if (context.client().currentScreen instanceof InfusionScreen screen) {
            screen.updateRecipe(payload);
        }
    }
}
