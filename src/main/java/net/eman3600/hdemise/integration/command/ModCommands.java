package net.eman3600.hdemise.integration.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {


    public static void registerAll() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            SoulCommand.register(dispatcher, registryAccess);
        });
    }
}
