package net.eman3600.hdemise.block;

import com.mojang.serialization.MapCodec;
import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class InfusionTableBlock extends BlockWithEntity implements BlockEntityProvider {
    private static final VoxelShape SHAPE = Block.createColumnShape(16.0, 0.0, 12.0);
    public static final MapCodec<InfusionTableBlock> CODEC = InfusionTableBlock.createCodec(InfusionTableBlock::new);

    private static final Text CONTAINER_TITLE = Text.translatable("container.hdemise.infusion");

    public static final List<BlockPos> POWER_PROVIDER_OFFSETS = BlockPos.stream(-2, -1, -2, 2, 1, 2)
            .filter(pos -> Math.abs(pos.getX()) == 2 || Math.abs(pos.getZ()) == 2)
            .map(BlockPos::toImmutable)
            .collect(Collectors.toUnmodifiableList());

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

        for (BlockPos offset : POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) == 0 && canAccessPowerProvider(world, pos, offset)) {
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

            if (random.nextInt(8) == 0 && canAccessPowerProvider(world, pos, offset)) {
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

    public static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
        BlockState state = world.getBlockState(tablePos.add(providerOffset));
        return state.isOf(ModBlocks.RUNIC_OBSIDIAN) && state.get(Properties.LIT);
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

    @Nullable
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof InfusionTableBlockEntity) {
            return new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, player) -> new InfusionScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), CONTAINER_TITLE
            );
        } else {
            return null;
        }
    }
}
