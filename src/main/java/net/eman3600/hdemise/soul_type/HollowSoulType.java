package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

public class HollowSoulType extends SoulType {

    public static final Identifier HP_ATTRIBUTE_ID = Identifier.of(MODID, "hollow_hp");
    public static final Identifier MAX_SOUL_ATTRIBUTE_ID = Identifier.of(MODID, "hollow_soul_max");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/hollow.png");


    public HollowSoulType(Identifier id) {
        super(MeterType.SOUL, id);
    }

    @Override
    public @Nullable Identifier heartType() {
        return HEART_TYPE;
    }

    @Override
    public boolean canVanish() {
        return true;
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public boolean burnsInDaylight() {
        return true;
    }


    @Override
    public void applyAttributes(AttributeContainer container) {


        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);
        if (hpInstance != null) {

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(HP_ATTRIBUTE_ID, -.4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }


        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);
        if (maxSoulInstance != null) {

            maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(MAX_SOUL_ATTRIBUTE_ID, -2, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(HP_ATTRIBUTE_ID);
        }

        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);

        if (maxSoulInstance != null) {

            maxSoulInstance.removeModifier(MAX_SOUL_ATTRIBUTE_ID);
        }
    }
}
