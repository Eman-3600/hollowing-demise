package net.eman3600.hdemise.event;

import net.eman3600.hdemise.networking.c2s.FocusPayload;
import net.eman3600.hdemise.networking.c2s.GhostPayload;
import net.eman3600.hdemise.networking.c2s.AirJumpPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyInputHandler {
    public static final String KEY_FOCUS = "key.hdemise.focus";
    public static final String KEY_GHOST = "key.hdemise.ghost";

    public static KeyBinding focusKey;
    public static KeyBinding ghostKey;

    private static boolean holdingFocusKey;
    private static boolean holdingGhostKey;
    private static boolean holdingJumpKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (focusKey.isPressed() && client.player != null) {

                if (!holdingFocusKey) {
                    holdingFocusKey = true;
                    FocusPayload payload = new FocusPayload(true);
                    ClientPlayNetworking.send(payload);
                }
            } else if (holdingFocusKey) {
                holdingFocusKey = false;
                FocusPayload payload = new FocusPayload(false);
                ClientPlayNetworking.send(payload);
            }

            if (ghostKey.isPressed() && client.player != null) {

                if (!holdingGhostKey) {
                    holdingGhostKey = true;
                    GhostPayload payload = new GhostPayload(true);
                    ClientPlayNetworking.send(payload);
                }
            } else if (holdingGhostKey) {
                holdingGhostKey = false;
                GhostPayload payload = new GhostPayload(false);
                ClientPlayNetworking.send(payload);
            }

            if (MinecraftClient.getInstance().options.jumpKey.isPressed()) {
                if (!holdingJumpKey && MinecraftClient.getInstance().player != null) {
                    holdingJumpKey = true;
                    if (!MinecraftClient.getInstance().player.isOnGround())
                        ClientPlayNetworking.send(new AirJumpPayload(true));
                }
            } else if (holdingJumpKey) {
                holdingJumpKey = false;
                ClientPlayNetworking.send(new AirJumpPayload(false));
            }

        });
    }

    public static void registerBindings() {

        focusKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_FOCUS, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.Category.GAMEPLAY));
        ghostKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_GHOST, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KeyBinding.Category.GAMEPLAY));
    }
}
