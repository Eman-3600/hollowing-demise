package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.mixin_interfaces.LivingEntityAccess;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ServerWaypoint, LivingEntityAccess {
    @Shadow protected boolean jumping;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void hdemise$canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (((sc.isUndead() || sc.getSoulType() == ModSoulTypes.CONSTRUCT) && effect.equals(StatusEffects.POISON))
                    || (!sc.usesHunger() && effect.equals(StatusEffects.HUNGER))) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void hdemise$tickMovement(CallbackInfo ci) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.shouldFreeze()) {
                this.setVelocity(this.getVelocity().multiply(0.6));
            }
        }
    }

    @Inject(method = "getEffectiveGravity", at = @At("HEAD"), cancellable = true)
    private void hdemise$getEffectiveGravity(CallbackInfoReturnable<Double> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.shouldFreeze()) {
                cir.setReturnValue(0d);
            } else if (sc.isLunging()) {
                cir.setReturnValue(Math.max(getFinalGravity(), sc.getLungeGravity()));
            }
        }
    }

    @Inject(method = "hasNoDrag", at = @At("HEAD"), cancellable = true)
    private void hdemise$hasNoDrag(CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isLunging()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void hdemise$pushAwayFrom(Entity entity, CallbackInfo ci) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isGhost()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "pushAway", at = @At("HEAD"), cancellable = true)
    private void hdemise$pushAway(Entity entity, CallbackInfo ci) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isGhost()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "canBreatheInWater", at = @At("HEAD"), cancellable = true)
    private void hdemise$canBreatheInWater(CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isDrowningImmune()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "hasInvertedHealingAndHarm", at = @At("HEAD"), cancellable = true)
    private void hdemise$hasInvertedHealingAndHarm(CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isUndead()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Override
    public boolean hdemise$isJumping() {
        return this.jumping;
    }
}
