package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
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

        if (sc.isDemon() && (sc.getSoul() <= SoulComponent.EXHAUSTION_THRESHOLD || sc.hasSolarSickness()) && !getAbilities().allowFlying) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void hdemise$isInvulnerableTo(ServerWorld world, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.isDemon() && (source.isIn(DamageTypeTags.IS_FALL))) {
            cir.setReturnValue(true);
        } else if (sc.isGhost() && (!source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;)V"))
    private void hdemise$applyDamage(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.isFocusing()) {
            sc.setFocusing(false);
            sc.warnSoul();
        }
        if (sc.isVanishing()) {
            sc.interruptVanish();
        }
    }

    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void hdemise$isImmobile(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.lockedMovement()) {
            cir.setReturnValue(true);
        }
    }
}
