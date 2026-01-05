package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class RevenantSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(93, 35, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(125, 27, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(64, 100, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(35, 24, ModTags.Items.RED_AUGMENT),
            new AugmentSpace(15, 61, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/revenant.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/revenant_container.png");
    public static final Identifier RAGE_HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/rage.png");


    public RevenantSoulType(Identifier id) {
        super(MeterType.BOTH, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, -2, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -4, Operation.ADD_VALUE),
                new SoulAttribute(EntityAttributes.SAFE_FALL_DISTANCE, 2, Operation.ADD_VALUE)
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
    public int getFocusRate() {
        return 8;
    }

    @Override
    public int getFocusTicks() {
        return 10;
    }

    @Override
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        //player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 30 * (int)(focusAmount), 0, true, true));
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.RAGE, 30 * (int)(focusAmount), 0, true, true));

        return false;
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.REVENANT_SOUL.getDefaultStack();
    }
}
