package net.eman3600.hdemise.init.basics;

import static net.eman3600.hdemise.HDemise.MODID;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> IMPASSABLE = createTag("impassable");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(MODID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> YELLOW_AUGMENT = createTag("yellow_augment");
        public static final TagKey<Item> GREEN_AUGMENT = createTag("green_augment");
        public static final TagKey<Item> RED_AUGMENT = createTag("red_augment");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(MODID, name));
        }
    }
}
