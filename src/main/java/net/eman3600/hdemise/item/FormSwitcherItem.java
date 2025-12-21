package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.soul_type.SoulType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import static net.eman3600.hdemise.init.basics.ModItems.EXPERIENCE_CORE;

public class FormSwitcherItem extends Item {
    public FormSwitcherItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        SoulComponent sc = SoulComponent.of(user);
        if (world.isClient()) {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), sc.isSoulless() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE : SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
        } else {
            SoulType currentType = sc.getSoulType();

            if (currentType == ModSoulTypes.MORTAL) {
                sc.setSoulType(ModSoulTypes.HOLLOW);
            } else if (currentType == ModSoulTypes.HOLLOW) {
                sc.setSoulType(ModSoulTypes.AMETHYST);
            } else {
                sc.setSoulType(ModSoulTypes.MORTAL);
            }
        }

        return ActionResult.SUCCESS;
    }
}
