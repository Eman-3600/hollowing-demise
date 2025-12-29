package net.eman3600.hdemise.block.entity.renderer;

import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.init.entity.ModEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.SpriteMapper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.MODID;

@Environment(EnvType.CLIENT)
public class InfusionTableBlockEntityRenderer implements BlockEntityRenderer<InfusionTableBlockEntity, InfusionTableBlockEntityRenderer.InfusionTableBlockEntityRenderState> {

    private final SpriteHolder materials;
    private final ModelPart pearl;
    public static final SpriteIdentifier PEARL_TEXTURE = TexturedRenderLayers.ENTITY_SPRITE_MAPPER.map(Identifier.of(MODID, "infusion_pearl"));

    public InfusionTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.materials = ctx.spriteHolder();
        this.pearl = ctx.getLayerModelPart(ModEntityModelLayers.INFUSION_PEARL);
    }


    public static TexturedModelData getPearlTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("pearl", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 16, 16);
    }


    @Override
    public InfusionTableBlockEntityRenderState createRenderState() {
        return new InfusionTableBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(InfusionTableBlockEntity blockEntity, InfusionTableBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.@Nullable CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);

        state.active = blockEntity.usable();
        state.orbPosition = blockEntity.getOrbPosition();
        state.orbRotation = blockEntity.getOrbRotation();
    }

    @Override
    public void render(InfusionTableBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.translate(.5, .875 + state.orbPosition, .5);
        matrices.multiply(new Quaternionf().rotationY(state.orbRotation));
        queue.submitModelPart(
                this.pearl,
                matrices,
                PEARL_TEXTURE.getRenderLayer(RenderLayers::entitySolid),
                state.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                this.materials.getSprite(PEARL_TEXTURE),
                -1,
                state.crumblingOverlay
        );
        matrices.pop();
    }

    public static class InfusionTableBlockEntityRenderState extends BlockEntityRenderState {
        public boolean active;
        public float orbPosition = 0f;
        public float orbRotation = 0f;
    }
}
