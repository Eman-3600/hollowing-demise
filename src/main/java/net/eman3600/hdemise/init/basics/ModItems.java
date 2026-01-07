package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.item.*;
import net.eman3600.hdemise.item.augment.AbsorptionAugmentItem;
import net.eman3600.hdemise.item.augment.AttributeAugmentItem;
import net.eman3600.hdemise.item.augment.AugmentItem;
import net.eman3600.hdemise.item.augment.NightVisionAugmentItem;
import net.eman3600.hdemise.item.soul_using.WindStaffItem;
import net.eman3600.hdemise.util.ModToolMaterials;
import net.eman3600.hdemise.util.SoulAttribute;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
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

    public static final FoodComponent SOUL_BERRY_FOOD = new FoodComponent.Builder().nutrition(2).saturationModifier(.75F).build();



    public static final Item ALMARITE = register("almarite", XPItem::new, new Item.Settings());
    public static final Item FORM_SWITCHER = register("form_switcher", FormSwitcherItem::new, new Item.Settings().rarity(Rarity.EPIC).maxCount(1));
    public static final Item SIMPLE_CURE = register("simple_cure", ConsumableCureItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1));
    public static final Item AMETHYST_APPLE = register("amethyst_apple", AmethystAppleItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).food(FoodComponents.APPLE));
    public static final Item CROSS = register("cross", CrossItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1).maxDamage(64));
    public static final XPCoreItem EXPERIENCE_CORE = (XPCoreItem) register("experience_core", XPCoreItem::new, new Item.Settings().rarity(Rarity.RARE).maxCount(1).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final Item SOULROOT_BULB = register("soulroot_bulb", (settings) -> new EssenceFoodItem(settings, SoulComponent.SOUL_PER_VESSEL * 3 / 2), new Item.Settings().food(SOUL_BERRY_FOOD));
    public static final Item SOUL_BASE = register("soul_base", Item::new, new Item.Settings());
    public static final Item SOULROOT_SEEDS = register("soulroot_seeds", settings -> new BlockItem(ModBlocks.SOULROOT, settings), new Item.Settings().component(ModDataComponentTypes.TOOLTIP_LINES, 1));
    public static final Item SOULROOT_SOUP = register("soulroot_soup", EdibleCureItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).food(FoodComponents.BEETROOT_SOUP).useRemainder(Items.BOWL));


    public static final Item FEATHER_TOKEN = register("feather_token", (settings -> new AttributeAugmentItem(
            settings,
            2,
            new SoulAttribute(EntityAttributes.MOVEMENT_SPEED, .15, Operation.ADD_MULTIPLIED_BASE)
    )), new Item.Settings());
    public static final Item GOLEM_STRENGTH_BELT = register("golem_strength_belt", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(EntityAttributes.ATTACK_DAMAGE, 2, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item BOTTLED_TEAR = register("bottled_tear", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(EntityAttributes.MAX_HEALTH, 4d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item CARVED_OBSIDIAN = register("carved_obsidian", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(EntityAttributes.ARMOR, 4d, Operation.ADD_VALUE),
            new SoulAttribute(EntityAttributes.ARMOR_TOUGHNESS, 4d, Operation.ADD_VALUE),
            new SoulAttribute(EntityAttributes.KNOCKBACK_RESISTANCE, .2d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item STICKY_HAND = register("sticky_hand", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(EntityAttributes.BLOCK_INTERACTION_RANGE, 1.5d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item STARDUST = register("stardust", (settings -> new AugmentItem(settings, 1)), new Item.Settings());
    public static final Item WHETSTONE = register("whetstone", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(EntityAttributes.ATTACK_SPEED, 0.15d, Operation.ADD_MULTIPLIED_BASE)
    )), new Item.Settings());
    public static final Item DEMON_SCROLL = register("demon_scroll", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(ModAttributes.MAX_SOUL, 5d, Operation.ADD_VALUE),
            new SoulAttribute(EntityAttributes.MAX_HEALTH, -6d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item CRYSTAL_BALL = register("crystal_ball", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(ModAttributes.FOCUS_POWER, 2d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item RADIANT_JEWEL = register("radiant_jewel", (settings -> new NightVisionAugmentItem(settings, 1)), new Item.Settings());
    public static final Item ESSENCE_CORE = register("essence_core", settings -> new AugmentItem(settings, 2), new Item.Settings());
    public static final Item GOLDEN_FOOT = register("golden_foot", settings -> new AugmentItem(settings, 2), new Item.Settings());
    public static final Item MORTICIAN_CHARM = register("mortician_charm", (settings -> new AttributeAugmentItem(
            settings,
            new SoulAttribute(ModAttributes.MAX_SOUL, 2d, Operation.ADD_VALUE)
    )), new Item.Settings());
    public static final Item ECTOPLASMIC_BONE = register("ectoplasmic_bone", settings -> new AugmentItem(settings, 1), new Item.Settings());
    public static final Item DRAGON_WING = register("dragon_wing", settings -> new AugmentItem(settings, 1), new Item.Settings());
    public static final Item GOLDEN_FLOWER = register("golden_flower", settings -> new AbsorptionAugmentItem(settings, 1), new Item.Settings());
    public static final Item AGELESS_WATCH = register("ageless_watch", settings -> new AugmentItem(settings, 1), new Item.Settings());
    public static final Item METRONOME = register("metronome", settings -> new AugmentItem(settings, 1), new Item.Settings());
    public static final Item FORBIDDEN_FRUIT = register("forbidden_fruit", settings -> new AttributeAugmentItem(
            settings,
            2,
            new SoulAttribute(ModAttributes.FOCUS_POWER, -2, Operation.ADD_VALUE)
    ), new Item.Settings());


    public static final Item ALMARITE_SCYTHE = register("almarite_scythe", Item::new, new Item.Settings().sword(ModToolMaterials.ALMARITE, 3.5F, -3F)
            .component(ModDataComponentTypes.TOOLTIP_LINES, 2));

    public static final Item ALMARITE_PIX = register("almarite_pix", settings -> new PixItem(ModToolMaterials.ALMARITE, 4, -2.8f, settings), new Item.Settings().component(ModDataComponentTypes.TOOLTIP_LINES, 1));


    public static final Item WIND_STAFF = register("wind_staff", WindStaffItem::new, new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1).maxDamage(1250).repairable(Items.WIND_CHARGE));


    public static final Item PURE_SOUL = register("pure_soul", (settings) -> new SoulItem(settings, ModSoulTypes.MORTAL), SoulItem.getDefaultSettings());
    public static final Item CRYSTAL_SOUL_FRACTURED = register("crystal_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item CRYSTAL_SOUL = register("crystal_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.CRYSTAL, CRYSTAL_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item CONSTRUCT_SOUL_FRACTURED = register("construct_soul_fractured", Item::new, SoulItem.getDefaultSettings().fireproof());
    public static final Item CONSTRUCT_SOUL = register("construct_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.CONSTRUCT, CONSTRUCT_SOUL_FRACTURED), SoulItem.getDefaultSettings().fireproof());
    public static final Item PHANTOM_SOUL_FRACTURED = register("phantom_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item PHANTOM_SOUL = register("phantom_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.PHANTOM, PHANTOM_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item REVENANT_SOUL_FRACTURED = register("revenant_soul_fractured", Item::new, SoulItem.getDefaultSettings());
    public static final Item REVENANT_SOUL = register("revenant_soul", (settings) -> new BreakableSoulItem(settings, ModSoulTypes.REVENANT, REVENANT_SOUL_FRACTURED), SoulItem.getDefaultSettings());
    public static final Item ANTISOUL = register("antisoul", (settings) -> new SoulItem(settings, ModSoulTypes.NEGATIVE), SoulItem.getDefaultSettings().rarity(Rarity.EPIC));

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
            group.add(ModItems.SOUL_BASE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((group) -> {
            group.add(ModItems.ALMARITE);
            group.add(ModItems.ALMARITE_SCYTHE);
            group.add(ModItems.ALMARITE_PIX);
            group.add(ModItems.CROSS);
            group.add(ModItems.WIND_STAFF);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((group) -> {
            group.add(ModItems.ALMARITE);
            group.add(ModItems.ALMARITE_PIX);
            group.add(ModItems.WIND_STAFF);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register((group) -> {
            group.add(ModItems.SOULROOT_BULB);
            group.add(ModItems.SOULROOT_SOUP);
            group.add(ModItems.AMETHYST_APPLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register((group) -> {
            group.add(ModItems.FORM_SWITCHER);
        });
    }

}