package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.init.ModBlocks;
import net.eman3600.hdemise.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
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
                
                offerSmelting(ALMARITE_SMELTABLES, RecipeCategory.TOOLS, ModItems.ALMARITE, 1f, 200, "almarite");
                offerBlasting(ALMARITE_SMELTABLES, RecipeCategory.TOOLS, ModItems.ALMARITE, 1f, 100, "almarite");
                offerReversibleCompactingRecipes(RecipeCategory.BUILDING_BLOCKS, ModItems.ALMARITE, RecipeCategory.TOOLS, ModBlocks.ALMARITE_BLOCK);
            }
        };
    }

    @Override
    public String getName() {
        return "HDemise Recipes";
    }
}
