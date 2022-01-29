package eu.pb4.honeytech.item.general;

import eu.pb4.polymer.api.item.PolymerItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;


public class GlowingBlockItem extends BlockItem implements PolymerItem {
    private final Item item;
    public GlowingBlockItem(Block block, Item item, Settings settings) {
        super(block, settings);
        this.item = item;
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return super.place(context, state);
    }

    @Override
    public Item getPolymerItem(ItemStack stack, @Nullable ServerPlayerEntity player) {
        return this.item;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, @Nullable ServerPlayerEntity player) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, player);
        out.addEnchantment(Enchantments.INFINITY, 0);
        return out;
    }
}
