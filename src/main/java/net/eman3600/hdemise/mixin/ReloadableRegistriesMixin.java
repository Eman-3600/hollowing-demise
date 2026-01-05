package net.eman3600.hdemise.mixin;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import net.eman3600.hdemise.mixin_interfaces.LootTableAccess;
import net.fabricmc.fabric.api.loot.v3.FabricLootTableBuilder;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.ReloadableRegistries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

import static net.eman3600.hdemise.HDemise.MODID;

@Mixin(ReloadableRegistries.class)
public abstract class ReloadableRegistriesMixin {

    @Unique
    private static Identifier buildInjectionRoute(Identifier id) {
        return Identifier.of(MODID, "injections/" + id.getNamespace() + "/" + id.getPath());
    }

    @Inject(method = "method_61240", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private static <T> void hdemise$prepare(LootDataType<T> lootDataType, ResourceManager resourceManager, RegistryOps<JsonElement> registryOps, CallbackInfoReturnable<MutableRegistry<?>> cir, @Local Map<Identifier, T> map) {
        map.replaceAll((identifier, t) -> modifyLootTable(map, t, identifier, registryOps));
    }

    @Unique
    private static <T> T modifyLootTable(Map<Identifier,T> map, T obj, Identifier identifier, RegistryOps<JsonElement> registryOps) {
        if (obj instanceof LootTable table) {

            Identifier injectorId = buildInjectionRoute(identifier);
            if (map.containsKey(injectorId) && map.get(injectorId) instanceof LootTableAccess injector) {
                LootTable.Builder builder = FabricLootTableBuilder.copyOf(table);

                List<LootPool> pools = injector.hdemise$getPools();

                if (pools != null) {
                    for (LootPool pool : pools) {
                        builder.pool(pool);
                    }

                    return (T) builder.build();
                }
            }
        }

        return obj;
    }


}
