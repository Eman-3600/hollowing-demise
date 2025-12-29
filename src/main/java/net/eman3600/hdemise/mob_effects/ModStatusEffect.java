package net.eman3600.hdemise.mob_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;

public class ModStatusEffect extends StatusEffect {

    public static final int RAGE_REDUCTION_ON_HIT = 40;
    public static final int RAGE_REDUCTION_ON_LUNGE = 20;



    public ModStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public ModStatusEffect(StatusEffectCategory category, int color, ParticleEffect particleEffect) {
        super(category, color, particleEffect);
    }

    /**
     * Reduces an entity's status effect duration by a set amount
     * @param entity the target entity
     * @param effect the target status effect
     * @param amount the duration reduction
     */
    public static void reduceDuration(LivingEntity entity, RegistryEntry<StatusEffect> effect, int amount) {
        if (entity.hasStatusEffect(effect)) {
            StatusEffectInstance current = entity.getStatusEffect(effect);

            if (current.isInfinite()) return;

            int duration = current.getDuration() - amount;
            entity.removeStatusEffect(effect);

            if (duration > 0) {
                entity.addStatusEffect(current.withScaledDuration((float)duration / current.getDuration()));
            }
        }
    }
}
