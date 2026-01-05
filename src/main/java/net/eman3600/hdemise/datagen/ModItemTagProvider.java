package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        valueLookupBuilder(ItemTags.BEACON_PAYMENT_ITEMS)
                .add(ModItems.ALMARITE);

        valueLookupBuilder(ItemTags.SWORDS)
                .add(ModItems.ALMARITE_SCYTHE);


        valueLookupBuilder(ModTags.Items.YELLOW_AUGMENT)
                .add(ModItems.FEATHER_TOKEN)
                .add(ModItems.GOLEM_STRENGTH_BELT)
                .add(ModItems.BOTTLED_TEAR)
                .add(ModItems.CARVED_OBSIDIAN);

        valueLookupBuilder(ModTags.Items.GREEN_AUGMENT)
                .add(ModItems.DEMON_SCROLL)
                .add(ModItems.CRYSTAL_BALL)
                .add(ModItems.RADIANT_JEWEL);

        valueLookupBuilder(ModTags.Items.RED_AUGMENT)
                .add(ModItems.ECTOPLASMIC_BONE);



        valueLookupBuilder(ModTags.Items.AERIAL_IMPROVEMENT)
                .add(ModItems.FEATHER_TOKEN);

        valueLookupBuilder(ModTags.Items.XP_ABSORBENT)
                .add(ModItems.ECTOPLASMIC_BONE);



        valueLookupBuilder(ModTags.Items.REAPER)
                .add(ModItems.ALMARITE_SCYTHE);

        valueLookupBuilder(ModTags.Items.ALMARITE_REPAIR)
                .add(ModItems.ALMARITE)
                .add(ModItems.SOULROOT_BULB);
    }
}
