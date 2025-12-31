package net.eman3600.hdemise.screen;

import net.eman3600.hdemise.init.event.ModScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

public class InfusionScreenHandler extends ScreenHandler {

    private Page page = Page.MAIN;


    public InfusionScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public InfusionScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlerTypes.INFUSION, syncId);

        this.addPlayerSlots(playerInventory, 8, 140);


    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        this.syncState();
    }

    public enum Page {
        MAIN,
        INFO,
        REPAIR;

        public int id() {
            for (int i = 0; i < Page.values().length; i++) {
                if (Page.values()[i] == this) {
                    return i;
                }
            }
            return 0;
        }

        public static Page fromId(int id) {
            if (id >= 0 && id < Page.values().length) {
                return Page.values()[id];
            }

            return MAIN;
        }
    }
}
