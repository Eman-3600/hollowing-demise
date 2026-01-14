package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.init.entity.ModAttributes;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.util.SoulAttribute;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;

public class SoulItem extends Item {

    private final SoulType soulType;

    public SoulItem(Settings settings, SoulType soulType) {
        super(settings);
        this.soulType = soulType;
    }

    public ItemStack breakSoul() {
        return ItemStack.EMPTY;
    }

    public SoulType getSoulType() {
        return soulType;
    }

    public static Settings getDefaultSettings() {
        return new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON);
    }

    public static void saveStats(PlayerEntity player, ItemStack stack) {
        final SoulComponent sc = SoulComponent.of(player);

        if (stack.isEmpty()) {
            sc.saveHollowStats();
            return;
        }

        NbtComponent component = stack.getOrDefault(ModDataComponentTypes.SOUL, NbtComponent.of(new NbtCompound()));

        component = component.apply(nbt -> {
            nbt.putFloat("hp", player.getHealth());
            nbt.putInt("food", player.getHungerManager().getFoodLevel());
            nbt.putFloat("saturation", player.getHungerManager().getSaturationLevel());
            nbt.putInt("soul", sc.getSoul());
            nbt.putInt("corruption", sc.getCorruption());
            nbt.putBoolean("afflicted", sc.isAfflicted());
            nbt.putBoolean("used_pale_revive", sc.hasUsedPaleRevive());
            nbt.putInt("level", player.experienceLevel);
            nbt.putFloat("experience_progress", player.experienceProgress);
            nbt.putInt("display_points", (int)(player.experienceProgress * player.getNextLevelExperience()));
        });

        stack.set(ModDataComponentTypes.SOUL, component);
    }

    public static void loadStats(PlayerEntity player, ItemStack stack, boolean setSoulType) {
        final SoulComponent sc = SoulComponent.of(player);

        if (stack.isEmpty()) {
            if (setSoulType) {
                sc.setSoulType(ModSoulTypes.HOLLOW);
                sc.setGhost(false);
                if (sc.isHollowTopped()) {
                    sc.topUp();
                } else {
                    sc.loadHollowStats();
                }
            }
            return;
        }

        HungerManager manager = player.getHungerManager();

        if (setSoulType && stack.getItem() instanceof SoulItem item) {
            sc.setSoulType(item.getSoulType());
            sc.setGhost(false);
        }

        NbtComponent component = stack.get(ModDataComponentTypes.SOUL);
        if (component == null) {
            sc.topUp();
            player.experienceLevel = 0;
            player.experienceProgress = 0;
            player.totalExperience = 0;
            saveStats(player, stack);
        } else {
            NbtCompound nbt = component.copyNbt();

            sc.setAfflicted(nbt.getBoolean("afflicted", false));
            sc.setUsedPaleRevive(nbt.getBoolean("has_used_pale_revive", false));
            player.setHealth(nbt.getFloat("hp", player.getHealth()));
            manager.setFoodLevel(nbt.getInt("food", manager.getFoodLevel()));
            manager.setSaturationLevel(nbt.getFloat("saturation", manager.getSaturationLevel()));
            sc.setSoul(nbt.getInt("soul", sc.getSoul()));
            sc.setCorruption(nbt.getInt("corruption", 0));
            player.experienceLevel = nbt.getInt("level", player.experienceLevel);
            player.experienceProgress = nbt.getFloat("experience_progress", player.experienceProgress);

            if (player instanceof ServerPlayerEntityAccess access) {
                access.hdemise$markXPDirty();
            }
        }
    }

    public static void resetStats(ItemStack stack) {
        if (stack.isEmpty()) return;
        stack.remove(ModDataComponentTypes.SOUL);
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        super.onItemEntityDestroyed(entity);

        if (!entity.getEntityWorld().isClient()) {
            entity.getEntityWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GHAST_HURT, SoundCategory.MASTER, 1, .5f);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        NbtComponent component = stack.get(ModDataComponentTypes.SOUL);
        if (component != null && soulType.hasExperience()) {
            NbtCompound nbt = component.copyNbt();

            textConsumer.accept(Text.translatable("tooltip.hdemise.soul.level", nbt.getInt("level", 0)).withColor(Colors.LIGHT_GRAY));
            textConsumer.accept(Text.translatable("tooltip.hdemise.soul.extra", nbt.getInt("display_points", 0)).withColor(Colors.LIGHT_GRAY));
        }

        List<SoulAttribute> modifiers = this.soulType.getAttributes();

        if (!modifiers.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("tooltip.hdemise.soul_attributes").withColor(Colors.LIGHT_GRAY));

            for (SoulAttribute modifier : modifiers) {
                if (modifier.attribute() == EntityAttributes.SAFE_FALL_DISTANCE
                        || modifier.attribute() == EntityAttributes.FALL_DAMAGE_MULTIPLIER
                ) continue;

                boolean green = false;

                double e;
                if (modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        || modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = modifier.value() * 100.0;
                } else if (modifier.attribute().matches(EntityAttributes.KNOCKBACK_RESISTANCE)) {
                    e = modifier.value() * 10.0;
                } else {
                    double d = modifier.value();
                    if (modifier.attribute() == EntityAttributes.MAX_HEALTH) {
                        d += 20;
                        green = true;
                    } else if (modifier.attribute() == ModAttributes.MAX_SOUL) {
                        d += 10;
                        green = true;
                    }
                    e = d;
                }

                if (green) {
                    textConsumer.accept(
                            ScreenTexts.space()
                                    .append(
                                            Text.translatable(
                                                    "attribute.modifier.equals." + modifier.operation().getId(),
                                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                                    Text.translatable(modifier.attribute().value().getTranslationKey())
                                            )
                                    )
                                    .formatted(Formatting.DARK_GREEN)
                    );
                } else if (e > 0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.plus." + modifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(modifier.attribute().value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute().value().getFormatting(true))
                    );
                } else if (e < 0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.take." + modifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(-e),
                                            Text.translatable(modifier.attribute().value().getTranslationKey())
                                    )
                                    .formatted(modifier.attribute().value().getFormatting(false))
                    );
                }
            }
        }
    }
}
