package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class AmethystAppleItem extends Item {
    public AmethystAppleItem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        SoulComponent sc = SoulComponent.of(user);

        if (sc.isSoulless()) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        SoulComponent sc = SoulComponent.of(user);

        if (!world.isClient()) {
            sc.setSoulType(ModSoulTypes.CRYSTAL);
        } else {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
        }

        ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
        return consumableComponent != null ? consumableComponent.finishConsumption(world, user, stack) : stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip").withColor(Colors.LIGHT_GRAY));
    }
}
