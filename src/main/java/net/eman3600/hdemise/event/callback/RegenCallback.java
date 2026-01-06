package net.eman3600.hdemise.event.callback;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface RegenCallback {

    /**
     * Add to this event a health and food regen modifier.
     * Regen rate is in HP/second
     */
    Event<RegenCallback> EVENT = EventFactory.createArrayBacked(RegenCallback.class, callbacks -> (player, sc) -> {
        float rate = 0;

        for (RegenCallback callback : callbacks) {
            rate += callback.getRegenRate(player, sc);
        }

        return rate;
    });

    float getRegenRate(ServerPlayerEntity player, SoulComponent sc);
}
