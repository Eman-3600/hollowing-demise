package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class ConstructSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(43, 27, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(107, 27, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(75, 10, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(75, 81, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/construct.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/construct_container.png");


    public ConstructSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, 0, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -5, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.FOCUS_POWER, -2, Operation.ADD_VALUE),
                new SoulAttribute(EntityAttributes.SAFE_FALL_DISTANCE, 4, Operation.ADD_VALUE),
                new SoulAttribute(EntityAttributes.FALL_DAMAGE_MULTIPLIER, -.5, Operation.ADD_MULTIPLIED_BASE)
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
    public ItemStack getDefaultSoulStack() {
        return ModItems.CONSTRUCT_SOUL.getDefaultStack();
    }
}
