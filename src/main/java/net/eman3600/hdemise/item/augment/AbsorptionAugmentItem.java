package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AbsorptionAugmentItem extends AugmentItem implements TopUpAugment {
    public AbsorptionAugmentItem(Settings settings) {
        super(settings);
    }

    public AbsorptionAugmentItem(Settings settings, int tooltipLines) {
        super(settings, tooltipLines);
    }

    @Override
    public void onTopUp(PlayerEntity player, ItemStack stack) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, -1, 3, true, true));
    }

    @Override
    public void onRemove(PlayerEntity player, ItemStack stack) {
        super.onRemove(player, stack);

        player.removeStatusEffect(StatusEffects.ABSORPTION);
    }
}
