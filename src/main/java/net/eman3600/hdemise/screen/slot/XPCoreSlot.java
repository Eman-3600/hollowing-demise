package net.eman3600.hdemise.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class XPCoreSlot extends DynamicSlot {
    public XPCoreSlot(Inventory inventory, int index, int x, int y, boolean enabled) {
        super(inventory, index, x, y, enabled);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }
}
