package net.eman3600.hdemise.screen;

import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.init.basics.ModTags;
import net.eman3600.hdemise.networking.s2c.InfusionRecipePayload;
import net.eman3600.hdemise.screen.InfusionScreenHandler.Page;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.util.inventory.AugmentSpace;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.eman3600.hdemise.HDemise.MODID;

@Environment(EnvType.CLIENT)
public class InfusionScreen extends HandledScreen<InfusionScreenHandler> {

    private static final Identifier MAIN_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/main.png");
    private static final Identifier INFO_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/info.png");
    private static final Identifier REPAIR_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/repair.png");

    private static final Identifier DEFAULT_HEART_TYPE = Identifier.ofVanilla("textures/gui/sprites/hud/heart/full.png");
    private static final Identifier DEFAULT_HARDCORE_HEART_TYPE = Identifier.ofVanilla("textures/gui/sprites/hud/heart/hardcore_full.png");

    private final Button topUpButton;
    private final Button infoPageButton;
    private final Button repairPageButton;
    private final Button mainPageButton;
    private final Button xpButton;

    private int cycleTicks = 0;

    private List<ItemStack> firstRepairStacks = new ArrayList<>();
    private List<ItemStack> secondRepairStacks = new ArrayList<>();

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.backgroundWidth = 176;
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = 129;

        this.topUpButton = new Button(0, 158, 124, 176, 18, 11, 11);
        this.infoPageButton = new Button(1, 158, 113, 176, 40, 11, 11);
        this.repairPageButton = new Button(2, 158, 102, 176, 62, 11, 11);
        this.mainPageButton = new Button(3, 158, 113, 176, 40, 11, 11);
        this.xpButton = new Button(4, 7, 114, 176, 62, 18, 11);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if (handler.getPage() != Page.REPAIR && cycleTicks > 0) {
            cycleTicks = 0;
        } else {
            cycleTicks++;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

        SoulComponent sc = SoulComponent.of(client.player);
        if (sc == null) return;
        SoulType soulType = sc.getSoulType();

        switch (handler.getPage()) {
            case Page.MAIN -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, MAIN_TEXTURE, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
                Identifier soulTexture = Identifier.of(soulType.getId().getNamespace(), "textures/gui/container/soul/" + soulType.getId().getPath() + ".png");

                context.drawTexture(RenderPipelines.GUI_TEXTURED, soulTexture, x + 4, y + 4, 0f, 0f, 168, 133, 168, 133);

                infoPageButton.draw(context, MAIN_TEXTURE, mouseX, mouseY);
                repairPageButton.draw(context, MAIN_TEXTURE, mouseX, mouseY);

                if (infoPageButton.isSelected(mouseX, mouseY) || repairPageButton.isSelected(mouseX, mouseY)) {
                    context.setCursor(StandardCursors.POINTING_HAND);
                }

                if (sc.getInventory().getStack(0).isEmpty()) {
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, MAIN_TEXTURE, x + 79, y + 63, 176, 156, 18, 18, 256, 256);
                }

                List<AugmentSpace> augments = soulType.getAugments();
                for (int i = 0; i < augments.size(); i++) {
                    AugmentSpace space = augments.get(i);
                    int x2 = space.getX() + x + 4;
                    int y2 = space.getY() + y + 4;

                    int v = space.getType() == ModTags.Items.YELLOW_AUGMENT ? 84 :
                            space.getType() == ModTags.Items.GREEN_AUGMENT ? 102 :
                            space.getType() == ModTags.Items.RED_AUGMENT ? 120 : 138;
                    int u = sc.getInventory().getStack(i + 1).isEmpty() ? 176 : 194;

                    context.drawTexture(RenderPipelines.GUI_TEXTURED, MAIN_TEXTURE, x2, y2, u, v, 18, 18, 256, 256);
                }
            }
            case Page.INFO -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, INFO_TEXTURE, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

                mainPageButton.draw(context, INFO_TEXTURE, mouseX, mouseY);

                if (mainPageButton.isSelected(mouseX, mouseY)) {
                    context.setCursor(StandardCursors.POINTING_HAND);
                }
            }
            case REPAIR -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, REPAIR_TEXTURE, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);

                mainPageButton.draw(context, REPAIR_TEXTURE, mouseX, mouseY);

                if (mainPageButton.isSelected(mouseX, mouseY)) {
                    context.setCursor(StandardCursors.POINTING_HAND);
                }

                if (handler.canExtractExperience()) {
                    xpButton.draw(context, REPAIR_TEXTURE, mouseX, mouseY);
                    if (xpButton.isSelected(mouseX, mouseY)) {
                        context.setCursor(StandardCursors.POINTING_HAND);
                    }
                }

                if (handler.repairInventory.getStack(1).isEmpty()) {
                    int v = (cycleTicks / 20 % 4) * 18 + 84;
                    context.drawTexture(RenderPipelines.GUI_TEXTURED, REPAIR_TEXTURE, x + 16, y + 63, 176.0F, v, 18, 18, 256, 256);
                }
            }
        }

        if (sc.canTopUp()) {
            topUpButton.draw(context, MAIN_TEXTURE, mouseX, mouseY);

            if (topUpButton.isSelected(mouseX, mouseY))
                context.setCursor(StandardCursors.POINTING_HAND);
        }

        if (client.world == null) return;

        boolean useDefaultHeart = soulType.heartType() == null;
        Identifier heartType = useDefaultHeart ? (client.world.getLevelProperties().isHardcore() ? DEFAULT_HARDCORE_HEART_TYPE : DEFAULT_HEART_TYPE) : soulType.heartType();

        int progress = sc.getTopUpDisplayPixels();
        int i = topUpButton.getX() + 2;
        int j = topUpButton.getY() + 2;

        if (useDefaultHeart) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, heartType, i, j, 1, 1, progress, 7, 9, 9);
        } else {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, heartType, i, j, client.world.getLevelProperties().isHardcore() ? 37 : 1, 1, progress, 7, 72, 9);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        SoulComponent sc = SoulComponent.of(client.player);

        if (sc != null && topUpButton.isSelected(mouseX, mouseY)) {
            context.drawTooltip(sc.canTopUp() ?
                    Text.translatable("container.hdemise.infusion.top_up") :
                    Text.translatable("container.hdemise.infusion.top_up_cooldown", (sc.getTopUpCooldown()/1200), (sc.getTopUpCooldown()/20 % 60)).withColor(Colors.GRAY),
                    mouseX, mouseY);
        }
        if (this.handler.getPage() == Page.MAIN) {
            if (infoPageButton.isSelected(mouseX, mouseY)) {
                context.drawTooltip(Text.translatable("container.hdemise.infusion.info_page"), mouseX, mouseY);
            } else if (repairPageButton.isSelected(mouseX, mouseY)) {
                context.drawTooltip(Text.translatable("container.hdemise.infusion.repair_page"), mouseX, mouseY);
            }
        } else if (mainPageButton.isSelected(mouseX, mouseY)) {
            context.drawTooltip(Text.translatable("container.hdemise.infusion.main_page"), mouseX, mouseY);
        }

        if (this.handler.getPage() == Page.INFO && sc != null) {
            Text soulName = Text.translatable(sc.getSoulType().getTranslationKey());
            Text description = Text.translatable(sc.getSoulType().getTranslationKey() + ".description");

            int left = this.x + 31;
            int top = this.y + 19;
            int right = this.x + 145;
            int bottom = this.y + 124;

            int i = (left + right)/2 - (textRenderer.getWidth(soulName)/2);
            context.drawText(textRenderer, soulName, i, top, sc.isSoulless() ? Colors.CYAN : Colors.LIGHT_YELLOW, true);

            int j = top + 12;
            List<OrderedText> list = textRenderer.wrapLines(description, right - left);
            i = 0;
            for (OrderedText txt : list) {
                if (textRenderer.getWidth(txt) > i) {
                    i = textRenderer.getWidth(txt);
                }
            }
            i = (left + right)/2 - (i/2);
            for (int k = 0; k < list.size(); k++) {
                context.drawText(textRenderer, list.get(k), i, j + k * 10, Colors.WHITE, true);
            }
        } else if (this.handler.getPage() == Page.REPAIR) {

            int itemX = this.x + 65;
            int itemY = this.y + 45;
            for (ItemStack stack : firstRepairStacks) {
                context.drawItem(stack, itemX, itemY);
                context.drawStackOverlay(this.client.textRenderer, stack, itemX, itemY, stack.getCount() > 1 ? stack.getCount() + "" : null);

                if (mouseX > itemX && mouseX < itemX + 17 && mouseY > itemY && mouseY < itemY + 17) {
                    context.drawTooltip(this.client.textRenderer, stack.getTooltip(Item.TooltipContext.DEFAULT, client.player, client.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC), mouseX, mouseY);
                }

                itemY -= 18;

                if (itemY < this.y + 3) break;
            }

            itemX = this.x + 83;
            itemY = this.y + 45;
            for (ItemStack stack : secondRepairStacks) {
                context.drawItem(stack, itemX, itemY);
                context.drawStackOverlay(this.client.textRenderer, stack, itemX, itemY, stack.getCount() > 1 ? stack.getCount() + "" : null);

                if (mouseX > itemX && mouseX < itemX + 17 && mouseY > itemY && mouseY < itemY + 17) {
                    context.drawTooltip(this.client.textRenderer, stack.getTooltip(Item.TooltipContext.DEFAULT, client.player, client.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC), mouseX, mouseY);
                }

                itemY -= 18;

                if (itemY < this.y + 3) break;
            }

            if (xpButton.isSelected(mouseX, mouseY)) {
                context.drawTooltip(Text.translatable("container.hdemise.infusion.extract_xp").withColor(this.handler.canExtractExperience() ? Colors.WHITE : Colors.GRAY), mouseX, mouseY);
            }
        }

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (topUpButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, topUpButton.index)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.client.interactionManager.clickButton(this.handler.syncId, topUpButton.index);
            return true;
        }

        switch (this.handler.getPage()) {
            case MAIN -> {
                if (infoPageButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, infoPageButton.index)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    this.client.interactionManager.clickButton(this.handler.syncId, infoPageButton.index);
                    return true;
                }
                if (repairPageButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, repairPageButton.index)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    this.client.interactionManager.clickButton(this.handler.syncId, repairPageButton.index);
                    return true;
                }
            }
            case INFO, REPAIR -> {
                if (mainPageButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, mainPageButton.index)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
                    this.client.interactionManager.clickButton(this.handler.syncId, mainPageButton.index);
                    return true;
                }
            }
        }

        if (handler.getPage() == Page.REPAIR && xpButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, xpButton.index)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.client.interactionManager.clickButton(this.handler.syncId, xpButton.index);
            return true;
        }

        return super.mouseClicked(click, doubled);
    }

    public void updateRecipe(InfusionRecipePayload payload) {
        this.firstRepairStacks = payload.firstStacks();
        this.secondRepairStacks = payload.secondStacks();
    }

    private class Button {
        public final int index;
        private final int x;
        private final int y;
        public final int u;
        public final int v;
        public final int width;
        public final int height;

        private Button(int index, int x, int y, int u, int v, int width, int height) {
            this.index = index;
            this.x = x;
            this.y = y;
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return InfusionScreen.this.x + x;
        }

        public int getY() {
            return InfusionScreen.this.y + y;
        }

        public boolean isSelected(int mouseX, int mouseY) {
            return mouseX >= getX() && mouseX < getX() + width && mouseY >= getY() && mouseY < getY() + height;
        }

        public int getSelectedV() {
            return v + height;
        }

        public void draw(DrawContext context, Identifier texture, int mouseX, int mouseY) {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, getX(), getY(), u, isSelected(mouseX, mouseY) ? getSelectedV() : v, width,  height, 256, 256);
        }
    }
}
