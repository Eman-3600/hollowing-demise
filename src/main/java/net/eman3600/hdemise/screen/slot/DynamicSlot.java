package net.eman3600.hdemise.screen.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.List;

public class DynamicSlot extends Slot {

    private boolean enabled;


    public DynamicSlot(Inventory inventory, int index, int x, int y, boolean enabled) {
        super(inventory, index, x, y);

        this.enabled = enabled;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return isEnabled();
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return isEnabled();
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public static void enableAll(List<? extends DynamicSlot> list) {
        list.forEach(DynamicSlot::enable);
    }

    public static void disableAll(List<? extends DynamicSlot> list) {
        list.forEach(DynamicSlot::disable);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
