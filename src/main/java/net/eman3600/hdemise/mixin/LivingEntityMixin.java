package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModDamageTypes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.mixin_interfaces.LivingEntityAccess;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, ServerWaypoint, LivingEntityAccess {
    @Shadow protected boolean jumping;

    @Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow public abstract boolean damage(ServerWorld world, DamageSource source, float amount);

    @Shadow public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void hdemise$canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (effect.equals(StatusEffects.POISON) && (sc.isUndead() || sc.getSoulType() == ModSoulTypes.CONSTRUCT || sc.hasAugment(ModItems.AGELESS_WATCH))
                    || effect.equals(StatusEffects.HUNGER) && (!sc.usesHunger() || sc.hasAugment(ModItems.AGELESS_WATCH))
                    || (effect.equals(StatusEffects.WITHER) || effect.equals(StatusEffects.WEAKNESS)) && sc.hasAugment(ModItems.AGELESS_WATCH)) {
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
                cir.setReturnValue(Math.max(getFinalGravity()/2, sc.getLungeGravity()));
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

    @Inject(method = "getExperienceToDrop(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;)I", at = @At("RETURN"), cancellable = true)
    private void hdemise$getExperienceToDrop(ServerWorld world, Entity attacker, CallbackInfoReturnable<Integer> cir) {
        if (attacker instanceof PlayerEntity player) {
            int xp = cir.getReturnValueI();

            if (player.getStackInHand(Hand.MAIN_HAND).isIn(ModTags.Items.REAPER)) {
                cir.setReturnValue(xp * 2);
            }
        }
    }

    @Inject(method = "updatePotionVisibility", at = @At("HEAD"))
    private void hdemise$updatePotionVisibility(CallbackInfo ci) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            sc.setHasLightfoot(hasStatusEffect(ModStatusEffects.LIGHTFOOT));
        }
    }

    @Inject(method = "onStatusEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void hdemise$onStatusEffectsRemoved(Collection<StatusEffectInstance> effects, CallbackInfo ci, Iterator var2, StatusEffectInstance statusEffectInstance) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (statusEffectInstance.getEffectType() == ModStatusEffects.FLEETING_VIGOR && sc.isOnDeathsDoor() && getEntityWorld() instanceof ServerWorld world) {
                damage(world, world.getDamageSources().create(ModDamageTypes.VIGOR_FAILED), 100000);
            }
        }
    }

    @Inject(method = "onKilledBy", at = @At("HEAD"))
    private void hdemise$onKilledBy(LivingEntity adversary, CallbackInfo ci) {
        if (this.getEntityWorld() instanceof ServerWorld && adversary instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isOnDeathsDoor()) {
                sc.setOnDeathsDoor(false);
                player.removeStatusEffect(ModStatusEffects.FLEETING_VIGOR);
            }
        }
    }

    @Override
    public boolean hdemise$isJumping() {
        return this.jumping;
    }
}
