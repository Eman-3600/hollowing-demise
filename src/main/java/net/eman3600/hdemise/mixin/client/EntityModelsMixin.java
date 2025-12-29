package net.eman3600.hdemise.mixin.client;

import com.google.common.collect.ImmutableMap;
import net.eman3600.hdemise.init.entity.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import net.minecraft.client.render.entity.model.ModelTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(EntityModels.class)
public abstract class EntityModelsMixin {

    @Inject(method = "getModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/TexturedModelData;of(Lnet/minecraft/client/model/ModelData;II)Lnet/minecraft/client/model/TexturedModelData;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void hdemise$getModels(CallbackInfoReturnable<Map<EntityModelLayer, TexturedModelData>> cir, ImmutableMap.Builder<EntityModelLayer, TexturedModelData> builder) {
        for (EntityModelLayer layer : ModEntityModelLayers.MODELS.keySet()) {
            builder.put(layer, ModEntityModelLayers.MODELS.get(layer).get());
        }
    }
}
