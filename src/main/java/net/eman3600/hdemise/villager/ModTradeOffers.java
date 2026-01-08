package net.eman3600.hdemise.villager;

import net.eman3600.hdemise.init.basics.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import java.util.Optional;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModTradeOffers {

    public static final RegistryKey<VillagerProfession> MORTICIAN = getKey("mortician");

    public static void registerTradeOffers() {
        LOGGER.info("Registering Trade Offers for " + MODID);

        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 1, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.ALMARITE, 8),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.AMETHYST_SHARD, 16),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.BONE, 20),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 24),
                    new ItemStack(ModItems.FEATHER_TOKEN, 1), 4, 2, 0.04f));
        });
        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 2, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.CRYING_OBSIDIAN, 6),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 12),
                    new ItemStack(ModItems.SIMPLE_CURE, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 2),
                    Optional.of(new TradedItem(Items.ENDER_PEARL)),
                    new ItemStack(ModItems.SIMPLE_CURE, 1), 4, 2, 0.04f));
        });
        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 3, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.SKELETON_SKULL),
                    new ItemStack(Items.EMERALD, 10), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 18),
                    new ItemStack(Items.WITHER_SKELETON_SKULL), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 12),
                    new ItemStack(ModItems.SOULROOT_SEEDS, 1), 4, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 30),
                    new ItemStack(ModItems.MORTICIAN_CHARM), 4, 2, 0.04f));
        });
        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 4, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 32),
                    Optional.of(new TradedItem(ModItems.PHANTOM_SOUL_FRACTURED)),
                    new ItemStack(ModItems.PHANTOM_SOUL), 4, 2, 0.04f));
        });
        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 5, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.SCULK_CATALYST),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.04f));
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 50),
                    Optional.of(new TradedItem(ModItems.PURE_SOUL)),
                    new ItemStack(ModItems.PHANTOM_SOUL), 4, 2, 0.04f));
        });
    }

    private static RegistryKey<VillagerProfession> getKey(String name) {
        return RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, Identifier.of(MODID, name));
    }
}
