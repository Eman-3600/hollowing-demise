package net.eman3600.hdemise.block.entity;

import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfusionTableBlockEntity extends BlockEntity {

    public static final int REQUIRED_SHELVES = 7;

    private int shelves = 0;
    private int ticks = 0;
    private float orbPosition = 0f;
    private float orbRotation = 0f;

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

    public void attemptUse(PlayerEntity player) {
        updateShelves();

        if (shelves < REQUIRED_SHELVES) {
            player.sendMessage(Text.translatable("block.hdemise.infusion_table.not_enough_shelves", shelves, REQUIRED_SHELVES), true);
        }
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, InfusionTableBlockEntity blockEntity) {

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
        }
    }


}