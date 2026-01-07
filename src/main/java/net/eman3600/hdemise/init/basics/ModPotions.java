package net.eman3600.hdemise.init.basics;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModPotions {

    public static final RegistryEntry<Potion> SOUL_REGEN = registerPotion("soul_regen",
            new Potion("soul_regen", new StatusEffectInstance(ModStatusEffects.SOUL_REGEN, 3600, 0)));
    public static final RegistryEntry<Potion> LONG_SOUL_REGEN = registerPotion("long_soul_regen",
            new Potion("soul_regen", new StatusEffectInstance(ModStatusEffects.SOUL_REGEN, 9600, 0)));
    public static final RegistryEntry<Potion> STRONG_SOUL_REGEN = registerPotion("strong_soul_regen",
            new Potion("soul_regen", new StatusEffectInstance(ModStatusEffects.SOUL_REGEN, 1800, 1)));


    public static final RegistryEntry<Potion> CHAINED = registerPotion("chained",
            new Potion("chained", new StatusEffectInstance(ModStatusEffects.CHAINED, 1800, 0)));
    public static final RegistryEntry<Potion> LONG_CHAINED = registerPotion("long_chained",
            new Potion("chained", new StatusEffectInstance(ModStatusEffects.CHAINED, 4800, 0)));

    private static RegistryEntry<Potion> registerPotion(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(MODID, name), potion);
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering potions for {}", MODID);
    }
}
