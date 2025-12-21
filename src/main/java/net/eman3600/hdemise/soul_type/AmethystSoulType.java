package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.minecraft.entity.attribute.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

public class AmethystSoulType extends SoulType {

    public static final Identifier HP_ATTRIBUTE_ID = Identifier.of(MODID, "amethyst_hp");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst_container.png");


    public AmethystSoulType(Identifier id) {
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
        return false;
    }

    @Override
    public boolean burnsInDaylight() {
        return false;
    }


    @Override
    public void applyAttributes(AttributeContainer container) {


        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);
        if (hpInstance != null) {

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(HP_ATTRIBUTE_ID, -.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(HP_ATTRIBUTE_ID);
        }
    }
}
