package net.eman3600.hdemise.item.soul_using;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public interface SoulCostItem {
    int getBaseSoulCost();

    default int getSoulCost(ItemStack stack) {
        return getBaseSoulCost();
    }

    default boolean canAffordSoul(PlayerEntity player, ItemStack stack) {
        if (player != null)
            return SoulComponent.of(player).getSoul() >= getSoulCost(stack) || player.isCreative();
        return false;
    }
    default Text getTooltipSoul(ItemStack stack) {
        return Text.translatable("tooltip.hdemise.soul_cost", getSoulCost(stack)).withColor(Colors.CYAN);
    }
    default void spendSoul(PlayerEntity player, ItemStack stack) {
        if (canAffordSoul(player, stack) && !player.isCreative())
            SoulComponent.of(player).addSoul(-getSoulCost(stack));
    }
}
