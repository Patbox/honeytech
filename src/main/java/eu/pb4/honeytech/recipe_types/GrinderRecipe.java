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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GrinderRecipe implements Recipe<Inventory> {
    private final Ingredient input;
    private final ItemStack outputStack;
    private final Identifier id;
    private final int requiredClicks;
    private final int requiredPower;

    public GrinderRecipe(Ingredient input, ItemStack outputStack, int requiredClicks, int requiredPower, Identifier id) {
        this.input = input;
        this.outputStack = outputStack;
        this.id = id;
        this.requiredClicks = requiredClicks;
        this.requiredPower = requiredPower;
    }

    public Ingredient getInput() {
        return this.input;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        for (int x = 0; x < inv.size(); x++) {
            ItemStack stack = inv.getStack(x).copy();
            if (!stack.isEmpty()) {
                if (this.input.test(stack)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
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

    public int getRequiredClicks() {
        return this.requiredClicks;
    }

    public int getRequiredPower() {
        return this.requiredPower;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        return DefaultedList.ofSize(1, this.input);
    }

    public static class Type implements RecipeType<GrinderRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();

        public static final String ID = "grinder_recipe";
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<GrinderRecipe>, VirtualObject {
        private Serializer() {
        }

        public static final Serializer INSTANCE = new Serializer();

        public static final Identifier ID = new Identifier("honeytech", "grinder_recipe");

        @Override
        public GrinderRecipe read(Identifier id, JsonObject json) {
            RecipeJsonFormat recipeJson = new Gson().fromJson(json, RecipeJsonFormat.class);

            Ingredient input = Ingredient.fromJson(recipeJson.input);
            Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem)).get();
            ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);
            return new GrinderRecipe(input, output, recipeJson.requiredClicks, recipeJson.requiredPower, id);
        }

        @Override
        public GrinderRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient inputA = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            int clicks = buf.readInt();
            int power = buf.readInt();

            return new GrinderRecipe(inputA, output, clicks, power, id);
        }

        @Override
        public void write(PacketByteBuf buf, GrinderRecipe recipe) {
            recipe.getInput().write(buf);
            buf.writeItemStack(recipe.getOutput());
            buf.writeInt(recipe.requiredClicks);
            buf.writeInt(recipe.requiredPower);
        }
    }

    static class RecipeJsonFormat {
        JsonObject input;
        String outputItem;
        int outputAmount;
        int requiredClicks = 1;
        int requiredPower = 0;
    }
}
