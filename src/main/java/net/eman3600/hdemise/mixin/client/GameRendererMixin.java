package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.mixin_interfaces.GameRendererAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.world.waypoint.TrackedWaypoint;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin implements GameRendererAccess, TrackedWaypoint.PitchProvider, AutoCloseable {
    @Shadow public abstract @Nullable Identifier getPostProcessorId();

    @Shadow protected abstract void setPostProcessor(Identifier id);

    @Override
    public Identifier hdemise$getPostProcessor() {
        return this.getPostProcessorId();
    }

    @Override
    public void hdemise$setPostProcessor(Identifier id) {
        this.setPostProcessor(id);
    }
}
