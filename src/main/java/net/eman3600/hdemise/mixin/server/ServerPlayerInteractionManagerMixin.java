package net.eman3600.hdemise.mixin.server;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow @Final protected ServerPlayerEntity player;

    @Inject(method = "setGameMode", at = @At("TAIL"))
    private void hdemise$setGameMode(GameMode gameMode, GameMode previousGameMode, CallbackInfo ci) {
        SoulComponent.of(player).updateAbilities(false);
    }
}
