package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.data_component.XPStorageComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModDataComponentTypes {

    public static final ComponentType<XPStorageComponent> XP_STORAGE = register(
            "xp_storage", builder -> builder.codec(XPStorageComponent.CODEC).packetCodec(XPStorageComponent.PACKET_CODEC).cache()
    );

    public static final ComponentType<NbtComponent> SOUL = register("soul", builder -> builder.codec(NbtComponent.CODEC));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MODID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering Data Component Types for " + MODID);
    }
}
