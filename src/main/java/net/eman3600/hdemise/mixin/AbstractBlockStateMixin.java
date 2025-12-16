package net.eman3600.hdemise.mixin;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin extends State<Block, BlockState> {


    protected AbstractBlockStateMixin(Block owner, Reference2ObjectArrayMap<Property<?>, Comparable<?>> propertyMap, MapCodec<BlockState> codec) {
        super(owner, propertyMap, codec);
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"), cancellable = true)
    private void hdemise$getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        VoxelShape blockShape = cir.getReturnValue();
        if(!blockShape.isEmpty() && context instanceof EntityShapeContext esc) {
            if (esc.getEntity() instanceof PlayerEntity player && SoulComponent.of(player).isGhost() && player.getAbilities().flying) {
                cir.setReturnValue(VoxelShapes.empty());
            }
        }
    }

    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void hdemise$onEntityCollision(World world, BlockPos pos, Entity entity, EntityCollisionHandler entityCollisionHandler, boolean bl, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player && SoulComponent.of(player).isGhost()) {
            ci.cancel();
        }
    }
}
