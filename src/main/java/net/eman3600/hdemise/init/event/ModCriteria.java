package net.eman3600.hdemise.init.event;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.event.criterion.AugmentCriterion;
import net.eman3600.hdemise.event.criterion.TriggerCriterion;
import net.eman3600.hdemise.event.criterion.SoulTypeChangedCriterion;
import net.eman3600.hdemise.event.criterion.UniqueSoulCriterion;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModCriteria {

    public static final SoulTypeChangedCriterion SOUL_TYPE_CHANGED = register("soul_type_changed", new SoulTypeChangedCriterion());
    public static final TriggerCriterion CURE = register("cure", new TriggerCriterion());
    public static final UniqueSoulCriterion UNIQUE_SOUL = register("unique_soul", new UniqueSoulCriterion());
    public static final AugmentCriterion AUGMENTS_EQUIPPED = register("augments_equipped", new AugmentCriterion());
    public static final TriggerCriterion AFFLICTION_START = register("affliction_start", new TriggerCriterion());
    public static final TriggerCriterion AFFLICTION_END = register("affliction_end", new TriggerCriterion());


    private static <T extends Criterion<?>> T register(String id, T criterion) {
        return Registry.register(Registries.CRITERION, Identifier.of(MODID, id), criterion);
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering criterian for {}", MODID);
    }
}
