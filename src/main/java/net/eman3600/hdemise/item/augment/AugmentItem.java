package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.init.basics.ModTags;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AugmentItem extends Item {

    public static final Map<TagKey<Item>, Text> textMap = new HashMap<>();

    public AugmentItem(Settings settings) {
        super(settings.maxCount(1));
    }

    public void onEquip(PlayerEntity player, ItemStack stack) {
        onReload(player, stack);
    }
    public void onRespawn(PlayerEntity player, ItemStack stack, boolean alive) {}
    public void onRemove(PlayerEntity player, ItemStack stack) {
        player.clearStatusEffects();
    }
    public void onReload(PlayerEntity player, ItemStack stack) {}
    public void onTopUp(PlayerEntity player, ItemStack stack) {}

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        for (TagKey<Item> tag : textMap.keySet()) {
            if (stack.isIn(tag)) {
                textConsumer.accept(textMap.get(tag));
            }
        }
    }

    static {
        textMap.put(ModTags.Items.YELLOW_AUGMENT, Text.translatable("tag.item.hdemise.yellow_augment").withColor(Colors.YELLOW));
        textMap.put(ModTags.Items.GREEN_AUGMENT, Text.translatable("tag.item.hdemise.green_augment").withColor(Colors.GREEN));
        textMap.put(ModTags.Items.RED_AUGMENT, Text.translatable("tag.item.hdemise.red_augment").withColor(Colors.LIGHT_RED));
    }
}
