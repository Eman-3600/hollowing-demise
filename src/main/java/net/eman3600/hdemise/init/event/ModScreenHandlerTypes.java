package net.eman3600.hdemise.init.event;

import net.eman3600.hdemise.screen.InfusionScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModScreenHandlerTypes {

    public static final ScreenHandlerType<InfusionScreenHandler> INFUSION = register("infusion", InfusionScreenHandler::new);



    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MODID, id), new ScreenHandlerType<>(factory, FeatureSet.empty()));
    }

    public static void registerAll() {
        System.out.println("Registering screen handler types for " + MODID);
    }
}
