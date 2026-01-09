package net.eman3600.hdemise.recipe;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.util.IngredientWithCount;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public final class InfusionRecipe implements Recipe<InfusionRecipeInput> {
    private final Ingredient baseItem;
    private final IngredientWithCount ingredient;
    private final boolean repair;
    private final boolean keepBase;
    private final ItemStack output;

    private final DefaultedList<Ingredient> ingredients = DefaultedList.of();

    public static final List<IngredientWithCount> repairIngredients = List.of(
            new IngredientWithCount(Ingredient.ofItem(ModItems.ALMARITE), 4),
            new IngredientWithCount(Ingredient.ofItem(ModItems.ECTOPLASM), 16),
            new IngredientWithCount(Ingredient.ofItem(Items.GHAST_TEAR), 1)
    );

    public InfusionRecipe(Ingredient baseItem, IngredientWithCount ingredient, boolean repair, boolean keepBase, ItemStack output) {
        this.baseItem = baseItem;
        this.ingredient = ingredient;
        this.repair = repair;
        this.keepBase = keepBase;
        this.output = output;

        ingredients.add(baseItem);
        ingredients.add(ingredient.ingredient());
        if (repair) {
            for (IngredientWithCount i : repairIngredients) {
                ingredients.add(i.ingredient());
            }
        }
    }

    @Override
    public boolean matches(InfusionRecipeInput input, World world) {
        return false;
    }

    @Override
    public ItemStack craft(InfusionRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<InfusionRecipeInput>> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends Recipe<InfusionRecipeInput>> getType() {
        return null;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forShapeless(ingredients);
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    public Ingredient baseItem() {
        return baseItem;
    }

    public IngredientWithCount ingredient() {
        return ingredient;
    }

    public boolean repair() {
        return repair;
    }

    public boolean keepBase() {
        return keepBase;
    }

    public ItemStack output() {
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (InfusionRecipe) obj;
        return Objects.equals(this.baseItem, that.baseItem) &&
                Objects.equals(this.ingredient, that.ingredient) &&
                this.repair == that.repair &&
                this.keepBase == that.keepBase &&
                Objects.equals(this.output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseItem, ingredient, repair, keepBase, output);
    }

    @Override
    public String toString() {
        return "InfusionRecipe[" +
                "baseItem=" + baseItem + ", " +
                "ingredient=" + ingredient + ", " +
                "repair=" + repair + ", " +
                "keepBase=" + keepBase + ", " +
                "output=" + output + ']';
    }

}
