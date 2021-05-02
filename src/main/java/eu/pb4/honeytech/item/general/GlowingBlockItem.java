package eu.pb4.honeytech.item.general;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;


public class GlowingBlockItem extends BlockItem implements VirtualItem {
    private final Item item;
    public GlowingBlockItem(Block block, Item item, Settings settings) {
        super(block, settings);
        this.item = item;
    }

    protected boolean place(ItemPlacementContext context, BlockState state) {
        return super.place(context, state);
    }

    @Override
    public Item getVirtualItem() {
        return this.item;
    }

    @Override
    public ItemStack getVirtualItemStack(ItemStack itemStack, ServerPlayerEntity player) {
        ItemStack out = VirtualItem.super.getVirtualItemStack(itemStack, player);
        out.addEnchantment(Enchantments.INFINITY, 0);
        return out;
    }
}
