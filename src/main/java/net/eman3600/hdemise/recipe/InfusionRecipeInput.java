package net.eman3600.hdemise.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record InfusionRecipeInput(ItemStack base, ItemStack ingredient1, ItemStack ingredient2) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        switch (slot) {
            case 0 -> {
                return base;
            }
            case 1 -> {
                return ingredient1;
            }
            case 2 -> {
                return ingredient2;
            }
            default -> {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public int size() {
        return 3;
    }
}
