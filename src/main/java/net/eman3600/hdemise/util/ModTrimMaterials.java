package net.eman3600.hdemise.util;

import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModTrimMaterials {

    public static final RegistryKey<ArmorTrimMaterial> ALMARITE = of("almarite");
    public static final RegistryKey<ArmorTrimMaterial> ECTOPLASM = of("ectoplasm");


    private static RegistryKey<ArmorTrimMaterial> of(String id) {
        return RegistryKey.of(RegistryKeys.TRIM_MATERIAL, Identifier.of(MODID, id));
    }
}
