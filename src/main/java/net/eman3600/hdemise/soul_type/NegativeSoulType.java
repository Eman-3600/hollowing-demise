package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class NegativeSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of();

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/negative.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/negative_container.png");


    public NegativeSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, -10, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -7, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.FOCUS_POWER, -2, Operation.ADD_VALUE)
        );
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
    public ItemStack getDefaultSoulStack() {
        return ModItems.ANTISOUL.getDefaultStack();
    }
}
