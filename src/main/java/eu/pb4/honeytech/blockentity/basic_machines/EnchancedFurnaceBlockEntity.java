package eu.pb4.honeytech.blockentity.basic_machines;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.block.basic_machines.EnchancedFurnaceBlock;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class EnchancedFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    public EnchancedFurnaceBlockEntity() {
        super(HTBlockEntities.ENCHANCED_FURNACE, RecipeType.SMELTING);
    }

    protected Text getContainerName() {
        return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (int) (createFuelTimeMap().getOrDefault(item, 0) * ((EnchancedFurnaceBlock) this.getCachedState().getBlock()).burnMulti);
        }
    }

    @Override
    protected int getCookTime() {
        return (int) (this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200) / ((EnchancedFurnaceBlock) this.getCachedState().getBlock()).speedMulti);
    }
}
