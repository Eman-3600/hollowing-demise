package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.eman3600.hdemise.HDemise.MODID;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin {

    private static final Identifier HUD_ICONS = Identifier.of(MODID, "textures/gui/hud/icons.png");



    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);
        if (sc.isDemon()) {

            int left = right - 81;
            int pixels = (int)(81 * sc.getSoulPercentage());
//            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, 256, 256, 0, 0, left, top, 81, 9, -1);
//            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, 256, 256, 0, 9, left, top, pixels, 9, -1);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, left, top, 0, 0, 81, 9, 256, 256);
            context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, right - pixels, top, 81 - pixels, 9, pixels, 9, 256, 256);


            ci.cancel();
        }
    }
}
