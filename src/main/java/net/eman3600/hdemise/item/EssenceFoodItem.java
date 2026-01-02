package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class EssenceFoodItem extends Item {

    private final int essenceAmount;
    private static final int COOLDOWN = 100;


    public EssenceFoodItem(Settings settings, int essenceAmount) {
        super(settings);
        this.essenceAmount = essenceAmount;
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
        user.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (user instanceof ServerPlayerEntity player) {
            SoulComponent.of(player).addSoul(this.essenceAmount);
            if (!player.isCreative()) {
                player.getItemCooldownManager().set(stack, COOLDOWN);
            }
        }

        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.PLAYERS, 1.2f, .9f + .35f * world.getRandom().nextFloat());

        ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
        return consumableComponent != null ? consumableComponent.finishConsumption(world, user, stack) : stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip").withColor(Colors.LIGHT_GRAY));
    }
}
