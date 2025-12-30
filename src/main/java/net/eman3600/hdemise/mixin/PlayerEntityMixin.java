package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.mob_effects.ModStatusEffect;
import net.eman3600.hdemise.soul_type.MortalSoulType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends PlayerLikeEntity {
    @Shadow public abstract PlayerAbilities getAbilities();

    @Shadow @Final private PlayerAbilities abilities;

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract void addExperience(int experience);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "addExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addScore(I)V", shift = At.Shift.AFTER), cancellable = true)
    private void hdemise$addExperience(int experience, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.usesSoul() && ((experience > 0 && sc.getSoul() < sc.getMaxSoul()) || (experience < 0 && sc.getSoul() > 0))) {

            int remainingXP = (experience * SoulComponent.SOUL_PER_XP - ((experience > 0 ? sc.getMaxSoul() : 0) - sc.getSoul()))/SoulComponent.SOUL_PER_XP;
            sc.gainSoulFromXP(experience);

            ci.cancel();

            if (MathHelper.sign(experience) * remainingXP > 0) {
                addExperience(remainingXP);
            }
        }

        if (!sc.hasExperience()) {
            ci.cancel();
        }
    }

    @Inject(method = "canFoodHeal", at = @At(value = "HEAD"), cancellable = true)
    private void hdemise$canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (!sc.usesHunger()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canSprintOrFly", at = @At("RETURN"), cancellable = true)
    private void hdemise$canSprintOrFly(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.usesSoul() && !sc.usesHunger() && (sc.getSoul() <= SoulComponent.EXHAUSTION_THRESHOLD || sc.hasSolarSickness(true)) && !getAbilities().allowFlying) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void hdemise$isInvulnerableTo(ServerWorld world, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (((sc.getSoulType().canVanish() || sc.getSoulType() == ModSoulTypes.CONSTRUCT) && source.isIn(DamageTypeTags.IS_FALL))
                || (sc.isDrowningImmune()) && source.isIn(DamageTypeTags.IS_DROWNING)
                || (sc.isGhost() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))) {

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
        if (sc.isCuring()) {
            sc.interruptCure();
        }


        if (hasStatusEffect(ModStatusEffects.RAGE)) {
            ModStatusEffect.reduceDuration(this, ModStatusEffects.RAGE, ModStatusEffect.RAGE_REDUCTION_ON_HIT);
        }
    }

    @Inject(method = "isImmobile", at = @At("HEAD"), cancellable = true)
    private void hdemise$isImmobile(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this);

        if (sc.lockedMovement()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void hdemise$interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (SoulComponent.of(this).isGhost()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    // Common Side Ghost Locks

    @Inject(method = "collideWithEntity", at = @At("HEAD"), cancellable = true)
    private void hdemise$collideWithEntity(Entity entity, CallbackInfo ci) {
        if (SoulComponent.of(this).isGhost() && entity.getType() != EntityType.EXPERIENCE_ORB) {
            ci.cancel();
        }
    }

    @Inject(method = "getOffGroundSpeed", at = @At("HEAD"), cancellable = true)
    private void hdemise$getOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
        if (SoulComponent.of(this).isGhost() && this.abilities.flying && !this.hasVehicle() && this.isSprinting() && !this.isCreative()) {
            cir.setReturnValue(this.abilities.getFlySpeed() * 1.7F);
        }
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue((info.getReturnValue())
                .add(ModAttributes.MAX_SOUL, 10d)
                .add(ModAttributes.REGEN, 0d)
                .add(ModAttributes.FOCUS_POWER, 6d));
    }

    @Inject(method = "canConsume", at = @At("HEAD"), cancellable = true)
    private void hdemise$canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
        if (!SoulComponent.of(this).usesHunger() && !this.abilities.invulnerable) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldRenderName", at = @At("HEAD"), cancellable = true)
    private void hdemise$shouldRenderName(CallbackInfoReturnable<Boolean> cir) {
        if (SoulComponent.of(this).isGhost()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void hdemise$dropInventory(ServerWorld world, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        sc.getInventory().scatterAll((PlayerEntity)(Object)this);
    }
}
