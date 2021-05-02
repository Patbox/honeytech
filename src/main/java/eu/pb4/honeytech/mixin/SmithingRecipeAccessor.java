package eu.pb4.honeytech.mixin;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingRecipe.class)
public interface SmithingRecipeAccessor {
    @Accessor("base")
    Ingredient getBase();

    @Accessor("addition")
    Ingredient getAddition();
}
