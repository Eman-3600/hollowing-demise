package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ConsumableCureItem extends Item {
    public ConsumableCureItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        SoulComponent sc = SoulComponent.of(user);

        if (sc.isDemon()) {
            if (!world.isClient()) {
                sc.setForm(false);

                ItemStack stack = user.getStackInHand(hand);
                stack.decrementUnlessCreative(1, user);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
