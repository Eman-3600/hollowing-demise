package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Bar.class)
public interface BarMixin {
    @Inject(method = "drawExperienceLevel", at = @At("HEAD"), cancellable = true)
    private static void hdemise$drawExperienceLevel(DrawContext context, TextRenderer textRenderer, int level, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(MinecraftClient.getInstance().player);

        if (sc != null && (sc.isGhost() || !sc.hasExperience())) {
            ci.cancel();
        }
    }
}
