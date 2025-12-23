package net.eman3600.hdemise.init.entity;

import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.init.basics.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModBlockEntities {

    public static final BlockEntityType<InfusionTableBlockEntity> INFUSION_TABLE_BLOCK_ENTITY =
            register( "infusion_table_block_entity", InfusionTableBlockEntity::new, ModBlocks.INFUSION_TABLE);

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<T> entityFactory, Block... blocks) {
        Identifier id = Identifier.of(MODID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(entityFactory, blocks).build());
    }

    public static void registerAll() {
        LOGGER.info("Registering Block Entities for " + MODID);
    }
}
