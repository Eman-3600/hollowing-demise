package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
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
    public SoulEventPayload.SoulEventType displayEvent() {
        return SoulEventPayload.SoulEventType.WITHER;
    }
}
