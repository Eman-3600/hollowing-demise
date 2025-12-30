package net.eman3600.hdemise.integration.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.item.XPCoreItem;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.soul_type.SoulTypeRegistry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SoulCommand {

    private static final DynamicCommandExceptionType SOUL_TYPE_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("soul_type.soulTypeNotFound", id)
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {

        LiteralArgumentBuilder<ServerCommandSource> root = CommandManager.literal("hdemise:soul").requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK));

        root.then(CommandManager.literal("set")
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("type", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, SoulTypeRegistry.KEY))
                                .executes(SoulCommand::setSoulType))));


        dispatcher.register(
                root
        );
    }

    private static int setSoulType(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        int affected = 0;
        RegistryEntry<SoulType> typeKey = RegistryEntryReferenceArgumentType.getRegistryEntry(context, "type", SoulTypeRegistry.KEY);
        SoulType type = typeKey.value();

        for (PlayerEntity player : EntityArgumentType.getPlayers(context, "targets")) {
            SoulComponent sc = SoulComponent.of(player);

            if (sc.getSoulType() != type) {
                XPCoreItem.extractToWorld(player);
                sc.setSoulType(type);
                sc.setSoul(sc.getMaxSoul());
                player.setHealth(player.getMaxHealth());
                HungerManager hg = player.getHungerManager();
                hg.setFoodLevel(20);
                hg.setSaturationLevel(20f);
                affected++;
            }
        }

        if (affected > 0) {
            int finalAffected = affected;
            context.getSource().sendFeedback(() -> Text.translatable("commands.hdemise.soul.set_success", finalAffected, Text.translatable(type.getTranslationKey())), true);
        } else {
            context.getSource().sendError(Text.translatable("commands.hdemise.soul.set_none", Text.translatable(type.getTranslationKey())));
        }


        return affected;
    }
}
