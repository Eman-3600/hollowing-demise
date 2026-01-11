package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractWindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.World;

public class CorruptionAugmentItem extends AugmentItem implements FocusAugment {



    public CorruptionAugmentItem(Settings settings) {
        super(settings);
    }

    public CorruptionAugmentItem(Settings settings, int tooltipLines) {
        super(settings, tooltipLines);
    }

    @Override
    public void onFocus(PlayerEntity player, ItemStack stack, float focusPower) {

        SoulComponent.of(player).addCorruption(9);
    }
}
