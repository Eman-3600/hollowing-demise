package net.eman3600.hdemise.mixin.client;

import com.mojang.authlib.GameProfile;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow public Input input;

    protected ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "getCrosshairTarget(FLnet/minecraft/entity/Entity;)Lnet/minecraft/util/hit/HitResult;", at = @At("HEAD"), cancellable = true)
    private void hdemise$getCrosshairTarget(float tickProgress, Entity cameraEntity, CallbackInfoReturnable<HitResult> cir) {
        if (cameraEntity == this && SoulComponent.of(this).lockedInteraction()) {
            Vec3d vec3d = this.getCameraPosVec(tickProgress);
            Vec3d vec3d2 = this.getRotationVec(tickProgress);
            double maxDistance = this.getBlockInteractionRange();
            Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
            cir.setReturnValue(BlockHitResult.createMissed(vec3d3, Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(vec3d3)));
        }
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void hdemise$pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        if (SoulComponent.of(this).isGhost()) {
            ci.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isCamera()Z"), cancellable = true)
    private void hdemise$tickMovement$cancelFlight(CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this);

        if (this.getAbilities().flying && sc.isGhost() && this.getGameMode() != null && this.getGameMode().isSurvivalLike()) {
            BlockPos blockPos = BlockPos.ofFloored(getX(), getEyeY(), getZ());
            if (!getEntityWorld().isSkyVisible(blockPos)) {
                return;
            }
            sc.ignoreGhostAbstrusion = true; // Yes this is cursed, but it works.
            double closestDistance = -100f;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Vec3d pos = getEyePos().add(i, 0, j);
                    Vec3d maxPos = getEntityPos().add(i, -25, j);
                    BlockHitResult hitResult = this.getEntityWorld()
                            .raycast(
                                    new RaycastContext(
                                            pos, maxPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, this
                                    )
                            );

                    double distance = hitResult.getBlockPos().getY() - getY();
                    if (distance > closestDistance) {
                        closestDistance = distance;
                    }
                }
            }

            sc.ignoreGhostAbstrusion = false;

            if (closestDistance < -10f) {


                if (closestDistance < -13f || this.input.playerInput.sneak()) {
                    this.setVelocity(this.getVelocity().add(0.0, -this.getAbilities().getFlySpeed() * (this.input.playerInput.sneak() ? 3.0F : closestDistance > -20F ? 0.2F : 1.5F), 0.0));
                }
                super.tickMovement();
                ci.cancel();
            }
        }
    }

//    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
//    private void hdemise$tickMovement$setVelocity(ClientPlayerEntity instance, Vec3d vec3d) {
//        SoulComponent sc = SoulComponent.of(this);
//
//        if (sc.isGhost() && this.getGameMode() != null && this.getGameMode().isSurvivalLike() && vec3d.y > 0) {
//            Vec3d pos = getEyePos();
//            Vec3d maxPos = getEntityPos().add(0, -2, 0);
//            sc.ignoreGhostAbstrusion = true; // Yes this is cursed, but it works.
//            BlockHitResult hitResult1 = this.getEntityWorld()
//                    .raycast(
//                            new RaycastContext(
//                                    pos, maxPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this
//                            )
//                    );
//            sc.ignoreGhostAbstrusion = false;
//
//            if (hitResult1.getType() == HitResult.Type.MISS) {
//
//                return;
//            }
//        }
//
//        instance.setVelocity(vec3d);
//    }
}
