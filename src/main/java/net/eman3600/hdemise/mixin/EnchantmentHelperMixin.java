package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
    @Inject(method = "getProtectionAmount", at = @At("RETURN"), cancellable = true)
    private static void hdemise$getProtectionAmount(ServerWorld world, LivingEntity user, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        SoulComponent sc = SoulComponent.of(user);

        if (sc != null && sc.isAfflicted()) {
            cir.setReturnValue(cir.getReturnValueF()/4);
        }
    }
}
