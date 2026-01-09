package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.recipe.InfusionRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModRecipes {
    public static final RecipeSerializer<InfusionRecipe> INFUSION_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(MODID, "infusion"), new InfusionRecipe.Serializer()
    );
    public static final RecipeType<InfusionRecipe> INFUSION_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(MODID, "infusion"), new RecipeType<InfusionRecipe>() {
                @Override
                public String toString() {
                    return "infusion";
                }
            }
    );

    public static void registerAll() {
        HDemise.LOGGER.info("Registering recipe types for {}", MODID);
    }
}
