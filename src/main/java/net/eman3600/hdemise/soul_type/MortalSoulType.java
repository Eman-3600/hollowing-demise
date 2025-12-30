package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.basics.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class MortalSoulType extends SoulType {
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
    public ItemStack getDefaultSoulStack() {
        return ModItems.PURE_SOUL.getDefaultStack();
    }
}
