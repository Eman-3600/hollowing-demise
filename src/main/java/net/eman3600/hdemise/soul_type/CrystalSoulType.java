package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class CrystalSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(96, 4, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(130, 38, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(96, 38, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(40, 38, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(96, 94, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(5, 81, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "crystal_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst_container.png");


    public CrystalSoulType(Identifier id) {
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
    public void applyAttributes(AttributeContainer container) {

        RegistryEntry<EntityAttribute> hp = EntityAttributes.MAX_HEALTH;
        EntityAttributeInstance hpInstance = container.getCustomInstance(hp);
        if (hpInstance != null) {

            hpInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -.1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }


        RegistryEntry<EntityAttribute> maxSoul = ModAttributes.MAX_SOUL;
        EntityAttributeInstance maxSoulInstance = container.getCustomInstance(maxSoul);
        if (maxSoulInstance != null) {

            maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -2, EntityAttributeModifier.Operation.ADD_VALUE));
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
    }

    @Override
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0, true, true));

        return super.onFocus(player, focusAmount);
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.CRYSTAL_SOUL.getDefaultStack();
    }
}
