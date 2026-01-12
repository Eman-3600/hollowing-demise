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
        .icon(() -> new ItemStack(ModItems.PURE_SOUL))
        .displayName(Text.translatable("itemgroup.hdemise.hdemise_group"))
        .entries((displayContext, entries) -> {
            entries.add(ModBlocks.ALMARITE_ORE);
            entries.add(ModBlocks.DEEPSLATE_ALMARITE_ORE);
            entries.add(ModBlocks.ALMARITE_BLOCK);
            entries.add(ModItems.ALMARITE);
            entries.add(ModItems.ALMARITE_SCYTHE);
                entries.add(ModItems.ALMARITE_PIX);
            entries.add(ModItems.ALMARITE_HOEVE);
            entries.add(ModItems.SOUL_BASE);
            entries.add(ModItems.SOULROOT_BULB);
                entries.add(ModItems.SOULROOT_SEEDS);
            entries.add(ModItems.SOULROOT_SOUP);
                entries.add(ModItems.ECTOPLASM);
                entries.add(ModItems.SIMPLE_CURE);
                entries.add(ModItems.ECTOPLASM_REMEDY);
            entries.add(ModItems.AMETHYST_APPLE);
            entries.add(ModItems.CROSS);
            entries.add(ModItems.EXPERIENCE_CORE);
            entries.add(ModBlocks.INFUSION_TABLE);
            entries.add(ModItems.PURE_SOUL);
            entries.add(ModItems.CRYSTAL_SOUL);
            entries.add(ModItems.CRYSTAL_SOUL_FRACTURED);
            entries.add(ModItems.MAGE_SOUL);
            entries.add(ModItems.MAGE_SOUL_FRACTURED);
            entries.add(ModItems.CONSTRUCT_SOUL);
            entries.add(ModItems.CONSTRUCT_SOUL_FRACTURED);
            entries.add(ModItems.PHANTOM_SOUL);
            entries.add(ModItems.PHANTOM_SOUL_FRACTURED);
            entries.add(ModItems.REVENANT_SOUL);
            entries.add(ModItems.REVENANT_SOUL_FRACTURED);
                entries.add(ModItems.ANTISOUL);
            entries.add(ModItems.FEATHER_TOKEN);
            entries.add(ModItems.GOLEM_STRENGTH_BELT);
            entries.add(ModItems.BOTTLED_TEAR);
            entries.add(ModItems.CARVED_OBSIDIAN);
            entries.add(ModItems.STICKY_HAND);
            entries.add(ModItems.STARDUST);
            entries.add(ModItems.WHETSTONE);
            entries.add(ModItems.CRAB_CLAW);
            entries.add(ModItems.FROG_BALLOON);
            entries.add(ModItems.CRYSTAL_BALL);
            entries.add(ModItems.DEMON_SCROLL);
            entries.add(ModItems.RADIANT_JEWEL);
            entries.add(ModItems.ESSENCE_CORE);
            entries.add(ModItems.GOLDEN_FOOT);
                entries.add(ModItems.MORTICIAN_CHARM);
            entries.add(ModItems.CURSED_SKULL);
            entries.add(ModItems.MAGIC_FAN);
                entries.add(ModItems.FAST_FORWARD);
            entries.add(ModItems.ECTOPLASMIC_BONE);
            entries.add(ModItems.DRAGON_WING);
            entries.add(ModItems.GOLDEN_FLOWER);
            entries.add(ModItems.AGELESS_WATCH);
            entries.add(ModItems.METRONOME);
            entries.add(ModItems.FORBIDDEN_FRUIT);
            entries.add(ModItems.PRIDE_PENDANT);
            entries.add(ModItems.UNDYING_TALISMAN);
            entries.add(ModItems.WIND_STAFF);
            entries.add(ModItems.FORM_SWITCHER);
        }).build());
    
    public static void registerItemGroups() {
        LOGGER.info("Registering Item Groups for " + MODID);
    }
}
