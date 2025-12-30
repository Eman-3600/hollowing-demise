package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class StalwartSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(3, 3, ModTags.Items.YELLOW_AUGMENT)
    );

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "stalwart_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/stalwart.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/stalwart_container.png");


    public StalwartSoulType(Identifier id) {
        super(MeterType.HUNGER, id);
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
    public List<AugmentSpace> getAugments() {
        return augments;
    }

    @Override
    public void applyAttributes(AttributeContainer container) {

        RegistryEntry<EntityAttribute> maxHP = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance maxHPInstance = container.getCustomInstance(maxHP);
        if (maxHPInstance != null) {

            maxHPInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, .4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }


        RegistryEntry<EntityAttribute> toughness = EntityAttributes.ARMOR_TOUGHNESS;
        EntityAttributeInstance toughnessInstance = container.getCustomInstance(toughness);
        if (toughnessInstance != null) {

            toughnessInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, 16, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> maxHP = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance maxHPInstance = container.getCustomInstance(maxHP);

        if (maxHPInstance != null) {

            maxHPInstance.removeModifier(ATTRIBUTE_ID);
        }


        RegistryEntry<EntityAttribute> toughness = EntityAttributes.ARMOR_TOUGHNESS;
        EntityAttributeInstance toughnessInstance = container.getCustomInstance(toughness);
        if (toughnessInstance != null) {

            toughnessInstance.removeModifier(ATTRIBUTE_ID);
        }
    }
}
