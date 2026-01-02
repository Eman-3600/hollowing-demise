package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CrossItem extends Item {

    private static final int COOLDOWN = 200;
    private static final int BLOCK_DURATION = 120;
    private static final double RANGE = 4;

    public CrossItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            Box box = user.getBoundingBox().expand(RANGE);

            for (PlayerEntity player : world.getEntitiesByClass(PlayerEntity.class, box, e -> e != user)) {
                SoulComponent sc = SoulComponent.of(player);

                if (sc.getSoulType().canVanish()) {
                    player.addStatusEffect(new StatusEffectInstance(ModStatusEffects.BLOCKED, BLOCK_DURATION), user);

                    if (sc.isGhost()) {
                        sc.setGhost(false);
                    }
                }
            }

            ItemStack stack = user.getStackInHand(hand);
            stack.damage(1, user, hand.getEquipmentSlot());
            if (!user.isCreative()) {
                user.getItemCooldownManager().set(stack, COOLDOWN);
            }
        }

        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.PLAYERS, 2f, .5f);

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip").withColor(Colors.LIGHT_GRAY));
    }
}
