package net.eman3600.hdemise;

import net.eman3600.hdemise.block.entity.renderer.InfusionTableBlockEntityRenderer;
import net.eman3600.hdemise.event.KeyInputHandler;
import net.eman3600.hdemise.init.entity.ModBlockEntities;
import net.eman3600.hdemise.init.entity.ModEntityModelLayers;
import net.eman3600.hdemise.init.event.ModMessages;
import net.eman3600.hdemise.init.event.ModScreenHandlerTypes;
import net.eman3600.hdemise.screen.InfusionScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.mixin.client.rendering.BlockEntityRenderersMixin;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

public class HDemiseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.registerBindings();
        KeyInputHandler.registerKeyInputs();

        ModMessages.registerS2CReceivers();

        ModEntityModelLayers.registerAll();


        HandledScreens.register(ModScreenHandlerTypes.INFUSION, InfusionScreen::new);


        BlockEntityRendererFactories.register(ModBlockEntities.INFUSION_TABLE_BLOCK_ENTITY, InfusionTableBlockEntityRenderer::new);
    }
}
