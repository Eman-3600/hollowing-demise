package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MortalSoulType extends SoulType {

    private static final List<AugmentSpace> augments = List.of(
            new AugmentSpace(35, 3, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(9, 59, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(35, 113, ModTags.Items.YELLOW_AUGMENT),
            new AugmentSpace(115, 3, ModTags.Items.RED_AUGMENT),
            new AugmentSpace(141, 59, ModTags.Items.RED_AUGMENT),
            new AugmentSpace(115, 113, ModTags.Items.RED_AUGMENT)
    );


    public MortalSoulType(Identifier id) {
        super(MeterType.HUNGER, id);
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
    public ItemStack getDefaultSoulStack() {
        return ModItems.PURE_SOUL.getDefaultStack();
    }
}
