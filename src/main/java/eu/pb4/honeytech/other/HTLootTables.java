package eu.pb4.honeytech.other;

import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;

import java.util.function.Consumer;

public class HTLootTables {
    public static final LootContextType ORE_WASHING_CONTEXT = registerContextType("ore_washing", (builder) -> {
        builder.allow(LootContextParameters.THIS_ENTITY).require(LootContextParameters.BLOCK_ENTITY);
    });


    private static LootContextType registerContextType(String name, Consumer<LootContextType.Builder> type) {
        LootContextType.Builder builder = new LootContextType.Builder();
        type.accept(builder);
        LootContextType lootContextType = builder.build();
        return lootContextType;
    }
}
