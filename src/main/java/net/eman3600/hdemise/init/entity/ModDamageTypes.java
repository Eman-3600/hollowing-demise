package net.eman3600.hdemise.init.entity;

import net.eman3600.hdemise.HDemise;
import net.fabricmc.fabric.api.event.registry.FabricRegistry;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModDamageTypes {

    public static final RegistryKey<DamageType> VIGOR_FAILED = keyOf("vigor_failed");



    private static RegistryKey<DamageType> keyOf(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MODID, name));
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering damage types for {}", MODID);
    }
}
