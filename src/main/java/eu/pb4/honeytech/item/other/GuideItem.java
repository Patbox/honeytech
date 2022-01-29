package eu.pb4.honeytech.item.other;

import eu.pb4.honeytech.advancements.HTCriteria;
import eu.pb4.honeytech.gui.guide.GuideGui;
import eu.pb4.polymer.api.item.PolymerItem;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class GuideItem extends Item implements PolymerItem {
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
    public Item getPolymerItem(ItemStack itemStack, ServerPlayerEntity player) {
        return Items.KNOWLEDGE_BOOK;
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack itemStack, ServerPlayerEntity player) {
        ItemStack out = PolymerItem.super.getPolymerItemStack(itemStack, player);
        out.addEnchantment(Enchantments.INFINITY, 0);
        out.getOrCreateNbt().put("Recipes", new NbtList());
        return out;
    }


}
