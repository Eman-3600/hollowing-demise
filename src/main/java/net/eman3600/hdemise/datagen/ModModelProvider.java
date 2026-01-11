package net.eman3600.hdemise.datagen;

import net.eman3600.hdemise.block.SoulrootBlock;
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
        blockStateModelGenerator.registerSimpleState(ModBlocks.INFUSION_TABLE);

        blockStateModelGenerator.registerCrop(ModBlocks.SOULROOT, SoulrootBlock.AGE, 0, 1, 2, 3);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ALMARITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.FORM_SWITCHER, Models.GENERATED);
        itemModelGenerator.register(ModItems.SIMPLE_CURE, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXPERIENCE_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.AMETHYST_APPLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CROSS, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOULROOT_BULB, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOULROOT_SOUP, Models.GENERATED);
        itemModelGenerator.register(ModItems.SOUL_BASE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ECTOPLASM, Models.GENERATED);
        itemModelGenerator.register(ModItems.ECTOPLASM_REMEDY, Models.GENERATED);

        itemModelGenerator.register(ModItems.FEATHER_TOKEN, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLEM_STRENGTH_BELT, Models.GENERATED);
        itemModelGenerator.register(ModItems.BOTTLED_TEAR, Models.GENERATED);
        itemModelGenerator.register(ModItems.CARVED_OBSIDIAN, Models.GENERATED);
        itemModelGenerator.register(ModItems.STICKY_HAND, Models.GENERATED);
        itemModelGenerator.register(ModItems.STARDUST, Models.GENERATED);
        itemModelGenerator.register(ModItems.WHETSTONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CRAB_CLAW, Models.GENERATED);
        itemModelGenerator.register(ModItems.FROG_BALLOON, Models.GENERATED);
        itemModelGenerator.register(ModItems.DEMON_SCROLL, Models.GENERATED);
        itemModelGenerator.register(ModItems.CRYSTAL_BALL, Models.GENERATED);
        itemModelGenerator.register(ModItems.RADIANT_JEWEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.ESSENCE_CORE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLDEN_FOOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.MORTICIAN_CHARM, Models.GENERATED);
        itemModelGenerator.register(ModItems.CURSED_SKULL, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGIC_FAN, Models.GENERATED);
        itemModelGenerator.register(ModItems.FAST_FORWARD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ECTOPLASMIC_BONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.DRAGON_WING, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLDEN_FLOWER, Models.GENERATED);
        itemModelGenerator.register(ModItems.AGELESS_WATCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.METRONOME, Models.GENERATED);
        itemModelGenerator.register(ModItems.FORBIDDEN_FRUIT, Models.GENERATED);
        itemModelGenerator.register(ModItems.PRIDE_PENDANT, Models.GENERATED);
        itemModelGenerator.register(ModItems.UNDYING_TALISMAN, Models.GENERATED);

        itemModelGenerator.register(ModItems.ALMARITE_SCYTHE, Models.HANDHELD);
        itemModelGenerator.register(ModItems.ALMARITE_PIX, Models.HANDHELD);

        itemModelGenerator.register(ModItems.WIND_STAFF, Models.HANDHELD);

        itemModelGenerator.register(ModItems.PURE_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.CRYSTAL_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.CRYSTAL_SOUL_FRACTURED, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGE_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.MAGE_SOUL_FRACTURED, Models.GENERATED);
        itemModelGenerator.register(ModItems.CONSTRUCT_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.CONSTRUCT_SOUL_FRACTURED, Models.GENERATED);
        itemModelGenerator.register(ModItems.PHANTOM_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.PHANTOM_SOUL_FRACTURED, Models.GENERATED);
        itemModelGenerator.register(ModItems.REVENANT_SOUL, Models.GENERATED);
        itemModelGenerator.register(ModItems.REVENANT_SOUL_FRACTURED, Models.GENERATED);
        itemModelGenerator.register(ModItems.ANTISOUL, Models.GENERATED);
    }
}
