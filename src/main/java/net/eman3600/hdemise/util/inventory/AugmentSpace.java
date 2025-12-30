package net.eman3600.hdemise.util.inventory;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public class AugmentSpace {

    private final int x;
    private final int y;
    private final TagKey<Item> type;

    public AugmentSpace(int x, int y, TagKey<Item> type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TagKey<Item> getType() {
        return type;
    }
}
