package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.mixin_interfaces.PlayerEntityRenderStateAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarlikeEntity extends PlayerLikeEntity & ClientPlayerLikeEntity>
        extends LivingEntityRenderer<AvatarlikeEntity, PlayerEntityRenderState, PlayerEntityModel> {
    protected PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("TAIL"))
    private void hdemise$updateRenderState(AvatarlikeEntity playerLikeEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (playerLikeEntity instanceof PlayerEntity player && playerEntityRenderState instanceof PlayerEntityRenderStateAccess access) {
            access.hdemise$updateWithSoulComponent(SoulComponent.of(player));
        }
    }

    @Inject(method = "shouldRenderFeatures(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Z", at = @At("HEAD"), cancellable = true)
    private void hdemise$shouldRenderFeatures(PlayerEntityRenderState playerEntityRenderState, CallbackInfoReturnable<Boolean> cir) {
        if (playerEntityRenderState instanceof PlayerEntityRenderStateAccess access && access.hdemise$isGhost()) {
            cir.setReturnValue(false);
        }
    }
}
