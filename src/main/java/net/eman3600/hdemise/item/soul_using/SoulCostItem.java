package net.eman3600.hdemise.item.soul_using;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public interface SoulCostItem {
    int getBaseSoulCost();

    default int getStackSoulCost(ItemStack stack) {
        return getBaseSoulCost();
    }

    default int getSoulCost(PlayerEntity player, ItemStack stack) {
        return getStackSoulCost(stack);
    }

    default boolean canAffordSoul(PlayerEntity player, ItemStack stack) {
        if (player != null)
            return SoulComponent.of(player).getSoul() >= getSoulCost(player, stack) || player.isCreative();
        return false;
    }
    default Text getTooltipSoul(ItemStack stack) {
        int cost = getStackSoulCost(stack);
        return Text.translatable("tooltip.hdemise.soul_cost." + ((cost > SoulComponent.SOUL_PER_VESSEL * 2) ? "large" : (cost > SoulComponent.SOUL_PER_VESSEL) ? "medium" : cost == SoulComponent.SOUL_PER_VESSEL ? "one" : (cost >= SoulComponent.SOUL_PER_VESSEL/4) ? "small" : "tiny")).withColor(Colors.LIGHT_GRAY);
    }
    default void spendSoul(PlayerEntity player, ItemStack stack) {
        if (canAffordSoul(player, stack) && !player.isCreative())
            SoulComponent.of(player).addSoul(-getSoulCost(player, stack));
    }
}
