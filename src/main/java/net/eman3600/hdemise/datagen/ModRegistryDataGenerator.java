package net.eman3600.hdemise.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class ModRegistryDataGenerator extends FabricDynamicRegistryProvider{

    public ModRegistryDataGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(registries.getOrThrow(RegistryKeys.PLACED_FEATURE));
    }
    
    @Override
    public String getName() {
        return "HDemise Registries";
    }
}
