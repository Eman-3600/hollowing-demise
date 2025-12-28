package net.eman3600.hdemise.init.basics;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup HDEMISE_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(MODID, "hdemise_group"),
     FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.DEMON_SCROLL))
        .displayName(Text.translatable("itemgroup.hdemise.hdemise_group"))
        .entries((displayContext, entries) -> {
            entries.add(ModBlocks.ALMARITE_ORE);
            entries.add(ModBlocks.DEEPSLATE_ALMARITE_ORE);
            entries.add(ModBlocks.ALMARITE_BLOCK);
            entries.add(ModItems.ALMARITE);
            entries.add(ModItems.SIMPLE_CURE);
            entries.add(ModItems.DEMON_SCROLL);
            entries.add(ModItems.AMETHYST_APPLE);
            entries.add(ModItems.EXPERIENCE_CORE);
            entries.add(ModBlocks.INFUSION_TABLE);
            entries.add(ModItems.FEATHER_TOKEN);
            entries.add(ModItems.ECTOPLASMIC_BONE);
            entries.add(ModItems.FORM_SWITCHER);
            entries.add(ModItems.PURE_SOUL);
            entries.add(ModItems.CRYSTAL_SOUL);
            entries.add(ModItems.FRACTURED_CRYSTAL_HEART);
        }).build());
    
    public static void registerItemGroups() {
        LOGGER.info("Registering Item Groups for " + MODID);
    }
}
