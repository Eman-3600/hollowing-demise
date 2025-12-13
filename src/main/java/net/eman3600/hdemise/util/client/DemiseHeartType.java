package net.eman3600.hdemise.util.client;

import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public enum DemiseHeartType {
    SOUL(Identifier.of(MODID,"textures/gui/hud/heart/soul_full.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_full_blinking.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_half.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_half_blinking.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_hardcore_full.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_hardcore_full_blinking.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_hardcore_half.png"),
            Identifier.of(MODID,"textures/gui/hud/heart/soul_hardcore_half_blinking.png"));

    private final Identifier fullTexture;
    private final Identifier fullBlinkingTexture;
    private final Identifier halfTexture;
    private final Identifier halfBlinkingTexture;
    private final Identifier hardcoreFullTexture;
    private final Identifier hardcoreFullBlinkingTexture;
    private final Identifier hardcoreHalfTexture;
    private final Identifier hardcoreHalfBlinkingTexture;

    private DemiseHeartType(Identifier fullTexture, Identifier fullBlinkingTexture, Identifier halfTexture, Identifier halfBlinkingTexture, Identifier hardcoreFullTexture, Identifier hardcoreFullBlinkingTexture, Identifier hardcoreHalfTexture, Identifier hardcoreHalfBlinkingTexture) {
        this.fullTexture = fullTexture;
        this.fullBlinkingTexture = fullBlinkingTexture;
        this.halfTexture = halfTexture;
        this.halfBlinkingTexture = halfBlinkingTexture;
        this.hardcoreFullTexture = hardcoreFullTexture;
        this.hardcoreFullBlinkingTexture = hardcoreFullBlinkingTexture;
        this.hardcoreHalfTexture = hardcoreHalfTexture;
        this.hardcoreHalfBlinkingTexture = hardcoreHalfBlinkingTexture;
    }

    public Identifier getTexture(boolean hardcore, boolean half, boolean blinking) {
        if (!hardcore) {
            if (half) {
                return blinking ? this.halfBlinkingTexture : this.halfTexture;
            }
            return blinking ? this.fullBlinkingTexture : this.fullTexture;
        }
        if (half) {
            return blinking ? this.hardcoreHalfBlinkingTexture : this.hardcoreHalfTexture;
        }
        return blinking ? this.hardcoreFullBlinkingTexture : this.hardcoreFullTexture;
    }
}
