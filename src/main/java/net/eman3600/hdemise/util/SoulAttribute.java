package net.eman3600.hdemise.util;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public record SoulAttribute(RegistryEntry<EntityAttribute> attribute, double value,
                            EntityAttributeModifier.Operation operation) {

    public void apply(PlayerEntity player, Identifier id) {
        EntityAttributeInstance container = player.getAttributeInstance(attribute);

        if (container != null && id != null) {
            container.removeModifier(id);

            container.addTemporaryModifier(new EntityAttributeModifier(id, value, operation));
        }
    }

    public void remove(PlayerEntity player, Identifier id) {
        EntityAttributeInstance container = player.getAttributeInstance(attribute);

        if (container != null && id != null) {
            container.removeModifier(id);
        }
    }
}