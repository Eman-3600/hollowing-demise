package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
            new AugmentSpace(53, 81, ModTags.Items.RED_AUGMENT)
    );

    public static final Identifier HEART_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst.png");
    public static final Identifier HEART_CONTAINER_TYPE = Identifier.of(MODID,"textures/gui/hud/heart/amethyst_container.png");


    public CrystalSoulType(Identifier id) {
        super(MeterType.SOUL, id,
                new SoulAttribute(EntityAttributes.MAX_HEALTH, -2, EntityAttributeModifier.Operation.ADD_VALUE),
                new SoulAttribute(ModAttributes.MAX_SOUL, -1, EntityAttributeModifier.Operation.ADD_VALUE)
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
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0, true, true));

        return super.onFocus(player, focusAmount);
    }

    @Override
    public ItemStack getDefaultSoulStack() {
        return ModItems.CRYSTAL_SOUL.getDefaultStack();
    }
}
