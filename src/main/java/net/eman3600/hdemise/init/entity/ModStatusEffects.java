package net.eman3600.hdemise.init.entity;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.mob_effects.ModStatusEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModStatusEffects {

    public static final RegistryEntry<StatusEffect> RAGE = register("rage", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xf79080)
            .addAttributeModifier(EntityAttributes.ATTACK_DAMAGE, Identifier.of(MODID, "effect.rage"), 3, EntityAttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(EntityAttributes.MOVEMENT_SPEED, Identifier.of(MODID, "effect.rage"), .4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(EntityAttributes.ATTACK_SPEED, Identifier.of(MODID, "effect.rage"), .25, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(EntityAttributes.ATTACK_KNOCKBACK, Identifier.of(MODID, "effect.rage"), -.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final RegistryEntry<StatusEffect> LIGHTFOOT = register("lightfoot", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xfdf55f)
            .addAttributeModifier(EntityAttributes.MOVEMENT_SPEED, Identifier.of(MODID, "effect.lightfoot"), .3, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    public static final RegistryEntry<StatusEffect> DEMON_STRENGTH = register("demon_strength", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0xd25e7f)
            .addAttributeModifier(EntityAttributes.ATTACK_DAMAGE, Identifier.of(MODID, "effect.demon_strength"), .1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));

    public static final RegistryEntry<StatusEffect> CHAINED = register("chained", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x495065));
    public static final RegistryEntry<StatusEffect> BLOCKED = register("blocked", new ModStatusEffect(StatusEffectCategory.HARMFUL, 0x962300));
    public static final RegistryEntry<StatusEffect> SOUL_REGEN = register("soul_regen", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0x4b7d7d));
    public static final RegistryEntry<StatusEffect> SHIELD = register("shield", new ModStatusEffect(StatusEffectCategory.BENEFICIAL, 0x5d9dfd));


    private static RegistryEntry<StatusEffect> register(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MODID, id), statusEffect);
    }

    public static void registerAll() {
        HDemise.LOGGER.info("Registering status effects for " + MODID);
    }
}
