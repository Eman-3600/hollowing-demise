package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

public class PhantomSoulType extends SoulType {

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "phantom_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/phantom.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/phantom_container.png");


    public PhantomSoulType(Identifier id) {
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

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -.4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(ATTRIBUTE_ID);
        }
    }
}
