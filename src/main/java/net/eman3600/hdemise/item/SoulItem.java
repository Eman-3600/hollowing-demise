package net.eman3600.hdemise.item;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModDataComponentTypes;
import net.eman3600.hdemise.soul_type.SoulType;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Rarity;

public class SoulItem extends Item {

    private final SoulType soulType;

    public SoulItem(Settings settings, SoulType soulType) {
        super(settings);
        this.soulType = soulType;
    }

    public SoulType getSoulType() {
        return soulType;
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



        NbtComponent component = stack.getOrDefault(ModDataComponentTypes.SOUL, NbtComponent.of(new NbtCompound()));

        final SoulComponent sc = SoulComponent.of(player);
        HungerManager manager = player.getHungerManager();

        NbtCompound nbt = component.copyNbt();

        if (setSoulType && stack.getItem() instanceof SoulItem item) {
            sc.setSoulType(item.getSoulType());
        }

        player.setHealth(nbt.getFloat("hp", player.getHealth()));
        manager.setFoodLevel(nbt.getInt("food", manager.getFoodLevel()));
        manager.setSaturationLevel(nbt.getFloat("saturation", manager.getSaturationLevel()));
        sc.setSoul(nbt.getInt("soul", sc.getSoul()));
        player.experienceLevel = nbt.getInt("level", player.experienceLevel);
        player.experienceProgress = nbt.getFloat("experience_progress", player.experienceProgress);
    }
}
