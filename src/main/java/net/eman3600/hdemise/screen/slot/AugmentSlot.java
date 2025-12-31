package net.eman3600.hdemise.screen.slot;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;

public class AugmentSlot extends Slot {

    private final InfusionScreenHandler handler;
    private final PlayerEntity player;
    private final TagKey<Item> tag;

    public AugmentSlot(InfusionScreenHandler handler, PlayerEntity player, TagKey<Item> tag, int index, int x, int y) {
        super(SoulComponent.of(player).getInventory(), index, x, y);

        this.handler = handler;
        this.player = player;
        this.tag = tag;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isIn(tag);
    }
}
