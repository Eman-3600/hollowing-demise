package net.eman3600.hdemise.event.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eman3600.hdemise.cardinal_components.SoulComponent;
import net.eman3600.hdemise.soul_type.SoulType;
import net.eman3600.hdemise.soul_type.SoulTypeRegistry;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class SoulTypeChangedCriterion extends AbstractCriterion<SoulTypeChangedCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, SoulType type) {
        super.trigger(player, conditions -> conditions.matches(type));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, SoulType type) implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                SoulTypeRegistry.REGISTRY.getCodec().fieldOf("type").forGetter(Conditions::type)
                        )
                        .apply(instance, Conditions::new)
        );

        public boolean matches(SoulType type) {
            return type == this.type;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return this.player;
        }
    }
}
