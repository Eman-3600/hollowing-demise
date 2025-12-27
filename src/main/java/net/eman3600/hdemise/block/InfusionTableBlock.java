package net.eman3600.hdemise.block;

import com.mojang.serialization.MapCodec;
import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

public class InfusionTableBlock extends BlockWithEntity implements BlockEntityProvider {
    private static final VoxelShape SHAPE = Block.createColumnShape(16.0, 0.0, 12.0);
    public static final MapCodec<InfusionTableBlock> CODEC = InfusionTableBlock.createCodec(InfusionTableBlock::new);

    public InfusionTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfusionTableBlockEntity(pos, state);
    }

    @Override
    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? validateTicker(type, ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY, InfusionTableBlockEntity::clientTick) : validateTicker(type, ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY, InfusionTableBlockEntity::serverTick);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        for (BlockPos offset : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) == 0 && EnchantingTableBlock.canAccessPowerProvider(world, pos, offset)) {
                world.addParticleClient(
                        ParticleTypes.ENCHANT,
                        pos.getX() + 0.5,
                        pos.getY() + 2.0,
                        pos.getZ() + 0.5,
                        offset.getX() + random.nextFloat() - 0.5,
                        offset.getY() - random.nextFloat() - 1.0F,
                        offset.getZ() + random.nextFloat() - 0.5
                );
            }

            if (random.nextInt(8) == 0 && EnchantingTableBlock.canAccessPowerProvider(world, pos, offset)) {
                world.addParticleClient(
                        ParticleTypes.PORTAL,
                        pos.getX() + 0.5,
                        pos.getY() + 0.75,
                        pos.getZ() + 0.5,
                        offset.getX() + random.nextFloat() - 0.5,
                        offset.getY() - random.nextFloat() - 1.0F,
                        offset.getZ() + random.nextFloat() - 0.5
                );
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        InfusionTableBlockEntity blockEntity = world.getBlockEntity(pos) instanceof InfusionTableBlockEntity e ? e : null;
        if (blockEntity != null) {

            if (!world.isClient()) {
                blockEntity.attemptUse(player);
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hit);
    }
}
