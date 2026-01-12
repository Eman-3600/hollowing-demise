package net.eman3600.hdemise.item;

import net.eman3600.hdemise.init.basics.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PixItem extends Item {
    public PixItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.tool(material, ModTags.Blocks.PIX_MINEABLE, attackDamage, attackSpeed, 5));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        if (world.getBlockState(blockPos).isOf(Blocks.OBSIDIAN)) {
            PlayerEntity playerEntity = context.getPlayer();
            world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClient()) {
                world.setBlockState(blockPos, Blocks.CRYING_OBSIDIAN.getDefaultState(), Block.NOTIFY_LISTENERS);
                if (playerEntity != null) {
                    playerEntity.getItemCooldownManager().set(context.getStack(), 100);
                    context.getStack().damage(8, playerEntity, context.getHand().getEquipmentSlot());
                }
            }

            return ActionResult.SUCCESS;
        }

        ActionResult result = super.useOnBlock(context);

        if (result == ActionResult.PASS) {
            return Items.DIAMOND_AXE.useOnBlock(context);
        }

        return result;
    }
}
