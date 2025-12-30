package net.eman3600.hdemise.util.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;

public class SoulInventory implements Inventory {

    private final DefaultedList<ItemStack> stacks;

    public SoulInventory() {
        stacks = DefaultedList.ofSize(12, ItemStack.EMPTY);
    }


    public int size() {
        return this.stacks.size();
    }

    public boolean isEmpty() {
        java.util.Iterator<ItemStack> var1 = this.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while (itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= this.size() ? ItemStack.EMPTY : this.stacks.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.stacks, slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {

        return Inventories.splitStack(this.stacks, slot, amount);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        //this.handler.onContentChanged(this);
    }

    @Override
    public void markDirty() {
        //handler.getContentsChangedListener().run();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.stacks.clear();
    }

    public void scatterAugments(PlayerEntity player) {
        for (int i = 1; i < stacks.size(); i++) {
            if (stacks.get(i).isEmpty()) continue;
            ItemScatterer.spawn(player.getEntityWorld(), player.getX(), player.getY(), player.getZ(), stacks.remove(i));
        }
    }

    public void readData(ReadView readView) {
        Inventories.readData(readView, stacks);
    }

    public void writeData(WriteView writeView) {
        Inventories.writeData(writeView, stacks, true);
    }
}
