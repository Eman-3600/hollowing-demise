package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class NightVisionAugmentItem extends AugmentItem implements FocusAugment {
    public NightVisionAugmentItem(Settings settings) {
        super(settings);
    }

    public NightVisionAugmentItem(Settings settings, int tooltipLines) {
        super(settings, tooltipLines);
    }

    @Override
    public void onFocus(PlayerEntity player, ItemStack stack, float focusPower) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2400, 0, true, true));
    }
}
