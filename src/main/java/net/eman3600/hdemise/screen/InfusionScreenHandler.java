package net.eman3600.hdemise.screen;

import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.block.entity.InfusionTableBlockEntity;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModBlocks;
import net.eman3600.hdemise.init.basics.ModItems;
import net.eman3600.hdemise.init.basics.ModRecipes;
import net.eman3600.hdemise.init.event.ModScreenHandlerTypes;
import net.eman3600.hdemise.item.SoulItem;
import net.eman3600.hdemise.item.XPCoreItem;
import net.eman3600.hdemise.mixin_interfaces.ServerPlayerEntityAccess;
import net.eman3600.hdemise.recipe.InfusionRecipe;
import net.eman3600.hdemise.recipe.InfusionRecipeInput;
import net.eman3600.hdemise.screen.slot.AugmentSlot;
import net.eman3600.hdemise.screen.slot.DynamicSlot;
import net.eman3600.hdemise.screen.slot.SoulSlot;
import net.eman3600.hdemise.screen.slot.XPCoreSlot;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.soul_type.SoulTypeRegistry;
import net.eman3600.hdemise.util.IngredientWithCount;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.WorldEvents;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class InfusionScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private Page page = Page.MAIN;
    private final SoulSlot soulSlot;
    private final SoulSlot infoSoulSlot;
    private final Property pageProperty;
    private final XPCoreSlot coreSlot;

    @Nullable
    private InfusionRecipe lastRecipe;
    private final PlayerEntity player;

    private final List<DynamicSlot> dynamicSlots;
    private final Map<SoulType, List<AugmentSlot>> typeAugments;
    private final List<DynamicSlot> repairSlots;

    public final Inventory repairInventory = new SimpleInventory(4) {
        @Override
        public void markDirty() {
            super.markDirty();
            InfusionScreenHandler.this.onContentChanged(this);
        }
    };
    public final CraftingResultInventory output = new CraftingResultInventory() {
        @Override
        public void markDirty() {
            super.markDirty();
            InfusionScreenHandler.this.onContentChanged(this);
        }
    };


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

        this.coreSlot = new XPCoreSlot(repairInventory, 0, 8, 95, false);
        this.addSlot(coreSlot);
        dynamicSlots.add(coreSlot);

        this.repairSlots = List.of(
                new DynamicSlot(repairInventory, 1, 17, 64, false),
                new DynamicSlot(repairInventory, 2, 66, 64, false),
                new DynamicSlot(repairInventory, 3, 84, 64, false),
                new DynamicSlot(output, 0, 144, 64, false) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public void onTakeItem(PlayerEntity player, ItemStack stack) {
                        InfusionScreenHandler.this.onRepair(player, stack);

                        super.onTakeItem(player, stack);

                    }
                }
        );
        for (DynamicSlot s : repairSlots) {
            this.addSlot(s);
            dynamicSlots.add(s);
        }

        this.dynamicSlots = List.copyOf(dynamicSlots);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack stack = ItemStack.EMPTY;

        Slot s = getSlot(slot);

        if (s.hasStack() && s.canTakeItems(player)) {

            ItemStack transferStack = getSlot(slot).getStack();
            ItemStack stackCopy = transferStack.copy();

            if (slot >= soulSlot.id) {

                if (!insertItem(transferStack, 0, soulSlot.id, true)) {
                    return ItemStack.EMPTY;
                }

                s.setStack(transferStack, stackCopy);
                s.onQuickTransfer(transferStack, stackCopy);
                s.onTakeItem(player, transferStack);

                stack = stackCopy;
            }
        }

        return stack;
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
        this.output.clear();
        this.context.run((world, pos) -> this.dropInventory(player, this.repairInventory));
        this.syncState();
    }

    public void playSound(SoundEvent sound, float volume, float pitchMin, float pitchMax) {
        context.run((world, blockPos) -> world.playSound(null, blockPos, sound, SoundCategory.BLOCKS, volume, pitchMin + (pitchMax - pitchMin) * world.getRandom().nextFloat()));
    }

    public boolean canExtractExperience() {
        return repairInventory.getStack(0).isEmpty() && (player.experienceLevel > 0 || player.experienceProgress > 0);
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
            case REPAIR -> {
                coreSlot.enable();
                DynamicSlot.enableAll(repairSlots);
            }
        }
    }

    public InfusionRecipeInput createRecipeInput() {
        return new InfusionRecipeInput(repairInventory.getStack(1), repairInventory.getStack(2), repairInventory.getStack(3));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);

        if (inventory == repairInventory) {
            InfusionRecipeInput input = createRecipeInput();

            Optional<RecipeEntry<InfusionRecipe>> optional;
            if (this.player.getEntityWorld() instanceof ServerWorld serverWorld) {
                optional = serverWorld.getRecipeManager().getFirstMatch(ModRecipes.INFUSION_TYPE, input, serverWorld);
            } else {
                optional = Optional.empty();
            }

            optional.ifPresentOrElse(recipe -> {

                this.output.setLastRecipe(recipe);
                this.output.setStack(0, recipe.value().craft(input, player.getEntityWorld().getRegistryManager()));
                this.lastRecipe = recipe.value();
            }, () -> {
                this.output.setLastRecipe(null);
                this.output.setStack(0, ItemStack.EMPTY);
            });
        }
    }

    public void onRepair(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player, stack.getCount());
        this.output.unlockLastRecipe(player, this.getInputStacks());
        if (this.lastRecipe != null) {
            if (!lastRecipe.keepBase()) {
                ItemStack s = repairInventory.getStack(1);
                s.decrement(1);
                repairInventory.setStack(1, s);
            }

            ItemStack ingredient1 = repairInventory.getStack(2);
            lastRecipe.ingredient().consume(ingredient1);
            repairInventory.setStack(2, ingredient1);

            if (lastRecipe.repair()) {
                ItemStack repairStack = repairInventory.getStack(3);
                lastRecipe.consumeRepairStack(repairStack);
                repairInventory.setStack(3, repairStack);
            }
        }

        context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    private List<ItemStack> getInputStacks() {
        return List.of(repairInventory.getStack(1),repairInventory.getStack(2),repairInventory.getStack(3));
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
            case 1 -> {
                if (page == Page.MAIN) {
                    setPage(Page.INFO);
                    reloadSlots();
                    return true;
                }
            }
            case 2 -> {
                if (page == Page.MAIN) {
                    setPage(Page.REPAIR);
                    reloadSlots();
                    return true;
                }
            }
            case 3 -> {
                if (page != Page.MAIN) {
                    setPage(Page.MAIN);
                    reloadSlots();
                    return true;
                }
            }
            case 4 -> {
                if (canExtractExperience()) {
                    ItemStack stack = ModItems.EXPERIENCE_CORE.extractPlayerExperience(player);
                    repairInventory.setStack(0, stack);
                    if (player instanceof ServerPlayerEntityAccess access) {
                        SoulComponent.of(player).validateSoulStack();
                        access.hdemise$markXPDirty();
                        playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1, 1, 1.1f);
                    }
                    return true;
                }
            }
        }

        return super.onButtonClick(player, id);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.clear();
        SoulComponent.of(player).markDirty();
        this.context.run((world, pos) -> this.dropInventory(player, this.repairInventory));
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
