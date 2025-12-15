package net.eman3600.hdemise.init;

import net.eman3600.hdemise.item.ConsumableCureItem;
import net.eman3600.hdemise.item.FormSwitcherItem;
import net.eman3600.hdemise.item.XPItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
    public static final Item FORM_SWITCHER = register("form_switcher", FormSwitcherItem::new, new Item.Settings().rarity(Rarity.EPIC));
    public static final Item SIMPLE_CURE = register("simple_cure", ConsumableCureItem::new, new Item.Settings().rarity(Rarity.UNCOMMON));



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
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((group) -> {
            group.add(ModItems.ALMARITE);
            group.add(ModItems.SIMPLE_CURE);
            group.add(ModItems.FORM_SWITCHER);
        });
    }

}