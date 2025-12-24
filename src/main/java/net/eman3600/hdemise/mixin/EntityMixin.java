package net.eman3600.hdemise.mixin;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void hdemise$isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof PlayerEntity player) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.getSoulType() == ModSoulTypes.CONSTRUCT) {
                cir.setReturnValue(true);
            }
        }
    }
}
