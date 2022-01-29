package eu.pb4.honeytech.item.general;

import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class HeadItem extends Item implements PolymerItem {
    private final String texture;

    public HeadItem(Settings settings, String texture) {
        super(settings);
        this.texture = texture;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, ServerPlayerEntity player) {
        return Items.PLAYER_HEAD;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, ServerPlayerEntity player) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, player);
        out.getOrCreateNbt().put("SkullOwner", HTUtils.getHeadSkullOwnerTag(this.texture));
        return out;
    }
}
