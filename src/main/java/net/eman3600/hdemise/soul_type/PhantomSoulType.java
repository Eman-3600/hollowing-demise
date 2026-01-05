package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class PhantomSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(46, 17, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(104, 17, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(46, 80, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(104, 80, ModTags.Items.GREEN_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/phantom.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/phantom_container.png");


    public PhantomSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, -6, Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -2, Operation.ADD_VALUE)
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
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0, true, true));
        return super.onFocus(player, focusAmount);
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.PHANTOM_SOUL.getDefaultStack();
    }
}
