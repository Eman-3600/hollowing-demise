package net.eman3600.hdemise.soul_type;

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
}
