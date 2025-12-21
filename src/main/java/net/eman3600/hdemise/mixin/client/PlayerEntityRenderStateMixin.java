package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.mixin_interfaces.PlayerEntityRenderStateAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin extends BipedEntityRenderState implements PlayerEntityRenderStateAccess {
    @Unique private boolean isDemon = false;
    @Unique private boolean isGhost = false;

    public boolean hdemise$isDemon() {
        return isDemon;
    }

    public boolean hdemise$isGhost() {
        return isGhost;
    }

    public void hdemise$updateWithSoulComponent(SoulComponent sc) {
        this.isDemon = sc.isSoulless();
        this.isGhost = sc.isGhost();
    }
}
