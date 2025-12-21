package net.eman3600.hdemise.init.event;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModCallbacks {

    public static void registerCallbacks() {
        LOGGER.info("Registering Callbacks for " + MODID);


        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            SoulComponent sc = SoulComponent.of(newPlayer);

            if (!alive) {
                sc.onDeath();
            } else {
                sc.reloadAttributes();
                sc.updateAbilities(true);
            }
        });

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            SoulComponent sc = SoulComponent.of(handler.getPlayer());

            sc.reloadAttributes();
            sc.setFocusing(false);
            sc.updateAbilities(true);
        });
    }
}
