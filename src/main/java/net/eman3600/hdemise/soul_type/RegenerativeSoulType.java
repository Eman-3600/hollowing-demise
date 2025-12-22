package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

public class RegenerativeSoulType extends SoulType {

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "regenerative_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/regenerative.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/regenerative_container.png");


    public RegenerativeSoulType(Identifier id) {
        super(MeterType.NONE, id);
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

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }


        RegistryEntry<EntityAttribute> regen = ModAttributes.REGEN;
        EntityAttributeInstance regenInstance = container.getCustomInstance(regen);
        if (regenInstance != null) {

            regenInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, 1.25, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);

        if (hpInstance != null) {

            hpInstance.removeModifier(ATTRIBUTE_ID);
        }


        RegistryEntry<EntityAttribute> regen = ModAttributes.REGEN;
        EntityAttributeInstance regenInstance = container.getCustomInstance(regen);
        if (regenInstance != null) {

            regenInstance.removeModifier(ATTRIBUTE_ID);
        }
    }
}
