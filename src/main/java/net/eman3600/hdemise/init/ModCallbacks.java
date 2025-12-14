package net.eman3600.hdemise.init;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ModCallbacks {

    public static void registerCallbacks() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            SoulComponent sc = SoulComponent.of(newPlayer);

            if (!alive) {
                sc.setForm(true);
            } else if (sc.isDemon()) {
                sc.reloadAttributes();
            }
        });

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            SoulComponent sc = SoulComponent.of(handler.getPlayer());

            sc.reloadAttributes();
            sc.setFocusing(false);
        });
    }
}
