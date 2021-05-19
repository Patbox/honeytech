package eu.pb4.honeytech.other;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.tuple.Pair;

import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.loot.JLootTable.*;

public class HTDataPack {
    public static final RuntimeResourcePack PACK = RuntimeResourcePack.create("honeytech:main");

    public static void createSimpleDrop(Block block) {
        PACK.addLootTable(id(block.getLootTableId().toString()),
                loot("minecraft:block")
                        .pool(pool()
                                .rolls(1)
                                .entry(entry()
                                        .type("minecraft:item")
                                        .name(Registry.ITEM.getId(block.asItem()).toString()))
                                .condition(predicate("minecraft:survives_explosion")))
        );
    }

    public static void createCraftingShaped(Item item, int count, String rows, Pair<String, Object>... pairs) {
        String[] correctRows = rows.split("\\|");

        JKeys keys = JKeys.keys();
        for (Pair<String, Object> pair : pairs) {
            JIngredient ingredient = JIngredient.ingredient();

            if (pair.getRight() instanceof Item) {
                ingredient.item((Item) pair.getRight());
            } else {
                ingredient.tag((String) pair.getRight());

            }

            keys = keys.key(pair.getLeft(), ingredient);
        }

        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "craft_" + identifier.getPath()),
                JRecipe.shaped(
                        JPattern.pattern(correctRows),
                        keys,
                        JResult.itemStack(item, count)
                ));
    }

    public static void createCraftingShapeless(Item item, int count, Object... ingredients) {
        JIngredients jIngredients = JIngredients.ingredients();

        for (Object obj : ingredients) {
            JIngredient ingredient = JIngredient.ingredient();

            if (obj instanceof Item) {
                ingredient.item((Item) obj);
            } else {
                ingredient.tag((String) obj);
            }

            jIngredients.add(ingredient);
        }

        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "craft_" + identifier.getPath()),
                JRecipe.shapeless(
                        jIngredients,
                        JResult.item(item)
                ));
    }

    public static void createSmeltingRecipe(Item item, Object ingredient, int time, double xp) {
        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "furnace_" + identifier.getPath()),
                JRecipe.smelting(
                        ingredient instanceof Item
                                ? JIngredient.ingredient().item((Item) ingredient)
                                : JIngredient.ingredient().tag((String) ingredient),
                        JResult.item(item)
                ));
    }

    public static void createBlastingRecipe(Item item, Object ingredient, int time, double xp) {
        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "blast_furnace_" + identifier.getPath()),
                JRecipe.blasting(
                        ingredient instanceof Item
                                ? JIngredient.ingredient().item((Item) ingredient)
                                : JIngredient.ingredient().tag((String) ingredient),
                        JResult.item(item)
                ));
    }

    public static void createSmokerRecipe(Item item, Object ingredient, int time, double xp) {
        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "smoker_" + identifier.getPath()),
                JRecipe.smoking(
                        ingredient instanceof Item
                                ? JIngredient.ingredient().item((Item) ingredient)
                                : JIngredient.ingredient().tag((String) ingredient),
                        JResult.item(item)
                ));
    }

    public static void createCampfireRecipe(Item item, Object ingredient, int time, double xp) {
        Identifier identifier = Registry.ITEM.getId(item);

        PACK.addRecipe(new Identifier(identifier.getNamespace(), "smoker_" + identifier.getPath()),
                JRecipe.campfire(
                        ingredient instanceof Item
                                ? JIngredient.ingredient().item((Item) ingredient)
                                : JIngredient.ingredient().tag((String) ingredient),
                        JResult.item(item)
                ));
    }

    public static void create() {
        RRPCallback.EVENT.register(a -> a.add(PACK));
    }
}
