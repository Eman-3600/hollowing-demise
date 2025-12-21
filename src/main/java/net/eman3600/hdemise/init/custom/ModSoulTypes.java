package net.eman3600.hdemise.init.custom;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.soul_type.HollowSoulType;
import net.eman3600.hdemise.soul_type.MortalSoulType;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.soul_type.SoulTypeRegistry;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModSoulTypes {

    public static final SoulType MORTAL = SoulTypeRegistry.register("mortal", MortalSoulType::new);
    public static final SoulType HOLLOW = SoulTypeRegistry.register("hollow", HollowSoulType::new);


    public static void registerAll() {
        HDemise.LOGGER.info("Registering Soul Types for " + MODID);
    }
}
