package net.eman3600.hdemise.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jspecify.annotations.Nullable;

public class RuneBlock extends Block {
    public static final MapCodec<RuneBlock> CODEC = RuneBlock.createCodec(RuneBlock::new);
    public static final BooleanProperty LIT = Properties.LIT;

    public RuneBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    @Override
    public MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {

            double d = pos.getX() - .1 + random.nextDouble() * 1.2;
            double e = pos.getY() - .1 + random.nextDouble() * 1.2;
            double f = pos.getZ() - .1 + random.nextDouble() * 1.2;
            world.addParticleClient(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.get(LIT)) {

            BlockPos blockPos = hit.getBlockPos();

            if (stack.getItem() instanceof FlintAndSteelItem) {
                world.playSound(player, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);

                world.setBlockState(blockPos, state.with(LIT, true));
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);

                player.getStackInHand(hand).damage(1, player, hand.getEquipmentSlot());

                return ActionResult.SUCCESS;
            } else if (stack.getItem() instanceof FireChargeItem) {
                Random random = world.getRandom();
                world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

                world.setBlockState(blockPos, state.with(LIT, true));
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);

                player.getStackInHand(hand).decrementUnlessCreative(1, player);

                return ActionResult.SUCCESS;
            }
        }

        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }
}
