package net.eman3600.hdemise.init.basics;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.eman3600.hdemise.HDemise;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.*;

import java.util.function.ToIntFunction;

import static net.eman3600.hdemise.HDemise.MODID;

public class ModGameRules {

    public static final GameRule<Boolean> UNBREAKABLE_SOULS = registerBooleanRule("unbreakable_souls", GameRuleCategory.PLAYER, false);
    public static final GameRule<Boolean> IMPROVE_AIR_SPEED = registerBooleanRule("improve_air_speed", GameRuleCategory.PLAYER, true);




    private static <T> GameRule<T> register(
            String name,
            GameRuleCategory category,
            GameRuleType type,
            ArgumentType<T> argumentType,
            Codec<T> codec,
            T defaultValue,
            FeatureSet requiredFeatures,
            GameRules.Acceptor<T> acceptor,
            ToIntFunction<T> commandResultSupplier
    ) {
        return Registry.register(
                Registries.GAME_RULE, Identifier.of(MODID, name), new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures)
        );
    }

    private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
        return register(
                name,
                category,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                Codec.BOOL,
                defaultValue,
                FeatureSet.empty(),
                GameRuleVisitor::visitBoolean,
                value -> value ? 1 : 0
        );
    }

    public static void registerGameRules() {
        HDemise.LOGGER.info("Registering gamerules for {}", MODID);
    }
}
