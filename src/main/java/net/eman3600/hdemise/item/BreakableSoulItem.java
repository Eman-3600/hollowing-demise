package net.eman3600.hdemise.item;

import net.eman3600.hdemise.soul_type.SoulType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BreakableSoulItem extends SoulItem {

    private final Item breakItem;

    public BreakableSoulItem(Settings settings, SoulType soulType, Item breakItem) {
        super(settings, soulType);
        this.breakItem = breakItem;
    }

    @Override
    public ItemStack breakSoul() {
        return breakItem.getDefaultStack();
    }
}
