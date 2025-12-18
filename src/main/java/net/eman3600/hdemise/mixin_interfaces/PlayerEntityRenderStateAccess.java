package net.eman3600.hdemise.mixin_interfaces;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface PlayerEntityRenderStateAccess {
    boolean hdemise$isDemon();

    boolean hdemise$isGhost();

    void hdemise$updateWithSoulComponent(SoulComponent sc);
}
