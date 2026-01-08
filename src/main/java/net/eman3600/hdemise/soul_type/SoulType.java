package net.eman3600.hdemise.soul_type;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.util.SoulAttribute;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jspecify.annotations.Nullable;

import java.util.List;

public abstract class SoulType {

    private final MeterType meterType;
    private final Identifier id;
    private final List<SoulAttribute> modifiers;

    protected SoulType(MeterType meterType, Identifier id, SoulAttribute... attributes) {
        this.meterType = meterType;
        this.id = id;
        this.modifiers = List.of(attributes);
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
        return true;
    }

    public int getFocusRate() {
        return 4;
    }

    public int getFocusTicks() {
        return 20;
    }

    public abstract boolean canVanish();
    public abstract boolean isUndead();
    public abstract boolean burnsInDaylight();
    public abstract List<AugmentSpace> getAugments();

    public final void applyAttributes(PlayerEntity player) {
        for (SoulAttribute attribute : modifiers) {
            attribute.apply(player, id.withSuffixedPath("_soul"));
        }
    }
    public final void removeAttributes(PlayerEntity player) {
        for (SoulAttribute attribute : modifiers) {
            attribute.remove(player, id.withSuffixedPath("_soul"));
        }
    }

    /**
     * Triggers when the player finishes focusing.
     * @param player the player focusing
     * @param focusAmount the focus value (usually hp restored)
     * @return whether the player should continue focusing automatically
     */
    public boolean onFocus(PlayerEntity player, float focusAmount) {
        player.heal(focusAmount);
        player.setHealth(MathHelper.ceil(player.getHealth()));

        return player.getHealth() < SoulComponent.of(player).getMaxHealthWithAffliction();
    }

    public String getTranslationKey() {
        return getId().toTranslationKey("soul_type");
    }

    public ItemStack getDefaultSoulStack() {
        return ItemStack.EMPTY;
    }

    public List<SoulAttribute> getAttributes() {
        return this.modifiers;
    }


    public enum MeterType {
        HUNGER(true, false),
        SOUL(false, true),
        BOTH(true, true),
        NONE(false, false);

        public final boolean usesHunger;
        public final boolean usesSoul;

        MeterType(boolean usesHunger, boolean usesSoul) {
            this.usesHunger = usesHunger;
            this.usesSoul = usesSoul;
        }
    }
}
