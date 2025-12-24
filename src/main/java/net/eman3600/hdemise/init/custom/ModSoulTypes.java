package net.eman3600.hdemise.init.custom;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.soul_type.*;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModSoulTypes {

    public static final SoulType MORTAL = SoulTypeRegistry.register("mortal", MortalSoulType::new);
    public static final SoulType HOLLOW = SoulTypeRegistry.register("hollow", HollowSoulType::new);
    public static final SoulType AMETHYST = SoulTypeRegistry.register("amethyst", AmethystSoulType::new);
    public static final SoulType PHANTOM = SoulTypeRegistry.register("phantom", PhantomSoulType::new);
    public static final SoulType ROTTEN = SoulTypeRegistry.register("rotten", RottenSoulType::new);
    public static final SoulType CONSTRUCT = SoulTypeRegistry.register("construct", ConstructSoulType::new);
    public static final SoulType NEGATIVE = SoulTypeRegistry.register("negative", NegativeSoulType::new);


    public static void registerAll() {
        HDemise.LOGGER.info("Registering Soul Types for " + MODID);
    }
}
