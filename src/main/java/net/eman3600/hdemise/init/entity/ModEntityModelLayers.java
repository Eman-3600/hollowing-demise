package net.eman3600.hdemise.init.entity;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.block.entity.renderer.InfusionTableBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.eman3600.hdemise.HDemise.MODID;

@Environment(EnvType.CLIENT)
public class ModEntityModelLayers {

    public static final Map<EntityModelLayer, Supplier<TexturedModelData>> MODELS = new HashMap<>();



    public static final EntityModelLayer INFUSION_PEARL = register("infusion_table", "pearl");



    private static EntityModelLayer registerMain(String id) {
        return register(id, "main");
    }

    private static EntityModelLayer register(String id, String layer) {
        return create(id, layer);
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(Identifier.of(MODID, id), layer);
    }

    private static void registerModel(EntityModelLayer layer, Supplier<TexturedModelData> data) {
        MODELS.put(layer, data);
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering entity model layers for " + MODID);

        // Call registerModel here
        registerModel(INFUSION_PEARL, InfusionTableBlockEntityRenderer::getPearlTexturedModelData);
    }
}
