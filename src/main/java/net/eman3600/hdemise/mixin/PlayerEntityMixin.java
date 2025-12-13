package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends PlayerLikeEntity {
    @Shadow public abstract PlayerAbilities getAbilities();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "addExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addScore(I)V", shift = At.Shift.AFTER), cancellable = true)
    private void hdemise$addExperience(int experience, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.isDemon()) {
            sc.gainSoulFromXP(experience);
            ci.cancel();
        }
    }

    @Inject(method = "canFoodHeal", at = @At(value = "HEAD"), cancellable = true)
    private void hdemise$canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.isDemon()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canSprintOrFly", at = @At("RETURN"), cancellable = true)
    private void hdemise$canSprintOrFly(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.isDemon() && sc.getSoulPercentage() <= .1f && !getAbilities().allowFlying) {
            cir.setReturnValue(false);
        }
    }
}
