package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class NegativeSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of();

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "negative_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/negative.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/negative_container.png");


    public NegativeSoulType(Identifier id) {
        super(MeterType.SOUL, id);
    }

    @Override
    public @Nullable Identifier heartType() {
        return HEART_TYPE;
    }

    @Override
    public @Nullable Identifier heartContainerType() {
        return HEART_CONTAINER_TYPE;
    }

    @Override
    public boolean canVanish() {
        return false;
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public boolean burnsInDaylight() {
        return false;
    }

    @Override
    public List<AugmentSpace> getAugments() {
        return augments;
    }

    @Override
    public boolean hasExperience() {
        return false;
    }

    @Override
    public void applyAttributes(AttributeContainer container) {


        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);
        if (hpInstance != null) {

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -10, EntityAttributeModifier.Operation.ADD_VALUE));
        }


        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);
        if (maxSoulInstance != null) {

            maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -8, EntityAttributeModifier.Operation.ADD_VALUE));
        }


        RegistryEntry<EntityAttribute> focusPower = ModAttributes.FOCUS_POWER;
        EntityAttributeInstance focusPowerInstance = container.getCustomInstance(focusPower);
        if (focusPowerInstance != null) {

            focusPowerInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -2, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(ATTRIBUTE_ID);
        }

        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);

        if (maxSoulInstance != null) {

            maxSoulInstance.removeModifier(ATTRIBUTE_ID);
        }

        RegistryEntry<EntityAttribute> focusPower = ModAttributes.FOCUS_POWER;
        EntityAttributeInstance focusPowerInstance = container.getCustomInstance(focusPower);
        if (focusPowerInstance != null) {

            focusPowerInstance.removeModifier(ATTRIBUTE_ID);
        }
    }
}
