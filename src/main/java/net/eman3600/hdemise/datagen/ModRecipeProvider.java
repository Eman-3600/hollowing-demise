package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.basics.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeGenerator getRecipeGenerator(RegistryWrapper.@NotNull WrapperLookup wrapperLookup, @NotNull RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {

                List<ItemConvertible> ALMARITE_SMELTABLES = List.of(ModBlocks.ALMARITE_ORE, ModBlocks.DEEPSLATE_ALMARITE_ORE);

                offerSmelting(ALMARITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALMARITE, 1f, 200, "almarite");
                offerBlasting(ALMARITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALMARITE, 1f, 100, "almarite");

                offerSmelting(List.of(ModItems.SOULROOT_BULB), RecipeCategory.MISC, ModItems.ECTOPLASM, 3f, 200, "ectoplasm");
                offerSmoking(List.of(ModItems.SOULROOT_BULB), RecipeCategory.MISC, ModItems.ECTOPLASM, 3f, 100, "ectoplasm");

                offerReversibleCompactingRecipes(RecipeCategory.MISC, ModItems.ALMARITE, RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALMARITE_BLOCK);
                offerReversibleCompactingRecipes(RecipeCategory.MISC, ModItems.POLTERIUM_INGOT, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLTERIUM_BLOCK);


            }

            public void offerSmoking(List<ItemConvertible> inputs, RecipeCategory category, ItemConvertible output, float experience, int cookingTime, String group) {
                offerMultipleOptions(RecipeSerializer.SMOKING, SmokingRecipe::new, inputs, category, output, experience, cookingTime, group, "_from_smoking");
                offerMultipleOptions(RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, inputs, category, output, experience, cookingTime * 6, group, "_from_campfire_cooking");
            }
        };
    }

    @Override
    public String getName() {
        return "HDemise Recipes";
    }
}
