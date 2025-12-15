package net.eman3600.hdemise.mixin.client;

import com.mojang.authlib.GameProfile;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
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
}
