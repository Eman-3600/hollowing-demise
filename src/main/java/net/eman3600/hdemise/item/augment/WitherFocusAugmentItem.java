package net.eman3600.hdemise.item.augment;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class WitherFocusAugmentItem extends AugmentItem implements FocusAugment {


    public WitherFocusAugmentItem(Settings settings) {
        super(settings);
    }

    public WitherFocusAugmentItem(Settings settings, int tooltipLines) {
        super(settings, tooltipLines);
    }

    @Override
    public void onFocus(PlayerEntity player, ItemStack stack, float focusPower) {
        Box box = player.getBoundingBox().expand(6, 1, 6);

        for (LivingEntity target : player.getEntityWorld().getEntitiesByClass(LivingEntity.class, box, e -> e != player && !player.isTeammate(e))) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 2), player);
        }
    }

    @Override
    public void displayFocus(PlayerEntity viewer, Vec3d pos) {
        Box box = viewer.getBoundingBox().expand(6, 1, 6);
        World world = viewer.getEntityWorld();
        Random random = viewer.getRandom();

        for (int i = 0; i < 250; i++) {
            world.addParticleClient(
                    ParticleTypes.SQUID_INK,
                    box.minX + (box.maxX - box.minX) * random.nextFloat(),
                    box.minY + (box.maxY - box.minY) * random.nextFloat(),
                    box.minZ + (box.maxZ - box.minZ) * random.nextFloat(),
                    0,
                    0,
                    0
            );
        }

        final double speed = .8d;

        for (int i = 0; i < 100; i++) {
            world.addParticleClient(ParticleTypes.SMOKE,
                    pos.x,
                    pos.y + .5,
                    pos.z,
                    (random.nextFloat() - .5f) * speed,
                    (random.nextFloat() - .5f) * speed,
                    (random.nextFloat() - .5f) * speed);
        }
    }
}
