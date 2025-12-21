package net.eman3600.hdemise.soul_type;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static net.eman3600.hdemise.HDemise.MODID;

public class SoulTypeRegistry {
    public static final Identifier ID = Identifier.of(MODID, "ritual");
    public static final RegistryKey<Registry<SoulType>> KEY = RegistryKey.ofRegistry(ID);

    public static final Registry<SoulType> REGISTRY = new SimpleRegistry<>(KEY, Lifecycle.stable(), false);

    public static SoulType register(String id, Function<Identifier, SoulType> builder) {
        return register(Identifier.of(MODID, id), builder);
    }

    public static SoulType register(Identifier id, Function<Identifier, SoulType> builder) {
        SoulType soulType = builder.apply(id);
        Registry.register(REGISTRY, id, soulType);
        return soulType;
    }
}
