package net.eman3600.hdemise.item.augment;

import net.eman3600.hdemise.util.SoulAttribute;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.List;
import java.util.function.Consumer;

public class AttributeAugmentItem extends AugmentItem {
    private final List<SoulAttribute> modifiers;

    public AttributeAugmentItem(Settings settings, int tooltipLines, List<SoulAttribute> modifiers) {
        super(settings, tooltipLines);
        this.modifiers = modifiers;
    }

    public AttributeAugmentItem(Settings settings, List<SoulAttribute> modifiers) {
        this(settings, 0, modifiers);
    }

    public AttributeAugmentItem(Settings settings, int tooltipLines, SoulAttribute... attributes) {
        this(settings, tooltipLines, List.of(attributes));
    }

    public AttributeAugmentItem(Settings settings, SoulAttribute... attributes) {
        this(settings, List.of(attributes));
    }

    @Override
    public void onReload(PlayerEntity player, ItemStack stack) {
        super.onReload(player, stack);

        for (SoulAttribute attribute : modifiers) {
            attribute.apply(player, Registries.ITEM.getId(this));
        }
    }

    @Override
    public void onRemove(PlayerEntity player, ItemStack stack) {
        super.onRemove(player, stack);

        for (SoulAttribute attribute : modifiers) {
            attribute.remove(player, Registries.ITEM.getId(this));
        }
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        if (!modifiers.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("tooltip.hdemise.augment_attributes").withColor(Colors.LIGHT_GRAY));

            for (SoulAttribute modifier : modifiers) {
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
                                            Text.translatable(modifier.attribute().value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute().value().getFormatting(true))
                    );
                } else if (e < 0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.take." + modifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(-e),
                                            Text.translatable(modifier.attribute().value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute().value().getFormatting(false))
                    );
                }
            }
        }
    }


}
