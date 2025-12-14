package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Shadow private float exhaustion;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void hdemise$update(ServerPlayerEntity player, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);

        if (sc.isDemon() && this.exhaustion > 4f) {
            this.exhaustion -= 4f;

            ci.cancel();
        }
    }
}
