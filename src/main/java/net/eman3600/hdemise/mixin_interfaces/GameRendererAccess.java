package net.eman3600.hdemise.mixin_interfaces;

import net.minecraft.util.Identifier;

public interface GameRendererAccess {
    void hdemise$setPostProcessor(Identifier id);
    Identifier hdemise$getPostProcessor();
}
