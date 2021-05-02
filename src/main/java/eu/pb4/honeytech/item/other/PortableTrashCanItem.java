package eu.pb4.honeytech.item.other;

import eu.pb4.honeytech.blockentity.basic_machines.TrashCanBlockEntity;
import eu.pb4.honeytech.item.general.HeadItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class PortableTrashCanItem extends HeadItem {
    private static String TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQxZjRlZGJjNjhjOTA2MTM1NTI0MmJkNzNlZmZjOTI5OWEzMjUyYjlmMTFlODJiNWYxZWM3YjNiNmFjMCJ9fX0=";

    public PortableTrashCanItem(Settings settings) {
    super(settings, TEXTURE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return TypedActionResult.fail(user.getStackInHand(hand));
        new TrashCanBlockEntity.TrashCanGui(null, (ServerPlayerEntity) user).open();
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        this.use(context.getWorld(), context.getPlayer(), context.getHand());
        return super.useOnBlock(context);
    }
}
