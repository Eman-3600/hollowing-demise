package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public interface FocusAugment {
    void onFocus(PlayerEntity player, ItemStack stack, float focusPower);
    default void displayFocus(PlayerEntity viewer, Vec3d pos) {}
}
