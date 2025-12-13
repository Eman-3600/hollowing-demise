package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class FormSwitcherItem extends Item {
    public FormSwitcherItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        SoulComponent sc = SoulComponent.of(user);
        sc.setForm(!sc.isDemon());

        return ActionResult.SUCCESS;
    }
}
