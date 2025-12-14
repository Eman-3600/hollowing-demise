package net.eman3600.hdemise.event;

import net.eman3600.hdemise.networking.c2s.FocusPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyInputHandler {
    public static final String KEY_FOCUS = "key.hdemise.focus";

    public static KeyBinding focusKey;
    private static boolean holdingFocusKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (focusKey.isPressed() && client.player != null) {

                if (!holdingFocusKey) {
                    holdingFocusKey = true;
                    FocusPayload payload = new FocusPayload(true);
                    ClientPlayNetworking.send(payload);
//                    Vec2f movementInput = client.player.input.getMovementInput();
//                    EntityComponents.INFUSION.get(client.player)
//                            .tryDodgeClient(new Vec3d(movementInput.x, 0, movementInput.y));
                }
            } else if (holdingFocusKey) {
                holdingFocusKey = false;
                FocusPayload payload = new FocusPayload(false);
                ClientPlayNetworking.send(payload);
            }
        });
    }

    public static void registerBindings() {

        focusKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_FOCUS, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, KeyBinding.Category.GAMEPLAY));
    }
}
