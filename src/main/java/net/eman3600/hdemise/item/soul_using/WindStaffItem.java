package net.eman3600.hdemise.item.soul_using;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WindChargeItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class WindStaffItem extends WindChargeItem implements SoulCostItem {
    public WindStaffItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        ItemStack stack = user.getStackInHand(hand);

        if (canAffordSoul(user, stack)) {
            if (world instanceof ServerWorld serverWorld) {
                ProjectileEntity.spawnWithVelocity(
                        (world2, shooter, s) -> new WindChargeEntity(user, world, user.getEntityPos().getX(), user.getEyePos().getY(), user.getEntityPos().getZ()),
                        serverWorld,
                        stack,
                        user,
                        0.0F,
                        POWER,
                        1.0F
                );
            }

            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.ENTITY_WIND_CHARGE_THROW,
                    SoundCategory.NEUTRAL,
                    0.5F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            spendSoul(user, stack);
            stack.damage(1, user, hand.getEquipmentSlot());
            return ActionResult.SUCCESS;
        }


        return ActionResult.PASS;
    }

    @Override
    public int getBaseSoulCost() {
        return 20;
    }
}
