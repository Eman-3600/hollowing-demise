package net.eman3600.hdemise.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.screen.InfusionScreenHandler.Page;
import net.eman3600.hdemise.soul_type.SoulType;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class InfusionScreen extends HandledScreen<InfusionScreenHandler> {

    private static final Identifier MAIN_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/main.png");
    private static final Identifier INFO_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/info.png");
    private static final Identifier REPAIR_TEXTURE = Identifier.of(MODID, "textures/gui/container/infusion_table/repair.png");

    public InfusionScreen(InfusionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        this.backgroundWidth = 176;
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = 129;
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        SoulComponent sc = SoulComponent.of(client.player);
        SoulType soulType = sc.getSoulType();

        switch (handler.getPage()) {
            case Page.MAIN -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, MAIN_TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
                Identifier soulTexture = Identifier.of(soulType.getId().getNamespace(), "textures/gui/container/soul/" + soulType.getId().getPath() + ".png");

                context.drawTexture(RenderPipelines.GUI_TEXTURED, soulTexture, i + 4, j + 4, 0f, 0f, 168, 133, 168, 133);
            }
            case Page.INFO -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, INFO_TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
            }
            case REPAIR -> {

                context.drawTexture(RenderPipelines.GUI_TEXTURED, REPAIR_TEXTURE, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
            }
        }
    }
}
