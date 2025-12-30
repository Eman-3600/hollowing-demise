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

public class ConstructSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(3, 3, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(21, 3, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(3, 21, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(3, 39, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "construct_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/construct.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/construct_container.png");


    public ConstructSoulType(Identifier id) {
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
    public List<AugmentSpace> getAugments() {
        return augments;
    }

    @Override
    public int getFocusRate() {
        return 8;
    }

    @Override
    public int getFocusTicks() {
        return 10;
    }

    @Override
    public boolean onFocus(PlayerEntity player, float focusAmount) {

        return super.onFocus(player, focusAmount/2);
    }

    @Override
    public void applyAttributes(AttributeContainer container) {

        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);
        if (maxSoulInstance != null) {

            maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -5, EntityAttributeModifier.Operation.ADD_VALUE));
        }


        RegistryEntry<EntityAttribute> toughness = EntityAttributes.ARMOR_TOUGHNESS;
        EntityAttributeInstance toughnessInstance = container.getCustomInstance(toughness);
        if (toughnessInstance != null) {

            toughnessInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, 8, EntityAttributeModifier.Operation.ADD_VALUE));
        }


        RegistryEntry<EntityAttribute> focusPower = ModAttributes.FOCUS_POWER;
        EntityAttributeInstance focusPowerInstance = container.getCustomInstance(focusPower);
        if (focusPowerInstance != null) {

            focusPowerInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -2, EntityAttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void removeAttributes(AttributeContainer container) {
        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);

        if (maxSoulInstance != null) {

            maxSoulInstance.removeModifier(ATTRIBUTE_ID);
        }


        RegistryEntry<EntityAttribute> toughness = EntityAttributes.ARMOR_TOUGHNESS;
        EntityAttributeInstance toughnessInstance = container.getCustomInstance(toughness);
        if (toughnessInstance != null) {

            toughnessInstance.removeModifier(ATTRIBUTE_ID);
        }


        RegistryEntry<EntityAttribute> focusPower = ModAttributes.FOCUS_POWER;
        EntityAttributeInstance focusPowerInstance = container.getCustomInstance(focusPower);
        if (focusPowerInstance != null) {

            focusPowerInstance.removeModifier(ATTRIBUTE_ID);
        }
    }
}
