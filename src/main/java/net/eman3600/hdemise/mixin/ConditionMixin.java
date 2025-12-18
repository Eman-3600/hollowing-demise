package net.eman3600.hdemise.mixin;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BedRule.Condition.class)
public abstract class ConditionMixin implements StringIdentifiable {

    @Redirect(method = "test", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isNight()Z"))
    private boolean hdemise$isNight(World instance) {
        return true;
    }
}
