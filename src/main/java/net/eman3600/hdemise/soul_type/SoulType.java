package net.eman3600.hdemise.soul_type;

import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class SoulType {

    /**
     * The spritesheet that should be used for this soul type's hearts
     * @return the identifier path of the heart spritesheet
     */
    @Nullable
    public Identifier heartType() {
        return null;
    }

    public abstract boolean usesSoul();

    public boolean hasExperience() {
        return !usesSoul();
    }

    public abstract boolean canVanish();
    public abstract boolean isUndead();
    public abstract boolean burnsInDaylight();
}
