package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.ModAttributes;
import net.eman3600.hdemise.util.client.DemiseHeartType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.eman3600.hdemise.HDemise.MODID;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @Shadow @Final private Random random;
    @Shadow private int ticks;
    @Unique
    private static final Identifier HUD_ICONS = Identifier.of(MODID, "textures/gui/hud/icons.png");
    @Unique
    private static final Identifier GHOST_VIGNETTE_TEXTURE = Identifier.of(MODID, "textures/misc/ghost_vignette.png");
    @Unique
    private static final Identifier SOLAR_VIGNETTE_TEXTURE = Identifier.of(MODID, "textures/misc/solar_vignette.png");



    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);
        if (sc.isDemon()) {

            if (sc.isGhost()) {
                right -= 51;
            }

            int v = sc.getWarning() > 0 ? 72 : (sc.isGhost() && sc.isVanishing() && sc.hasSolarSickness(false)) ? 36 : (sc.isGhost() != sc.isVanishing()) ? 54 : sc.hasSolarSickness(true) ? 36 : 18;

            int soulPerVessel = SoulComponent.SOUL_PER_VESSEL;
            int vessels = Math.max((int)player.getAttributeValue(ModAttributes.MAX_SOUL), 1);
            int soul = sc.getSoul();

            for (int j = 0; j < vessels; j++) {
                int l = right - (j % 10) * 8 - 9;
                int k = top - (j/10) * 10;
                boolean rightmost = (j % 10) == 0;
                boolean leftmost = (j % 10) == 9 || j == vessels - 1;
                int u = rightmost && leftmost ? 27 : rightmost ? 18 : leftmost ? 0 : 9;

                int fill = MathHelper.clamp(soul - j * soulPerVessel, 0, soulPerVessel);

                if (sc.getSoul() <= SoulComponent.EXHAUSTION_THRESHOLD || sc.hasSolarSickness(true)) {
                    k += this.random.nextInt(3) - 1;
                } else if ((sc.isFocusing() || sc.isVanishing()) && this.random.nextInt(4) == 0) {
                    k += this.random.nextInt(3) - 1;
                } else if ((sc.getSoulVessels() <= 3f || sc.isGhost()) && this.ticks % (int)(sc.getSoulVessels() * (sc.isGhost() ? 6 : 12) + 2) == 0) {
                    k += this.random.nextInt(3) - 1;
                }

                context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, l, k, u, v, 9, 9, 256, 256);
                if (fill > 0 || (j % 10 != 0 && soul - j * soulPerVessel >= soulPerVessel)) {
                    int pixels = 1 + (int)(8 * (float)fill/soulPerVessel);
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, l + (9 - pixels), k, u + (9 - pixels), v + 9, pixels, 9, 256, 256);
                }
            }

//            int pixels = (int)(81 * sc.getSoulPercentage());
//            context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, left, top, 0, 0, 81, 9, 256, 256);
//            context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, right - pixels, top, 81 - pixels, 9, pixels, 9, 256, 256);


            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.isGhost()) {

                context.drawTexture(
                        RenderPipelines.VIGNETTE,
                        GHOST_VIGNETTE_TEXTURE,
                        0,
                        0,
                        0.0F,
                        0.0F,
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight(),
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight(),
                        -1
                );

                ci.cancel();
            } else if (sc.sunTicks > 0) {
                float h = (float)sc.sunTicks / SoulComponent.SUN_TICKS;
                context.drawTexture(
                        RenderPipelines.VIGNETTE,
                        SOLAR_VIGNETTE_TEXTURE,
                        0,
                        0,
                        0.0F,
                        0.0F,
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight(),
                        context.getScaledWindowWidth(),
                        context.getScaledWindowHeight(),
                        ColorHelper.fromFloats(1.0F, h, h, h)
                );

                // ci.cancel();
            }
        }

    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc.shouldHideInteraction()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderAirBubbles(DrawContext context, PlayerEntity player, int heartCount, int top, int left, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc.isDemon()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc.shouldHideInteraction()) {
            ci.cancel();
        }
    }

    @Inject(method = "getCurrentBarType", at = @At("RETURN"), cancellable = true)
    private void hdemise$getCurrentBarType(CallbackInfoReturnable<InGameHud.BarType> cir) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (cir.getReturnValue() == InGameHud.BarType.EXPERIENCE && sc != null && sc.isDemon()) {
            cir.setReturnValue(InGameHud.BarType.EMPTY);
        }
    }

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void hdemise$drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc.isGhost()) {
            ci.cancel();
        } else if (sc.isDemon() && type == InGameHud.HeartType.NORMAL) {

            context.drawTexture(RenderPipelines.GUI_TEXTURED, DemiseHeartType.SOUL.getTexture(hardcore, half, blinking), x, y, 0, 0, 9, 9, 9, 9);
            ci.cancel();
        }
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void hdemise$renderArmor(DrawContext context, PlayerEntity player, int y, int i, int healthBarLines, int x, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);
        if (sc.isGhost()) {
            ci.cancel();
        }
    }
}
