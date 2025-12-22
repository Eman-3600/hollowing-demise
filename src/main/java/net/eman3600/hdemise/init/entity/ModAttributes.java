package net.eman3600.hdemise.init.entity;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModAttributes {
    /**
     * The number of soul vessels an entity has.
     */
    public static final RegistryEntry<EntityAttribute> MAX_SOUL = register(
            "max_soul", new ClampedEntityAttribute("attribute.hdemise.name.max_soul", 0.0, 0.0, 30.0).setTracked(true)
    );
    public static final RegistryEntry<EntityAttribute> REGEN = register(
            "regen", new ClampedEntityAttribute("attribute.hdemise.name.regen", 0.0, 0.0, 100.0).setTracked(true)
    );


    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MODID, id), attribute);
    }

    public static void registerAttributes() {
        System.out.println("Registering entity attributes for " + MODID);
    }
}
