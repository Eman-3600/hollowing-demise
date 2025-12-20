package net.eman3600.hdemise.mixin.server;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.EntityLookupView;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements EntityLookupView, StructureWorldAccess {

    @Unique
    private boolean isDayTime() {
        return isDay() & this.properties.getTimeOfDay() % 24000L < 12000L;
    }

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"), index = 0)
    private long hdemise$setTime(long ignored) {
        long t = isDay() ? 12000L : 24000L;
        long timeOfDay = this.properties.getTimeOfDay();
        long l = timeOfDay + t;
        return (l - l % t) + (isDayTime() ? 1000L : 0L);
    }

    @Redirect(method = "sendSleepingStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"))
    private MutableText hdemise$translatable(String key) {
        return isDayTime() ? Text.translatable("sleep.hdemise.skipping_day") : Text.translatable(key);
    }
}