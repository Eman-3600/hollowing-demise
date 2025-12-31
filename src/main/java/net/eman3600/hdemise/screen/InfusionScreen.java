package net.eman3600.hdemise.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.hdemise.HDemise;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.screen.InfusionScreenHandler.Page;
import net.eman3600.hdemise.soul_type.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

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

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.backgroundWidth = 176;
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = 129;

        this.topUpButton = new Button(0, 158, 124, 176, 18, 11, 11);
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
            }
            case Page.INFO -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, INFO_TEXTURE, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
            }
            case REPAIR -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, REPAIR_TEXTURE, x, y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
            }
        }

        if (sc.canTopUp()) {
            topUpButton.draw(context, MAIN_TEXTURE, mouseX, mouseY);
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

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        HDemise.LOGGER.info("Mouse click at {}, {}", click.x(), click.y());
        if (topUpButton.isSelected((int) click.x(), (int) click.y()) && this.handler.onButtonClick(client.player, 0)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
            return true;
        }

        return super.mouseClicked(click, doubled);
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
