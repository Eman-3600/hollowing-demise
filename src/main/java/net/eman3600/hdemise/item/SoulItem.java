package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.init.custom.ModSoulTypes;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.eman3600.hdemise.soul_type.SoulType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

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

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        SoulComponent sc = SoulComponent.of(user);

        if (sc.isSoulless()) {
            if (!world.isClient()) {
                ItemStack stack = user.getStackInHand(hand);
                ItemStack copy = stack.copyWithCount(1);

                sc.getInventory().setStack(0, copy);
                sc.applySoulStack(copy);

                stack.decrementUnlessCreative(1, user);
            }

            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, .8f, .8f + .4f * world.getRandom().nextFloat());

            return ActionResult.SUCCESS;
        }

        return super.use(world, user, hand);
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

            player.setHealth(nbt.getFloat("hp", player.getHealth()));
            manager.setFoodLevel(nbt.getInt("food", manager.getFoodLevel()));
            manager.setSaturationLevel(nbt.getFloat("saturation", manager.getSaturationLevel()));
            sc.setSoul(nbt.getInt("soul", sc.getSoul()));
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
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);

        NbtComponent component = stack.get(ModDataComponentTypes.SOUL);
        if (component == null) {
            textConsumer.accept(Text.translatable("tooltip.hdemise.soul.first").withColor(Colors.GRAY));
        } else {
            NbtCompound nbt = component.copyNbt();

            textConsumer.accept(Text.translatable("tooltip.hdemise.soul.level", nbt.getInt("level", 0)).withColor(Colors.GRAY));
            textConsumer.accept(Text.translatable("tooltip.hdemise.soul.extra", nbt.getInt("display_points", 0)).withColor(Colors.GRAY));
        }
    }
}
