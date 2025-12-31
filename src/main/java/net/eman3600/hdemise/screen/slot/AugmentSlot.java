package net.eman3600.hdemise.screen.slot;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;

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
        return stack.isIn(tag) && super.canInsert(stack);
    }
}
