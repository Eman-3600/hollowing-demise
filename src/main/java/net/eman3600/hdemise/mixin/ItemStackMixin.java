package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.init.entity.ModDamageTypes;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {
    @Shadow public abstract ItemStack copy();

    @Shadow public abstract Item getItem();

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    private void hdemise$appendTooltip(Item.TooltipContext context, TooltipDisplayComponent displayComponent, @Nullable PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo ci) {
        Integer lines = get(ModDataComponentTypes.TOOLTIP_LINES);

        if (lines != null) {
            if (lines == 1) {
                textConsumer.accept(Text.translatable(getItem().getTranslationKey() + ".tooltip").withColor(Colors.LIGHT_GRAY));
            } else {
                for (int i = 0; i < lines; i++) {
                    textConsumer.accept(Text.translatable(getItem().getTranslationKey() + ".tooltip." + i).withColor(Colors.LIGHT_GRAY));
                }
            }
        }
    }

    @Inject(method = "takesDamageFrom", at = @At("HEAD"), cancellable = true)
    private void hdemise$takesDamageFrom(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOf(ModDamageTypes.VIGOR_FAILED)) {
            cir.setReturnValue(false);
        }
    }
}
