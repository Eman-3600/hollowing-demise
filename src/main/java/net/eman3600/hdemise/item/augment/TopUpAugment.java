package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface TopUpAugment {
    void onTopUp(PlayerEntity player, ItemStack stack);
}
