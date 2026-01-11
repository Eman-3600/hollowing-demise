package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractWindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.World;

public class WindBurstAugmentItem extends AugmentItem implements FocusAugment {



    public WindBurstAugmentItem(Settings settings) {
        super(settings);
    }

    public WindBurstAugmentItem(Settings settings, int tooltipLines) {
        super(settings, tooltipLines);
    }

    @Override
    public void onFocus(PlayerEntity player, ItemStack stack, float focusPower) {

        player.getEntityWorld().createExplosion(
                player,
                null,
                AbstractWindChargeEntity.EXPLOSION_BEHAVIOR,
                player.getX(),
                player.getY() + player.getHeight() / 2.0F,
                player.getZ(),
                6F,
                false,
                World.ExplosionSourceType.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                Pool.empty(),
                SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
        );
    }
}
