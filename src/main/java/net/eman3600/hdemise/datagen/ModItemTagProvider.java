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

        valueLookupBuilder(ItemTags.PICKAXES)
                .add(ModItems.ALMARITE_TUNNELER);

        valueLookupBuilder(ItemTags.AXES)
                .add(ModItems.ALMARITE_FELLER);

        valueLookupBuilder(ItemTags.SHOVELS)
                .add(ModItems.ALMARITE_TUNNELER);

        valueLookupBuilder(ItemTags.HOES)
                .add(ModItems.ALMARITE_FELLER);

        valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ModItems.WIND_STAFF);

        valueLookupBuilder(ItemTags.TRIM_MATERIALS)
                .add(ModItems.ALMARITE)
                .add(ModItems.ECTOPLASM);


        valueLookupBuilder(ModTags.Items.YELLOW_AUGMENT)
                .add(ModItems.FEATHER_TOKEN)
                .add(ModItems.GOLEM_STRENGTH_BELT)
                .add(ModItems.BOTTLED_TEAR)
                .add(ModItems.CARVED_OBSIDIAN)
                .add(ModItems.STICKY_HAND)
                .add(ModItems.STARDUST)
                .add(ModItems.WHETSTONE)
                .add(ModItems.CRAB_CLAW)
                .add(ModItems.FROG_BALLOON);

        valueLookupBuilder(ModTags.Items.GREEN_AUGMENT)
                .add(ModItems.DEMON_SCROLL)
                .add(ModItems.CRYSTAL_BALL)
                .add(ModItems.RADIANT_JEWEL)
                .add(ModItems.ESSENCE_CORE)
                .add(ModItems.GOLDEN_FOOT)
                .add(ModItems.MORTICIAN_CHARM)
                .add(ModItems.CURSED_SKULL)
                .add(ModItems.MAGIC_FAN)
                .add(ModItems.FAST_FORWARD);

        valueLookupBuilder(ModTags.Items.RED_AUGMENT)
                .add(ModItems.ECTOPLASMIC_BONE)
                .add(ModItems.DRAGON_WING)
                .add(ModItems.GOLDEN_FLOWER)
                .add(ModItems.AGELESS_WATCH)
                .add(ModItems.METRONOME)
                .add(ModItems.FORBIDDEN_FRUIT)
                .add(ModItems.PRIDE_PENDANT)
                .add(ModItems.UNDYING_TALISMAN);


        valueLookupBuilder(ModTags.Items.MORTICIAN_AUGMENT_TRADE)
                .add(ModItems.FEATHER_TOKEN)
                .add(ModItems.GOLEM_STRENGTH_BELT)
                .add(ModItems.CARVED_OBSIDIAN)
                .add(ModItems.WHETSTONE)
                .add(ModItems.STICKY_HAND)
                .add(ModItems.CRAB_CLAW);


        valueLookupBuilder(ModTags.Items.NEGATES_FALL)
                .add(ModItems.DRAGON_WING);

        valueLookupBuilder(ModTags.Items.XP_ABSORBENT)
                .add(ModItems.ECTOPLASMIC_BONE);



        valueLookupBuilder(ModTags.Items.REAPER)
                .add(ModItems.ALMARITE_SCYTHE);

        valueLookupBuilder(ModTags.Items.ALMARITE_REPAIR)
                .add(ModItems.ALMARITE)
                .add(ModItems.SOULROOT_BULB);

        valueLookupBuilder(ModTags.Items.UNREMOVEABLE_SOUL)
                .add(ModItems.ANTISOUL);

        valueLookupBuilder(ModTags.Items.REPAIRABLE_SOULS)
                .add(ModItems.PHANTOM_SOUL)
                .add(ModItems.CRYSTAL_SOUL)
                .add(ModItems.MAGE_SOUL)
                .add(ModItems.CONSTRUCT_SOUL)
                .add(ModItems.REVENANT_SOUL)
                .add(ModItems.PALE_SOUL);
    }
}
