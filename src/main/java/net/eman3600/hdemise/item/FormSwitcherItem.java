package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
        if (sc.isDemon() && sc.canCure()) {
            if (world.isClient()) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
            } else {
                sc.setCuring(true, 20);
            }
        } else if (!sc.isDemon()) {
            if (world.isClient()) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
            } else {
                sc.setForm(true);
            }
        }

        return ActionResult.SUCCESS;
    }
}
