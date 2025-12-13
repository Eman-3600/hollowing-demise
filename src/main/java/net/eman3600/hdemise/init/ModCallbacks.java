package net.eman3600.hdemise.init;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class ModCallbacks {

    public static void registerCallbacks() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            SoulComponent sc = SoulComponent.of(newPlayer);
            if (sc.isDemon()) {
                sc.resetSoul();
            }
        });
    }
}
