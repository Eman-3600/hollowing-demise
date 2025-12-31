package net.eman3600.hdemise.screen.slot;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SoulSlot extends Slot {

    private final InfusionScreenHandler handler;
    private final PlayerEntity player;

    public SoulSlot(InfusionScreenHandler handler, PlayerEntity player, int x, int y) {
        super(SoulComponent.of(player).getInventory(), 0, x, y);

        this.handler = handler;
        this.player = player;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        super.onTakeItem(player, stack);

        SoulItem.saveStats(player, stack);
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        ItemStack s = super.insertStack(stack, count);

        SoulItem.loadStats(player, s, true);

        return s;
    }
}
