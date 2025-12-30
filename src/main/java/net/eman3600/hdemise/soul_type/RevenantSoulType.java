package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

public class RevenantSoulType extends SoulType {

    public static final Identifier ATTRIBUTE_ID = Identifier.of(MODID, "rotten_soul");

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/rotten.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/rotten_container.png");


    public RevenantSoulType(Identifier id) {
        super(MeterType.BOTH, id);
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
    public int getFocusRate() {
        return 8;
    }

    @Override
    public int getFocusTicks() {
        return 10;
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

            maxSoulInstance.addTemporaryModifier(new EntityAttributeModifier(ATTRIBUTE_ID, -4, EntityAttributeModifier.Operation.ADD_VALUE));
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
        //player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 30 * (int)(focusAmount), 0, true, true));
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RAGE, 30 * (int)(focusAmount), 0, true, true));

        return false;
    }
}
