package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.data_component.XPStorageComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class XPCoreItem extends Item {
    public XPCoreItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        SoulComponent sc = SoulComponent.of(user);
        ItemStack stack = user.getStackInHand(hand);
        XPStorageComponent component = stack.get(ModDataComponentTypes.XP_STORAGE);

        if (!sc.isDemon() && component != null) {
            if (!world.isClient()) {
                user.addExperience(component.totalXP());

                stack.decrementUnlessCreative(1, user);
            } else {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        XPStorageComponent component = stack.get(ModDataComponentTypes.XP_STORAGE);

        int displayLevel = 0;
        int displayPoints = 0;

        if (component != null) {
            displayLevel = component.displayLevel();
            displayPoints = component.displayPoints();
        }

        textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip.level", displayLevel));
        textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip.points", displayPoints));
    }

    public ItemStack extractPlayerExperience(PlayerEntity player) {
        XPStorageComponent component = XPStorageComponent.extractExperience(player);

        ItemStack stack = new ItemStack(this, 1);
        stack.set(ModDataComponentTypes.XP_STORAGE, component);

        return stack;
    }
}
