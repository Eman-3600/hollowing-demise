package net.eman3600.hdemise.block.entity;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfusionTableBlockEntity extends BlockEntity {

    public static final int REQUIRED_SHELVES = 7;

    private int shelves = 0;
    private int ticks = 0;
    private int orbTicks = 0;
    private float orbPosition = 0f;
    private float orbRotation = 0f;

    private static final float ORB_MOVE_SPEED = 0.03f;
    private static final float ORB_ROTATION_SPEED = 0.025f;
    private static final int ORB_SIN_TICKS = 100;

    public InfusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);

        view.putInt("shelves", shelves);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);

        shelves = view.getInt("shelves", 0);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = super.toInitialChunkDataNbt(registries);

        nbt.putInt("shelves", shelves);

        return nbt;
    }

    public void attemptUse(PlayerEntity player) {
        updateShelves();

        if (!usable()) {
            player.sendMessage(Text.translatable("block.hdemise.infusion_table.not_enough_shelves", shelves, REQUIRED_SHELVES), true);
        } else {
            SoulComponent.of(player).validateSoulStack();
            player.openHandledScreen(getCachedState().createScreenHandlerFactory(getWorld(), getPos()));
        }
    }

    public float getOrbPosition() {
        return orbPosition;
    }

    public float getOrbRotation() {
        return orbRotation;
    }

    private float getOrbTargetPosition() {
        return usable() ? (float) (0.25f + Math.sin((double)orbTicks/ORB_SIN_TICKS * Math.PI * 2) * 0.1f) : 0f;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, InfusionTableBlockEntity blockEntity) {
        blockEntity.orbTicks = (blockEntity.orbTicks + 1) % ORB_SIN_TICKS;
        float targetPos = blockEntity.getOrbTargetPosition();

        if (blockEntity.orbPosition < targetPos) {
            blockEntity.orbPosition = Math.min(blockEntity.orbPosition + ORB_MOVE_SPEED, targetPos);
        } else {
            blockEntity.orbPosition = Math.max(blockEntity.orbPosition - ORB_MOVE_SPEED, targetPos);
        }

        if (blockEntity.usable()) {
            blockEntity.orbRotation += ORB_ROTATION_SPEED;

            while (blockEntity.orbRotation >= (float) Math.PI) {
                blockEntity.orbRotation -= (float) (Math.PI * 2);
            }

            while (blockEntity.orbRotation < (float) -Math.PI) {
                blockEntity.orbRotation += (float) (Math.PI * 2);
            }
        } else if (blockEntity.orbRotation > 0) {
            blockEntity.orbRotation = Math.max(blockEntity.orbRotation - (ORB_ROTATION_SPEED * 5), 0);
        } else if (blockEntity.orbRotation < 0) {
            blockEntity.orbRotation = Math.min(blockEntity.orbRotation + (ORB_ROTATION_SPEED * 5), 0);
        }

        blockEntity.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, InfusionTableBlockEntity blockEntity) {
        if (++blockEntity.ticks >= 30) {
            blockEntity.ticks = 0;

            blockEntity.updateShelves();
        }
    }

    public void updateShelves() {
        if (world == null) return;

        int s = 0;

        for (BlockPos offset : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
            if (EnchantingTableBlock.canAccessPowerProvider(world, pos, offset)) {
                s++;
            }
        }

        if (shelves != s) {
            shelves = s;
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public boolean usable() {
        return shelves >= REQUIRED_SHELVES;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }


}