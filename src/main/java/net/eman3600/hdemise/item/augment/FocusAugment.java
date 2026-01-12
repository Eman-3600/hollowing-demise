package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public interface FocusAugment {
    void onFocus(PlayerEntity player, ItemStack stack, float focusPower);
    default SoulEventPayload.SoulEventType displayEvent() {
        return null;
    }
    default void duringFocusDisplay(PlayerEntity player, int progress) {}
}
