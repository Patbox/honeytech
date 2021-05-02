package eu.pb4.honeytech.item.other;

import eu.pb4.honeytech.advancements.HTCriteria;
import eu.pb4.honeytech.gui.guide.GuideGui;
import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class GuideItem extends Item implements VirtualItem {
    public GuideItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));
        HTCriteria.OPEN_GUIDE.trigger((ServerPlayerEntity) user);
        new GuideGui((ServerPlayerEntity) user, GuideGui.MAIN_GUIDE).open();

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public Item getVirtualItem() {
        return Items.KNOWLEDGE_BOOK;
    }

    @Override
    public ItemStack getVirtualItemStack(ItemStack itemStack, ServerPlayerEntity player) {
        ItemStack out = VirtualItem.super.getVirtualItemStack(itemStack, player);
        out.addEnchantment(Enchantments.INFINITY, 0);
        out.getOrCreateTag().put("Recipes", new ListTag());
        return out;
    }


}
