package net.eman3600.hdemise.event.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.eman3600.hdemise.util.inventory.SoulInventory;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Optional;

public class AugmentCriterion extends AbstractCriterion<AugmentCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, SoulInventory inventory) {
        trigger(player, conditions -> conditions.matches(inventory));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }


    public record Conditions(Optional<LootContextPredicate> player, List<Ingredient> augments) implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                                Ingredient.CODEC.listOf().fieldOf("augments").forGetter(Conditions::augments)
                        )
                        .apply(instance, Conditions::new)
        );

        public boolean matches(SoulInventory inventory) {
            outer: for (Ingredient augment : augments) {
                for (int i = 1; i < inventory.size(); i++) {
                    if (augment.test(inventory.getStack(i))) {
                        continue outer;
                    }
                }
                return false;
            }
            return true;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return this.player;
        }
    }
}
