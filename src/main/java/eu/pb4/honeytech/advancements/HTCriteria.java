package eu.pb4.honeytech.advancements;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class HTCriteria {
    public static OpenGuideCriterion OPEN_GUIDE = new OpenGuideCriterion();


    public static void register() {
        CriterionRegistry.register(OPEN_GUIDE);
    }
}
