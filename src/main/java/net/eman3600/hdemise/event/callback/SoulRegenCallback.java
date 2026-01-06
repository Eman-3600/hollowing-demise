package net.eman3600.hdemise.event.callback;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface SoulRegenCallback {

    /**
     * Add to this event an essence regen modifier.
     * Regen rate is in essence/second
     */
    Event<SoulRegenCallback> EVENT = EventFactory.createArrayBacked(SoulRegenCallback.class, callbacks -> (player, sc) -> {
        float rate = 0;

        for (SoulRegenCallback callback : callbacks) {
            rate += callback.getRegenRate(player, sc);
        }

        return rate;
    });

    float getRegenRate(ServerPlayerEntity player, SoulComponent sc);
}
