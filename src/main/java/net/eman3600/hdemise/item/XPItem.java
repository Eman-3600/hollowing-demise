package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class XPItem extends Item {

    private static final int XP_AMOUNT = 10;


    public XPItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient()) {
            user.addExperience(XP_AMOUNT);

            ItemStack stack = user.getStackInHand(hand);
            stack.decrementUnlessCreative(1, user);
        }

        return ActionResult.SUCCESS;
    }
}
