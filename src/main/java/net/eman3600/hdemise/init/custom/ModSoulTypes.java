package net.eman3600.hdemise.init.custom;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.soul_type.*;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModSoulTypes {

    public static final SoulType MORTAL = SoulTypeRegistry.register("mortal", MortalSoulType::new);
    public static final SoulType HOLLOW = SoulTypeRegistry.register("hollow", HollowSoulType::new);
    public static final SoulType CRYSTAL = SoulTypeRegistry.register("crystal", CrystalSoulType::new);
    public static final SoulType MAGE = SoulTypeRegistry.register("mage", MageSoulType::new);
    public static final SoulType PHANTOM = SoulTypeRegistry.register("phantom", PhantomSoulType::new);
    public static final SoulType REVENANT = SoulTypeRegistry.register("revenant", RevenantSoulType::new);
    public static final SoulType CONSTRUCT = SoulTypeRegistry.register("construct", ConstructSoulType::new);
    public static final SoulType PALE = SoulTypeRegistry.register("pale", PaleSoulType::new);
    public static final SoulType NEGATIVE = SoulTypeRegistry.register("negative", NegativeSoulType::new);


    public static void registerAll() {
        HDemise.LOGGER.info("Registering Soul Types for " + MODID);
    }
}
