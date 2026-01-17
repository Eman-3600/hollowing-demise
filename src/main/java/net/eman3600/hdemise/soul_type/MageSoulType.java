package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

public class MageSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(43, 26, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(106, 26, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(15, 59, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(134, 59, ModTags.Items.GREEN_AUGMENT),
            new AugmentSpace(43, 89, ModTags.Items.RED_AUGMENT),
            new AugmentSpace(106, 89, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/mage.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/mage_container.png");


    public MageSoulType(Identifier id) {
        super(MeterType.BOTH, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, 0, EntityAttributeModifier.Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -3, EntityAttributeModifier.Operation.ADD_VALUE)
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
    public boolean hasHealingFocus() {
        return false;
    }

    @Override
    public int getFocusRate() {
        return 10;
    }

    @Override
    public int getFocusTicks() {
        return 16;
    }

    @Override
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SHIELD, 50 * (int)(focusAmount), 0, true, true));

        return false;
    }

    @Override
    public List<AugmentSpace> getAugments() {
        return augments;
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.MAGE_SOUL.getDefaultStack();
    }
}
