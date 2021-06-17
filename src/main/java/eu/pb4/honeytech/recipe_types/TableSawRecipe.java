package eu.pb4.honeytech.recipe_types;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class TableSawRecipe implements Recipe<Inventory> {
    private final Ingredient input;
    private final ItemStack outputStack;
    private final Identifier id;


    public TableSawRecipe(Ingredient input, ItemStack outputStack, Identifier id) {
        this.input = input;
        this.outputStack = outputStack;
        this.id = id;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        return input.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return this.outputStack;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<TableSawRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();

        public static final String ID = "table_saw_recipe";
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<TableSawRecipe>, VirtualObject {
        private Serializer() {
        }

        public static final Serializer INSTANCE = new Serializer();

        public static final Identifier ID = new Identifier("honeytech", "table_saw");

        @Override
        public TableSawRecipe read(Identifier id, JsonObject json) {
            RecipeJsonFormat recipeJson = new Gson().fromJson(json, RecipeJsonFormat.class);

            Ingredient input = Ingredient.fromJson(recipeJson.input);
            Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem)).get();
            ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);
            return new TableSawRecipe(input, output, id);
        }

        @Override
        public TableSawRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient inputA = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();

            return new TableSawRecipe(inputA, output, id);
        }

        @Override
        public void write(PacketByteBuf buf, TableSawRecipe recipe) {
            recipe.getInput().write(buf);
            buf.writeItemStack(recipe.getOutput());
        }
    }

    static class RecipeJsonFormat {
        JsonObject input;
        String outputItem;
        int outputAmount;
    }
}
