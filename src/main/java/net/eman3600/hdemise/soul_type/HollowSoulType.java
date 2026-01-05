package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class HollowSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of();

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/hollow.png");


    public HollowSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, -8, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -4, Operation.ADD_VALUE)
        );
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
    public List<AugmentSpace> getAugments() {
        return augments;
    }

    @Override
    public boolean hasExperience() {
        return false;
    }
}
