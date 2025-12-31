package net.eman3600.hdemise.screen;

import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.event.ModScreenHandlerTypes;
import net.eman3600.hdemise.screen.slot.AugmentSlot;
import net.eman3600.hdemise.screen.slot.DynamicSlot;
import net.eman3600.hdemise.screen.slot.SoulSlot;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.soul_type.SoulTypeRegistry;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.*;

public class InfusionScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private Page page = Page.MAIN;
    private final SoulSlot soulSlot;
    private final SoulSlot infoSoulSlot;
    private final Property pageProperty;

    private final PlayerEntity player;

    private final List<DynamicSlot> dynamicSlots;
    private final Map<SoulType, List<AugmentSlot>> typeAugments;


    public InfusionScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public InfusionScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandlerTypes.INFUSION, syncId);

        this.player = playerInventory.player;
        this.context = context;
        this.pageProperty = addProperty(new Property() {
            @Override
            public int get() {
                return InfusionScreenHandler.this.page.id();
            }

            @Override
            public void set(int value) {
                InfusionScreenHandler.this.page = Page.fromId(value);
            }
        });
        this.addProperty(pageProperty);

        SoulComponent sc = SoulComponent.of(player);

        List<DynamicSlot> dynamicSlots = new ArrayList<>();
        this.typeAugments = new HashMap<>();

        this.addPlayerSlots(playerInventory, 8, 140);

        this.soulSlot = new SoulSlot(this, playerInventory.player, 80, 64, true);
        this.infoSoulSlot = new SoulSlot(this, playerInventory.player, 8, 64, false);

        this.addSlot(soulSlot);
        this.addSlot(infoSoulSlot);

        dynamicSlots.add(soulSlot);
        dynamicSlots.add(infoSoulSlot);

        for (SoulType type : SoulTypeRegistry.REGISTRY) {

            List<AugmentSlot> augmentSlots = new ArrayList<>();

            for (int i = 0; i < type.getAugments().size(); i++) {
                AugmentSlot slot = new AugmentSlot(this, player, type.getAugments().get(i), i + 1, type == sc.getSoulType());

                dynamicSlots.add(slot);
                augmentSlots.add(slot);
                this.addSlot(slot);
            }

            typeAugments.put(type, List.copyOf(augmentSlots));
        }

        this.dynamicSlots = List.copyOf(dynamicSlots);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.INFUSION_TABLE) && context.get((world, pos) -> {
            InfusionTableBlockEntity entity = world.getBlockEntity(pos) instanceof InfusionTableBlockEntity e ? e : null;

            return (entity != null && entity.usable());
        }, true);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        this.syncState();
    }

    public void playSound(SoundEvent sound, float volume, float pitchMin, float pitchMax) {
        context.run((world, blockPos) -> world.playSound(null, blockPos, sound, SoundCategory.BLOCKS, volume, pitchMin + (pitchMax - pitchMin) * world.getRandom().nextFloat()));
    }

    public void reloadSlots() {
        DynamicSlot.disableAll(dynamicSlots);

        SoulComponent sc = SoulComponent.of(player);

        switch (page) {
            case MAIN -> {
                soulSlot.enable();

                List<AugmentSlot> augments = typeAugments.getOrDefault(sc.getSoulType(), List.of());
                DynamicSlot.enableAll(augments);
            }
            case INFO -> {
                infoSoulSlot.enable();
            }
        }
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        SoulComponent sc = SoulComponent.of(player);

        switch (id) {
            case 0 -> {
                if (sc.canTopUp()) {

                    if (!player.getEntityWorld().isClient()) {

                        sc.topUp();
                        sc.setHollowTopped(!sc.isSoulless());
                        sc.validateSoulStack();

                        playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1, .8f, 1.2f);
                    }

                    sc.startTopUpCooldown();

                    return true;
                }
            }
        }

        return super.onButtonClick(player, id);
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
