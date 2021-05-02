package eu.pb4.honeytech.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Ingredient.class)
public interface IngredientAccessor {
    @Invoker("cacheMatchingStacks")
    void invokeCacheMatchingStacks();

    @Accessor("matchingStacks")
    ItemStack[] getMatchingStacks();
}
