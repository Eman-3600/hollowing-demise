package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class EnderSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(28, 19, ModTags.Items.WILD_AUGMENT),
            new AugmentSpace(121, 19, ModTags.Items.WILD_AUGMENT),
            new AugmentSpace(28, 98, ModTags.Items.WILD_AUGMENT),
            new AugmentSpace(121, 98, ModTags.Items.WILD_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/ender.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/ender_container.png");


    public EnderSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, 0, EntityAttributeModifier.Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, 0, EntityAttributeModifier.Operation.ADD_VALUE)
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
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        return super.onFocus(player, focusAmount * 2);
    }

    @Override
    public int getFocusRate() {
        return super.getFocusRate() * 3;
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.ENDER_SOUL.getDefaultStack();
    }
}
