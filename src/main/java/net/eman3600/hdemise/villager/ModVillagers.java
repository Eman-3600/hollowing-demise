package net.eman3600.hdemise.villager;

import com.google.common.collect.ImmutableSet;
import net.eman3600.hdemise.init.basics.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jspecify.annotations.Nullable;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModVillagers {
    public static final RegistryKey<PointOfInterestType> MORTICIAN_POI_KEY = registerPoiKey("mortician_poi");
    public static final PointOfInterestType MORTICIAN_POI = registerPOI("mortician_poi", ModBlocks.INFUSION_TABLE);

    public static final VillagerProfession MORTICIAN = register("mortician", MORTICIAN_POI_KEY, SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    private static VillagerProfession register(String name, RegistryKey<PointOfInterestType> heldWorkstation, @Nullable SoundEvent workSound) {
        Identifier id = getID(name);
        RegistryKey<VillagerProfession> key = RegistryKey.of(RegistryKeys.VILLAGER_PROFESSION, id);

        return Registry.register(Registries.VILLAGER_PROFESSION, id,
                new VillagerProfession(Text.translatable("entity." + key.getValue().getNamespace() + ".villager." + key.getValue().getPath()),
                        entry -> entry.matchesKey(heldWorkstation), entry -> entry.matchesKey(heldWorkstation),
                        ImmutableSet.of(), ImmutableSet.of(), workSound));
    }

    public static PointOfInterestType registerPOI(String name, Block block) {
        return PointOfInterestHelper.register(getID(name),1, 1, block);
    }

    public static RegistryKey<PointOfInterestType> registerPoiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, getID(name));
    }

    public static void registerVillagers() {
        LOGGER.info("Registering Villagers for " + MODID);
    }

    private static Identifier getID(String name) {
        return Identifier.of(MODID, name);
    }
}
