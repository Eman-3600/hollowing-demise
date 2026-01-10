package net.eman3600.hdemise.init.event;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.event.callback.RegenCallback;
import net.eman3600.hdemise.event.callback.SoulInUseCallback;
import net.eman3600.hdemise.event.callback.SoulRegenCallback;
import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.item.augment.AugmentItem;
import net.eman3600.hdemise.soul_type.SoulType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import static net.eman3600.hdemise.HDemise.LOGGER;
import static net.eman3600.hdemise.HDemise.MODID;

public class ModCallbacks {

    public static void registerCallbacks() {
        LOGGER.info("Registering Callbacks for " + MODID);


        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            SoulComponent sc = SoulComponent.of(newPlayer);

            if (!alive) {
                sc.onDeath();
            } else {
                sc.reloadAttributes();
                sc.updateAbilities(true);
            }

            sc.forEachAugment((stack, player) -> {
                if (stack.getItem() instanceof AugmentItem item) {
                    item.onRespawn(newPlayer, stack, alive);
                }
            });
        });

        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            SoulComponent sc = SoulComponent.of(handler.getPlayer());

            sc.reloadAttributes();
            sc.setFocusing(false);
            sc.setJetting(false);
            sc.updateAbilities(true);
        });

        ServerLivingEntityEvents.ALLOW_DEATH.register(((entity, source, damageAmount) -> {
            if (entity instanceof PlayerEntity player && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                SoulComponent sc = SoulComponent.of(player);

                if (sc.hasAugment(ModItems.UNDYING_TALISMAN) && !sc.isAfflicted()) {
                    player.setHealth(1f);
                    player.clearStatusEffects();
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 80, 2));
                    if (source.isIn(DamageTypeTags.IS_FIRE)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0));
                    }
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 80, 3));
                    sc.setCorruption(sc.getMaxCorruption());
                    return false;
                }
            }

            return true;
        }));




        // Basic Soul in Use
        SoulInUseCallback.EVENT.register((player, sc) -> sc.isFocusing() || sc.isGhost() || sc.isJetting() || sc.isVanishing());

        // Golden Foot Drain
        SoulInUseCallback.EVENT.register((player, sc) -> player.isSprinting() && !player.isSwimming() && sc.hasAugment(ModItems.GOLDEN_FOOT) && (player.isOnGround() || sc.hasAugment(ModTags.Items.AERIAL_IMPROVEMENT)));




        // Construct Regen
        SoulRegenCallback.EVENT.register((player, sc) -> sc.getSoulType() == ModSoulTypes.CONSTRUCT ?
                player.getEntityWorld().isDay() && player.getEntityWorld().isSkyVisibleAllowingSea(BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ())) ?
                        8f : 2f : 0);

        // Essence Core Regen
        SoulRegenCallback.EVENT.register((player, sc) -> sc.hasAugment(ModItems.ESSENCE_CORE) && sc.getSoul() < SoulComponent.SOUL_PER_VESSEL ? 10f : 0f);

        // Status Effect Soul Regen
        SoulRegenCallback.EVENT.register((player, sc) -> player.hasStatusEffect(ModStatusEffects.SOUL_REGEN) ? (player.getStatusEffect(ModStatusEffects.SOUL_REGEN).getAmplifier() + 1) * 5f : 0f);

        // Forbidden Fruit Regen
        RegenCallback.EVENT.register((player, sc) -> sc.hasAugment(ModItems.FORBIDDEN_FRUIT) ? .16f : 0);
    }
}
