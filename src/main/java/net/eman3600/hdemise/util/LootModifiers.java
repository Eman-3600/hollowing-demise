package net.eman3600.hdemise.util;

import net.eman3600.hdemise.mixin_interfaces.LootTableAccess;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

import static net.eman3600.hdemise.HDemise.MODID;

public class LootModifiers {

    private static Identifier buildInjectionRoute(Identifier id) {
        return Identifier.of(MODID, "injections/" + id.getNamespace() + "/" + id.getPath());
    }

    public static void modifyLootTables() {
//        LootTableEvents.MODIFY.register((key, builder, source, lookup) -> {
//
//            RegistryKey<LootTable> injector = RegistryKey.of(RegistryKeys.LOOT_TABLE, buildInjectionRoute(key.getValue()));
//            Optional<Reference<LootTable>> optional = lookup.getOrThrow(RegistryKeys.LOOT_TABLE).getOptional(injector);
//
//            if (optional.isPresent() && optional.get().value() instanceof LootTableAccess access) {
//                List<LootPool> pools = access.hdemise$getPools();
//
//                if (pools != null) {
//                    for (LootPool pool : pools) {
//                        builder.pool(pool);
//                    }
//                }
//
//            }
//
//        });
    }
}
