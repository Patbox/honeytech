package eu.pb4.honeytech.advancements;

import com.google.gson.JsonObject;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate.Extended;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class OpenGuideCriterion extends AbstractCriterion<OpenGuideCriterion.Conditions> {
    private static final Identifier ID = HTUtils.id("open_guide");

    public OpenGuideCriterion() {
    }

    public Identifier getId() {
        return ID;
    }

    public Conditions conditionsFromJson(JsonObject jsonObject, Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        return new Conditions(extended, ItemPredicate.ANY);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, (conditions) -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(Extended player, ItemPredicate item) {
            super(OpenGuideCriterion.ID, player);
        }

        public static Conditions any() {
            return new Conditions(Extended.EMPTY, ItemPredicate.ANY);
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            return jsonObject;
        }
    }
}
