package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.item.*;
import net.eman3600.hdemise.item.soul_using.WindStaffItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModItems {

    public static final Item ALMARITE = register("almarite", XPItem::new, new Item.Settings());
    public static final Item FORM_SWITCHER = register("form_switcher", FormSwitcherItem::new, new Item.Settings().rarity(Rarity.EPIC).maxCount(1));
    public static final Item SIMPLE_CURE = register("simple_cure", ConsumableCureItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1));
    public static final Item AMETHYST_APPLE = register("amethyst_apple", AmethystAppleItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).food(FoodComponents.APPLE));
    public static final Item CROSS = register("cross", CrossItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1).maxDamage(64));
    public static final XPCoreItem EXPERIENCE_CORE = (XPCoreItem) register("experience_core", XPCoreItem::new, new Item.Settings().rarity(Rarity.RARE).maxCount(1).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true));

    public static final Item FEATHER_TOKEN = register("feather_token", Item::new, new Item.Settings());
    public static final Item ECTOPLASMIC_BONE = register("ectoplasmic_bone", Item::new, new Item.Settings());
    public static final Item GOLEM_STRENGTH_BELT = register("golem_strength_belt", Item::new, new Item.Settings());
    public static final Item DEMON_SCROLL = register("demon_scroll", ConsumableDemonItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1));

    public static final Item WIND_STAFF = register("wind_staff", WindStaffItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1).maxDamage(1200));

    public static final Item PURE_SOUL = register("pure_soul", (settings) -> new SoulItem(settings, ModSoulTypes.MORTAL), SoulItem.getDefaultSettings());
    public static final Item CRYSTAL_SOUL_FRACTURED = register("crystal_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item CRYSTAL_SOUL = register("crystal_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.CRYSTAL, CRYSTAL_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item CONSTRUCT_SOUL_FRACTURED = register("construct_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item CONSTRUCT_SOUL = register("construct_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.CONSTRUCT, CONSTRUCT_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item PHANTOM_SOUL_FRACTURED = register("phantom_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item PHANTOM_SOUL = register("phantom_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.PHANTOM, PHANTOM_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item REVENANT_SOUL_FRACTURED = register("revenant_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item REVENANT_SOUL = register("revenant_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.REVENANT, REVENANT_SOUL_FRACTURED), SoulItem.getDefaultSettings());

    /**
     * Registers an item under a given ID string.
     * @param name the item's internal name
     * @param itemFactory constructor for the item
     * @param settings item properties
     * @return the registered item
     */
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MODID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void registerAll() {
        LOGGER.info("Registering Items for " + MODID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register((group) -> {
            group.add(ModItems.ALMARITE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((group) -> {
            group.add(ModItems.ALMARITE);
            group.add(ModItems.CROSS);
            group.add(ModItems.WIND_STAFF);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((group) -> {
            group.add(ModItems.ALMARITE);
            group.add(ModItems.SIMPLE_CURE);
            group.add(ModItems.DEMON_SCROLL);
            group.add(ModItems.AMETHYST_APPLE);
            group.add(ModItems.EXPERIENCE_CORE);
            group.add(ModItems.WIND_STAFF);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((group) -> {
            group.add(ModItems.AMETHYST_APPLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register((group) -> {
            group.add(ModItems.FORM_SWITCHER);
        });
    }

}