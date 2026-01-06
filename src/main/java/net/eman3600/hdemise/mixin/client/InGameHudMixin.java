package net.eman3600.hdemise.mixin.client;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.mixin_interfaces.PlayerEntityAccess;
import net.eman3600.hdemise.soul_type.RevenantSoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.debug.DebugHudEntries;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private static Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE;
    @Shadow @Final private static Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
    @Unique
    private static final Identifier HUD_ICONS = Identifier.of(MODID, "textures/gui/hud/icons.png");
    @Unique
    private static final Identifier CROSSHAIR_METRONOME = Identifier.of(MODID, "textures/gui/hud/crosshair_metronome.png");
    @Unique
    private static final Identifier GHOST_VIGNETTE_TEXTURE = Identifier.of(MODID, "textures/misc/ghost_vignette.png");
    @Unique
    private static final Identifier SOLAR_VIGNETTE_TEXTURE = Identifier.of(MODID, "textures/misc/solar_vignette.png");
    @Unique
    private static final Identifier CURE_VIGNETTE_TEXTURE = Identifier.of(MODID, "textures/misc/cure_vignette.png");



    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);

        if (sc.usesHunger() && !sc.isGhost()) {
            top -= 10;
            right -= 1;
        } else {
            ci.cancel();
        }

        if (sc.usesSoul()) {

            int vessels = Math.max((int)player.getAttributeValue(ModAttributes.MAX_SOUL), 1);

            if (sc.isGhost()) {
                right -= 51 + 4 * Math.max(0, 10 - vessels);
            }

            int v = sc.isCuring() ? 90
                    : sc.getWarning() > 0 ? 72
                    : (sc.isGhost() && sc.isVanishing() && sc.hasSolarSickness(false)) ? 36
                    : (sc.isGhost() != sc.isVanishing()) ? 54
                    : sc.hasSolarSickness(true) ? 36
                    : 18;

            int soulPerVessel = SoulComponent.SOUL_PER_VESSEL;

            int soul = sc.getSoul();
            boolean drawChains = player.hasStatusEffect(ModStatusEffects.CHAINED);

            for (int j = 0; j < vessels; j++) {
                int l = right - (j % 10) * 8 - 9;
                int k = top - (j/10) * 10;
                boolean rightmost = (j % 10) == 0;
                boolean leftmost = (j % 10) == 9 || j == vessels - 1;
                int u = rightmost && leftmost ? 27 : rightmost ? 18 : leftmost ? 0 : 9;

                int fill = MathHelper.clamp(soul - j * soulPerVessel, 0, soulPerVessel);

                if ((sc.getSoul() <= SoulComponent.EXHAUSTION_THRESHOLD && !sc.usesHunger()) || sc.hasSolarSickness(true)) {
                    k += this.random.nextInt(3) - 1;
                } else if ((sc.isFocusing() || sc.isVanishing() || sc.isJetting()) && this.random.nextInt(4) == 0) {
                    k += this.random.nextInt(3) - 1;
                } else if (((sc.getSoulVessels() <= 3f && !sc.usesHunger()) || sc.isGhost()) && this.ticks % (int)(sc.getSoulVessels() * (sc.isGhost() ? 6 : 12) + 2) == 0) {
                    k += this.random.nextInt(3) - 1;
                } else if (sc.getWarning() >= SoulComponent.WARNING_TICKS - 1) {
                    k += this.random.nextInt(3) - 1;
                }

                context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, l, k, u, v, 9, 9, 256, 256);
                if (fill > 0 || (j % 10 != 0 && soul - j * soulPerVessel >= soulPerVessel)) {
                    int pixels = 1 + (int)(8 * (float)fill/soulPerVessel);
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, l + (9 - pixels), k, u + (9 - pixels), v + 9, pixels, 9, 256, 256);
                }
                if (drawChains) {
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, HUD_ICONS, l, k, u, 126, 9, 9, 256, 256);
                }
            }
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
            } else if (sc.cureRenderTicks > 0) {
                float h = (float)sc.cureRenderTicks / SoulComponent.CURE_RENDER_TICKS;
                context.drawTexture(
                        RenderPipelines.VIGNETTE,
                        CURE_VIGNETTE_TEXTURE,
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
        if (sc != null && sc.shouldHideInteraction()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderCrosshair", at = @At("TAIL"))
    private void hdemise$renderCrosshair$metronome(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        PlayerEntity player = this.client.player;
        SoulComponent sc = SoulComponent.of(player);
        if (sc != null && !player.isSpectator() && !this.client.debugHudEntryList.isEntryVisible(DebugHudEntries.THREE_DIMENSIONAL_CROSSHAIR) && player instanceof PlayerEntityAccess access && sc.hasAugment(ModItems.METRONOME) && PlayerEntityAccess.isInMetronomeWindow(access.hdemise$getTicksSinceLastAttack(), player.getAttackCooldownProgressPerTick(), false)) {
            int j = context.getScaledWindowHeight() / 2 - 7 + 16;
            int k = context.getScaledWindowWidth() / 2 - 8;

            if (!(this.client.player.getAttackCooldownProgressPerTick() > 5.0F && this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR && this.client.targetedEntity instanceof LivingEntity && this.client.targetedEntity.isAlive())) {
                context.drawGuiTexture(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, k, j, 16, 4);
                context.drawGuiTexture(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 16, 4, 0, 0, k, j, 16, 4);
            }

            context.drawTexture(RenderPipelines.CROSSHAIR, CROSSHAIR_METRONOME, k, j, 0, 0, 16, 16, 16, 16);

        }
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    private void hdemise$renderAirBubbles(DrawContext context, PlayerEntity player, int heartCount, int top, int left, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc.isDrowningImmune()) {
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
        if (cir.getReturnValue() == InGameHud.BarType.EXPERIENCE && sc != null && (!sc.hasExperience() || sc.isGhost())) {
            cir.setReturnValue(InGameHud.BarType.EMPTY);
        }
    }

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void hdemise$drawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(getCameraPlayer());
        if (sc == null) return;
        Identifier heartTexture = sc.getSoulType().heartType();
        Identifier heartContainerTexture = sc.getSoulType().heartContainerType();
        if (sc.isGhost()) {
            ci.cancel();
        } else {
            if (heartTexture != null && type == InGameHud.HeartType.NORMAL) {
                if (sc.getSoulType() == ModSoulTypes.REVENANT && getCameraPlayer().hasStatusEffect(ModStatusEffects.RAGE)) {
                    heartTexture = RevenantSoulType.RAGE_HEART_TYPE;
                }

                hdemise$drawCustomHeart(context, heartTexture, x, y, hardcore, blinking, half, false);
                ci.cancel();
            }

            if (heartContainerTexture != null && type == InGameHud.HeartType.CONTAINER) {
                if (sc.getSoulType() == ModSoulTypes.REVENANT && getCameraPlayer().hasStatusEffect(ModStatusEffects.RAGE)) {
                    blinking = ticks/2 % 2 == 0;
                }

                hdemise$drawCustomHeart(context, heartContainerTexture, x, y, false, blinking, false, true);
                ci.cancel();
            }
        }
    }

    @Unique
    private void hdemise$drawCustomHeart(DrawContext context, Identifier texture, int x, int y, boolean hardcore, boolean blinking, boolean half, boolean container) {
        int u = (hardcore ? 36 : 0) + (half ? 18 : 0) + (blinking ? 9 : 0);

        context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, x, y, u, 0, 9, 9, container ? 18 : 72, 9);
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void hdemise$renderArmor(DrawContext context, PlayerEntity player, int y, int i, int healthBarLines, int x, CallbackInfo ci) {
        SoulComponent sc = SoulComponent.of(player);
        if (sc.isGhost()) {
            ci.cancel();
        }
    }
}
