package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathProtectionComponent.class)
public abstract class DeathProtectionComponentMixin {

    @Inject(method = "applyDeathEffects", at = @At("HEAD"))
    private void hdemise$applyDeathEffects(ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            SoulComponent.of(entity).setCorruption(0);
            SoulComponent.of(entity).setAfflicted(false);
        }
    }
}
