package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class SoulType {

    private final MeterType meterType;
    private final Identifier id;

    protected SoulType(MeterType meterType, Identifier id) {
        this.meterType = meterType;
        this.id = id;
    }

    public static SoulType ofID(Identifier id) {
        if (!SoulTypeRegistry.REGISTRY.containsId(id)) {
            return ModSoulTypes.MORTAL;
        }
        return SoulTypeRegistry.REGISTRY.get(id);
    }

    public static SoulType ofSerializable(String id) {
        return ofID(Identifier.tryParse(id));
    }

    public Identifier getId() {
        return this.id;
    }

    public String getSerializable() {
        return this.id.toString();
    }

    /**
     * The spritesheet that should be used for this soul type's hearts
     * @return the identifier path of the heart spritesheet
     */
    @Nullable
    public Identifier heartType() {
        return null;
    }

    /**
     * The spritesheet that should be used for this soul type's heart container
     * @return the identifier path of the heart container spritesheet
     */
    @Nullable
    public Identifier heartContainerType() {
        return null;
    }

    public boolean usesSoul() {
        return meterType.usesSoul;
    }

    public boolean usesHunger() {
        return meterType.usesHunger;
    }

    public boolean hasExperience() {
        return !usesSoul();
    }

    public abstract boolean canVanish();
    public abstract boolean isUndead();
    public abstract boolean burnsInDaylight();

    public final void applyAttributes(PlayerEntity player) {
        applyAttributes(player.getAttributes());
    }
    public final void removeAttributes(PlayerEntity player) {
        removeAttributes(player.getAttributes());
    }

    public void applyAttributes(AttributeContainer container) {}
    public void removeAttributes(AttributeContainer container) {}


    public enum MeterType {
        HUNGER(true, false),
        SOUL(false, true),
        NONE(false, false);

        public final boolean usesHunger;
        public final boolean usesSoul;

        MeterType(boolean usesHunger, boolean usesSoul) {
            this.usesHunger = usesHunger;
            this.usesSoul = usesSoul;
        }
    }
}
