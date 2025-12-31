package net.eman3600.hdemise.screen.slot;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;

public class SoulSlot extends DynamicSlot {

    private final InfusionScreenHandler handler;
    private final PlayerEntity player;

    public SoulSlot(InfusionScreenHandler handler, PlayerEntity player, int x, int y, boolean enabled) {
        super(SoulComponent.of(player).getInventory(), 0, x, y, enabled);

        this.handler = handler;
        this.player = player;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof SoulItem && super.canInsert(stack);
    }

    //    @Override
//    public void onTakeItem(PlayerEntity player, ItemStack stack) {
//        super.onTakeItem(player, stack);
//    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        SoulItem.saveStats(player, previousStack);
        SoulItem.loadStats(player, stack, true);

        super.setStack(stack, previousStack);


        handler.reloadSlots();
        handler.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1, .8f, 1.2f);
        handler.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1, .8f, 1.2f);
    }
}
