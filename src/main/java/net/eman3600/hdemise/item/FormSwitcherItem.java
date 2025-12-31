package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.soul_type.SoulType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        if (sc.isSoulless() && sc.canCure()) {
            if (!world.isClient()) {
                sc.setCuring(true, 20);
            }
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
        } else if (!sc.isSoulless()) {
            if (!world.isClient()) {
                sc.validateSoulStack();
                sc.setSoulType(ModSoulTypes.HOLLOW);
                sc.topUp();
                sc.validateSoulStack();
            }
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
        }

        return ActionResult.SUCCESS;
    }
}
