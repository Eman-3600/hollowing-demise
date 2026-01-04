package net.eman3600.hdemise.item.augment;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class AttributeAugmentItem extends AugmentItem {
    private final List<AugmentAttribute> modifiers;

    public AttributeAugmentItem(Settings settings, int tooltipLines, List<AugmentAttribute> modifiers) {
        super(settings, tooltipLines);
        this.modifiers = modifiers;
    }

    public AttributeAugmentItem(Settings settings, List<AugmentAttribute> modifiers) {
        this(settings, 0, modifiers);
    }

    public AttributeAugmentItem(Settings settings, int tooltipLines, AugmentAttribute... attributes) {
        this(settings, tooltipLines, List.of(attributes));
    }

    public AttributeAugmentItem(Settings settings, AugmentAttribute... attributes) {
        this(settings, List.of(attributes));
    }

    @Override
    public void onReload(PlayerEntity player, ItemStack stack) {
        super.onReload(player, stack);

        for (AugmentAttribute attribute : modifiers) {
            attribute.apply(player, Registries.ITEM.getId(this));
        }
    }

    @Override
    public void onRemove(PlayerEntity player, ItemStack stack) {
        super.onRemove(player, stack);

        for (AugmentAttribute attribute : modifiers) {
            attribute.remove(player, Registries.ITEM.getId(this));
        }
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        if (!modifiers.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("tooltip.hdemise.augment_attributes").withColor(Colors.LIGHT_GRAY));

            for (AugmentAttribute modifier : modifiers) {
                double e;
                if (modifier.operation() == Operation.ADD_MULTIPLIED_BASE
                        || modifier.operation() == Operation.ADD_MULTIPLIED_TOTAL) {
                    e = modifier.value() * 100.0;
                } else if (modifier.attribute().matches(EntityAttributes.KNOCKBACK_RESISTANCE)) {
                    e = modifier.value() * 10.0;
                } else {
                    e = modifier.value();
                }

                if (e > 0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.plus." + modifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(modifier.attribute.value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute.value().getFormatting(true))
                    );
                } else if (e < 0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.take." + modifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(-e),
                                            Text.translatable(modifier.attribute.value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute.value().getFormatting(false))
                    );
                }
            }
        }
    }

    public record AugmentAttribute(RegistryEntry<EntityAttribute> attribute, double value,
                                   Operation operation) {

        public void apply(PlayerEntity player, Identifier id) {
            EntityAttributeInstance container = player.getAttributeInstance(attribute);

            if (container != null && id != null) {
                container.removeModifier(id);

                container.addTemporaryModifier(new EntityAttributeModifier(id, value, operation));
            }
        }

        public void remove(PlayerEntity player, Identifier id) {
            EntityAttributeInstance container = player.getAttributeInstance(attribute);

            if (container != null && id != null) {
                container.removeModifier(id);
            }
        }
    }
}
