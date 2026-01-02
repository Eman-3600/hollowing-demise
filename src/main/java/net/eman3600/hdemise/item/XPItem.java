package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class XPItem extends Item {

    private static final int XP_AMOUNT = 20;
    private static final int COOLDOWN = 60;


    public XPItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient()) {
            user.addExperience(XP_AMOUNT);

            ItemStack stack = user.getStackInHand(hand);

            if (!user.isCreative()) {
                user.getItemCooldownManager().set(stack, COOLDOWN);
            }

            stack.decrementUnlessCreative(1, user);
        } else {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
        }

        return ActionResult.SUCCESS;
    }
}
