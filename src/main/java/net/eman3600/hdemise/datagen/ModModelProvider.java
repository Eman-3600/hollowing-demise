package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.basics.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;

@Environment(EnvType.CLIENT)
public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ALMARITE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ALMARITE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_ALMARITE_ORE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ALMARITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.FORM_SWITCHER, Models.GENERATED);
        itemModelGenerator.register(ModItems.SIMPLE_CURE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DEMON_SCROLL, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXPERIENCE_CORE, Models.GENERATED);
    }
}
