package io.github.apace100.origins.util;

import com.google.gson.JsonObject;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class ChoseOriginCriterion extends AbstractCriterion<ChoseOriginCriterion.Conditions> {

    public static ChoseOriginCriterion INSTANCE = new ChoseOriginCriterion();
    private static final Identifier ID = new Identifier(Origins.MODID, "chose_origin");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate, JsonHelper.getString(obj, "origin", null), JsonHelper.getString(obj, "layer", null));
    }

    public void trigger(ServerPlayerEntity player, Origin origin) {
        this.trigger(player, conditions -> conditions.matches(origin));
    }

    public void trigger(ServerPlayerEntity player, Origin origin, OriginLayer layer) {
        this.trigger(player, conditions -> conditions.matches(origin, layer));
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public static class Conditions extends AbstractCriterionConditions {

        private final Identifier originId;
        private final Identifier layerId;

        public Conditions(EntityPredicate.Extended player, Identifier originId) {
            super(ChoseOriginCriterion.ID, player);
            this.originId = originId;
            this.layerId = null;
        }

        public Conditions(EntityPredicate.Extended player, String origin, String layer) {
            super(ChoseOriginCriterion.ID, player);
            this.originId = origin != null ? new Identifier(origin) : null;
            this.layerId = layer != null ? new Identifier(layer) : null;
        }

        public boolean matches(Origin origin) {
            return originId == null || origin.getIdentifier().equals(originId);
        }

        public boolean matches(Origin origin, OriginLayer layer) {
            return (originId == null || origin.getIdentifier().equals(originId))
                && (layerId == null || layer.getIdentifier().equals(layerId));
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);

            if (originId != null) jsonObject.addProperty("origin", originId.toString());
            if (layerId != null) jsonObject.addProperty("layer", layerId.toString());

            return jsonObject;
        }

    }

}
