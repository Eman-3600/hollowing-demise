package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.block.InfusionTableBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.function.Function;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModBlocks {
    
    public static final Block ALMARITE_BLOCK = register("almarite_block", Block::new, AbstractBlock.Settings.create()
        .strength(1.5f)
        .requiresTool()
        .sounds(BlockSoundGroup.AMETHYST_BLOCK), true);

    public static final Block ALMARITE_ORE = register("almarite_ore", settings -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
        AbstractBlock.Settings.create()
        .strength(3f)
        .requiresTool(), true);

    public static final Block DEEPSLATE_ALMARITE_ORE = register("deepslate_almarite_ore", settings -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 7), settings),
        AbstractBlock.Settings.create()
        .strength(4.5f, 3f)
        .requiresTool()
        .sounds(BlockSoundGroup.DEEPSLATE), true);

    public static final Block INFUSION_TABLE = register("infusion_table", InfusionTableBlock::new,
        AbstractBlock.Settings.create()
        .strength(5.0f, 1200.0f)
        .requiresTool()
        .luminance(state -> 10), true);

    /**
     * Registers a block under a given ID string.
     * @param name the block's internal name
     * @param blockFactory constructor for the block
     * @param settings block properties
     * @param shouldRegisterItem whether an item should be registered with the block
     * @return the registered block
     */
    public static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        // Create the block key.
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MODID, name));
        
        // Create the block instance.
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Register the block.
        Registry.register(Registries.BLOCK, blockKey, block);

        // Create the block item key.
        RegistryKey<Item> blockItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MODID, name));

        // Create the block item instance.
        BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(blockItemKey));

        // Register the block item.
        Registry.register(Registries.ITEM, blockItemKey, blockItem);

        return block;
    }

    public static void registerAll() {
        LOGGER.info("Registering Blocks for " + MODID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(group -> {
            group.add(ModBlocks.ALMARITE_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(group -> {
            group.add(ModBlocks.ALMARITE_ORE);
            group.add(ModBlocks.DEEPSLATE_ALMARITE_ORE);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(group -> {
            group.add(ModBlocks.INFUSION_TABLE);
        });
    }
}
