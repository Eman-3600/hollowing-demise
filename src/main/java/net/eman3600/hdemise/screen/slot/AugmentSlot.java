package net.eman3600.hdemise.screen.slot;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.item.augment.AugmentItem;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;

public class AugmentSlot extends DynamicSlot {

    private final InfusionScreenHandler handler;
    private final PlayerEntity player;
    private final TagKey<Item> tag;

    public AugmentSlot(InfusionScreenHandler handler, PlayerEntity player, AugmentSpace space, int index, boolean enabled) {
        super(SoulComponent.of(player).getInventory(), index, space.getX() + 5, space.getY() + 5, enabled);

        this.handler = handler;
        this.player = player;
        this.tag = space.getType();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isIn(tag) && (getStack().isOf(stack.getItem()) || !SoulComponent.of(player).hasAugment(stack.getItem())) && super.canInsert(stack);
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        super.setStack(stack, previousStack);

        if (previousStack.getItem() instanceof AugmentItem item) {
            item.onRemove(player, previousStack);
        }
        if (stack.getItem() instanceof AugmentItem item) {
            item.onEquip(player, stack);
        }


        handler.playSound(stack.isEmpty() ? SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM : SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, 1, .8f, 1.2f);
    }
}
