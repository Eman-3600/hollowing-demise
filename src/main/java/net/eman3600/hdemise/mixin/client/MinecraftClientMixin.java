package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    protected MinecraftClientMixin(String string) {
        super(string);
    }

//    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
//    private void hdemise$handleInputEvents(CallbackInfo ci) {
//        SoulComponent sc = SoulComponent.of(this.player);
//
//        if (sc.lockedMovement()) {
//            while (this.options.attackKey.wasPressed());
//            while (this.options.useKey.wasPressed());
//            while (this.options.pickItemKey.wasPressed());
//            while (this.options.jumpKey.wasPressed());
//        }
//    }

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void hdemise$handleInputEvents(CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(this.player);

        if (sc.lockedInteraction()) {
            for (int i = 0; i < 9; i++) {
                while(this.options.hotbarKeys[i].wasPressed()) hdemise$nothing();
            }
            while (this.options.inventoryKey.wasPressed()) hdemise$nothing();
            while (this.options.swapHandsKey.wasPressed()) hdemise$nothing();
            while (this.options.dropKey.wasPressed()) hdemise$nothing();
            while (this.options.useKey.wasPressed()) hdemise$nothing();
            this.options.useKey.setPressed(false);
        }
    }

    @Unique
    private void hdemise$nothing() {

    }

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void hdemise$doAttack(CallbackInfoReturnable<Boolean> cir) {
        SoulComponent sc = SoulComponent.of(this.player);

        if (sc.lockedInteraction()) {
            cir.setReturnValue(false);
        }
    }
}
