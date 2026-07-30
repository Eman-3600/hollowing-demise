package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModStatusEffects;
import net.eman3600.hdemise.init.event.ModCriteria;
import net.eman3600.hdemise.networking.s2c.SoulEventPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class ConsumableVoidCureItem extends Item {
    private static final int TOTEM_POWER_DURATION = 1200;
    private static final int COOLDOWN = 20;

    public ConsumableVoidCureItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {

        SoulComponent sc = SoulComponent.of(user);

        int amplifier = 0;
        int previousDuration = 0;
        if (user.hasStatusEffect(ModStatusEffects.TOTEM_POWER)) {
            amplifier = user.getStatusEffect(ModStatusEffects.TOTEM_POWER).getAmplifier() + 1;
            previousDuration = user.getStatusEffect(ModStatusEffects.TOTEM_POWER).getDuration();
        }

        ItemStack stack = user.getStackInHand(hand);

        if (sc.canCure() && (sc.getSoulType() == ModSoulTypes.NEGATIVE || world.getLevelProperties().isHardcore())) {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());

            if (amplifier >= 3 || world.getLevelProperties().isHardcore()) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());

                if (!world.isClient()) {
                    sc.setSoulType(ModSoulTypes.ENDER);
                    user.getHungerManager().setSaturationLevel(15f);
                    sc.replaceSoulStack();
                    user.clearStatusEffects();
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 340, 0));
                    user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SOUL_REGEN, 340, 0));

                    ModCriteria.CURE.trigger((ServerPlayerEntity) user);

                    stack.decrementUnlessCreative(1, user);
                }
            } else if (!world.isClient()) {
                user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.TOTEM_POWER, TOTEM_POWER_DURATION, amplifier, false, true, true));

                user.getItemCooldownManager().set(stack, COOLDOWN);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        World world = MinecraftClient.getInstance().world;
        if (world != null && world.getLevelProperties().isHardcore()) {
            textConsumer.accept(Text.translatable(getTranslationKey() + ".hardcore").withColor(Colors.LIGHT_GRAY));
        } else {
            textConsumer.accept(Text.translatable(getTranslationKey() + ".tooltip").withColor(Colors.LIGHT_GRAY));
        }
    }
}
