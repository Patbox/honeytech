package eu.pb4.honeytech.item.general;

import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class HeadItem extends Item implements VirtualItem {
    private final String texture;

    public HeadItem(Settings settings, String texture) {
        super(settings);
        this.texture = texture;
    }

    @Override
    public Item getVirtualItem() {
        return Items.PLAYER_HEAD;
    }

    @Override
    public ItemStack getVirtualItemStack(ItemStack itemStack, ServerPlayerEntity player) {
        ItemStack out = VirtualItem.super.getVirtualItemStack(itemStack, player);
        out.getOrCreateTag().put("SkullOwner", HTUtils.getHeadSkullOwnerTag(this.texture));
        return out;
    }
}
