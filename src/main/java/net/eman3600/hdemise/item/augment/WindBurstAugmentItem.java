package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.util.RayHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractWindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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
                4F,
                false,
                World.ExplosionSourceType.TRIGGER,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.GUST_EMITTER_LARGE,
                Pool.empty(),
                SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST
        );
    }

    @Override
    public void duringFocusDisplay(PlayerEntity player, int progress) {
        Box box = player.getBoundingBox().expand(SoulComponent.MAGIC_FAN_RANGE/2);

        World world = player.getEntityWorld();
        Random random = player.getRandom();

        if (random.nextInt(3) == 0) {
            for (int i = 0; i < 4; i++) {

                Vec3d pos = RayHelper.randomPointWithin(box, random);

                world.addParticleClient(
                        ParticleTypes.GUST,
                        pos.x,
                        pos.y,
                        pos.z,
                        0,
                        0,
                        0
                );
            }
        }
    }
}
