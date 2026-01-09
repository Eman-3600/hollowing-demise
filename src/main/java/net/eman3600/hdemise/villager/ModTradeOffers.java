package net.eman3600.hdemise.villager;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.item.SoulItem;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.*;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModTradeOffers {

    public static final RegistryKey<VillagerProfession> MORTICIAN = getKey("mortician");

    public static void registerTradeOffers() {
        LOGGER.info("Registering Trade Offers for " + MODID);

        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 1, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.ALMARITE, 3),
                    new ItemStack(Items.EMERALD, 1), 16, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.AMETHYST_SHARD, 6),
                    new ItemStack(Items.EMERALD, 1), 16, 2, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.BONE, 20),
                    new ItemStack(Items.EMERALD, 1), 16, 2, 0.04f));

            factories.add(createMorticianTradeFactory(24, 4, 1, 0.04f));
        });


        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 2, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.CRYING_OBSIDIAN, 6),
                    new ItemStack(Items.EMERALD, 1), 16, 10, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 12),
                    new ItemStack(ModItems.SIMPLE_CURE, 1), 12, 5, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 2),
                    Optional.of(new TradedItem(Items.ENDER_PEARL)),
                    new ItemStack(Items.ENDER_EYE, 1), 12, 5, 0.04f));
        });


        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 3, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.SKELETON_SKULL),
                    new ItemStack(Items.EMERALD, 10), 4, 20, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 18),
                    new ItemStack(Items.WITHER_SKELETON_SKULL), 12, 10, 0.04f));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 12),
                    new ItemStack(ModItems.SOULROOT_SEEDS, 1), 12, 10, 0.04f));
        });


        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 4, factories -> {
//            factories.add((world, entity, random) -> new TradeOffer(
//                    new TradedItem(Items.EMERALD, 32),
//                    Optional.of(new TradedItem(ModItems.PHANTOM_SOUL_FRACTURED)),
//                    new ItemStack(ModItems.PHANTOM_SOUL), 4, 2, 0.04f));
            factories.add(new SoulItemRepairFactory(30, 32, 12, 0.04f, ModTags.Items.REPAIRABLE_SOULS));

            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 30),
                    new ItemStack(ModItems.MORTICIAN_CHARM), 12, 20, 0.04f));
        });


        TradeOfferHelper.registerVillagerOffers(MORTICIAN, 5, factories -> {
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.SCULK_CATALYST),
                    new ItemStack(Items.EMERALD, 2), 16, 4, 0.04f));
            factories.add((world, entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 50),
                    Optional.of(new TradedItem(ModItems.SOUL_BASE)),
                    new ItemStack(ModItems.PHANTOM_SOUL), 4, 4, 0.04f));
        });
    }

    private static TradeOffers.Factory createMorticianTradeFactory(int price, int maxUses, int merchantExperience, float priceMultiplier) {
        return new AugmentItemFactory(merchantExperience, price, maxUses, priceMultiplier, ModTags.Items.MORTICIAN_AUGMENT_TRADE);
    }

    public static class AugmentItemFactory implements TradeOffers.Factory {
        private final int experience;
        private final int price;
        private final int maxUses;
        private final float priceMultiplier;
        private final TagKey<Item> possibleAugments;

        public AugmentItemFactory(int experience, int price, int maxUses, float priceMultiplier, TagKey<Item> possibleAugments) {
            this.experience = experience;
            this.price = price;
            this.maxUses = maxUses;
            this.priceMultiplier = priceMultiplier;
            this.possibleAugments = possibleAugments;
        }

        @Override
        public @Nullable TradeOffer create(ServerWorld world, Entity entity, Random random) {
            Optional<RegistryEntry<Item>> optional = world.getRegistryManager()
                    .getOrThrow(RegistryKeys.ITEM)
                    .getRandomEntry(this.possibleAugments, random);
            ItemStack itemStack = ItemStack.EMPTY;
            if (optional.isPresent()) {
                RegistryEntry<Item> registryEntry = optional.get();
                Item item = registryEntry.value();
                itemStack = new ItemStack(item);
            }
            return new TradeOffer(new TradedItem(Items.EMERALD, this.price), itemStack, this.maxUses, this.experience, this.priceMultiplier);
        }
    }

    public static class SoulItemRepairFactory implements TradeOffers.Factory {
        private final int experience;
        private final int price;
        private final int maxUses;
        private final float priceMultiplier;
        private final TagKey<Item> possibleSouls;

        public SoulItemRepairFactory(int experience, int price, int maxUses, float priceMultiplier, TagKey<Item> possibleSouls) {
            this.experience = experience;
            this.price = price;
            this.maxUses = maxUses;
            this.priceMultiplier = priceMultiplier;
            this.possibleSouls = possibleSouls;
        }

        @Override
        public @Nullable TradeOffer create(ServerWorld world, Entity entity, Random random) {
            Optional<RegistryEntry<Item>> optional = world.getRegistryManager()
                    .getOrThrow(RegistryKeys.ITEM)
                    .getRandomEntry(this.possibleSouls, random);
            ItemStack itemStack = ItemStack.EMPTY;
            ItemStack itemStack2 = ItemStack.EMPTY;
            if (optional.isPresent()) {
                RegistryEntry<Item> registryEntry = optional.get();
                Item item = registryEntry.value();
                itemStack = new ItemStack(item);
                if (item instanceof SoulItem soul) {
                    itemStack2 = soul.breakSoul();
                }
            }
            return new TradeOffer(new TradedItem(itemStack2.getItem(), 1), Optional.of(new TradedItem(Items.EMERALD, this.price)), itemStack, this.maxUses, this.experience, this.priceMultiplier);
        }
    }

    private static RegistryKey<VillagerProfession> getKey(String name) {
        return RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, Identifier.of(MODID, name));
    }
}
