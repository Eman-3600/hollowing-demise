package net.eman3600.hdemise.event.callback;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface SoulInUseCallback {

    /**
     * Add to this event an essence regen modifier.
     * Regen rate is in essence/second
     */
    Event<SoulInUseCallback> EVENT = EventFactory.createArrayBacked(SoulInUseCallback.class, callbacks -> (player, sc) -> {

        for (SoulInUseCallback callback : callbacks) {
            if (callback.isUsingSoul(player, sc)) {
                return true;
            }
        }

        return false;
    });

    boolean isUsingSoul(PlayerEntity player, SoulComponent sc);
}
