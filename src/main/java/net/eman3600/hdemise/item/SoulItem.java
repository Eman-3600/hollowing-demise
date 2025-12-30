package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.eman3600.hdemise.soul_type.SoulType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

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

            return ActionResult.SUCCESS;
        }

        return super.use(world, user, hand);
    }

    public static Settings getDefaultSettings() {
        return new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON);
    }

    public static void saveStats(PlayerEntity player, ItemStack stack) {
        if (stack.isEmpty()) return;

        NbtComponent component = stack.getOrDefault(ModDataComponentTypes.SOUL, NbtComponent.of(new NbtCompound()));

        final SoulComponent sc = SoulComponent.of(player);

        component = component.apply(nbt -> {
            nbt.putFloat("hp", player.getHealth());
            nbt.putInt("food", player.getHungerManager().getFoodLevel());
            nbt.putFloat("saturation", player.getHungerManager().getSaturationLevel());
            nbt.putInt("soul", sc.getSoul());
            nbt.putInt("level", player.experienceLevel);
            nbt.putFloat("experience_progress", player.experienceProgress);
        });

        stack.set(ModDataComponentTypes.SOUL, component);
    }

    public static void loadStats(PlayerEntity player, ItemStack stack, boolean setSoulType) {
        if (stack.isEmpty()) return;

        final SoulComponent sc = SoulComponent.of(player);
        HungerManager manager = player.getHungerManager();

        if (setSoulType && stack.getItem() instanceof SoulItem item) {
            sc.setSoulType(item.getSoulType());
        }

        NbtComponent component = stack.get(ModDataComponentTypes.SOUL);
        if (component == null) {
            sc.topUp();
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
}
