package net.eman3600.hdemise.init;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<SoulComponent> SOUL = ComponentRegistry.getOrCreate(Identifier.of(MODID, "soul"), SoulComponent.class);


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SOUL, SoulComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
