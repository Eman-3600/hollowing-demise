package net.eman3600.hdemise.block.entity;

import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class InfusionTableBlockEntity extends BlockEntity {

    public InfusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY, pos, state);
    }
}
