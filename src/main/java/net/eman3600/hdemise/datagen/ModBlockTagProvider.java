package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.init.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(ModBlocks.ALMARITE_BLOCK)
            .add(ModBlocks.ALMARITE_ORE)
            .add(ModBlocks.DEEPSLATE_ALMARITE_ORE);

        valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
            .add(ModBlocks.ALMARITE_ORE)
            .add(ModBlocks.DEEPSLATE_ALMARITE_ORE);
    }
}
