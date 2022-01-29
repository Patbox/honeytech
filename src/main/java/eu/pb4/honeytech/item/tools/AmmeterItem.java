package eu.pb4.honeytech.item.tools;

import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.item.AdditionalItemActions;
import eu.pb4.honeytech.item.general.GlowingItem;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class AmmeterItem extends GlowingItem implements AdditionalItemActions {

    public AmmeterItem(Settings settings) {
        super(settings, Items.GOLDEN_HOE);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        if (!world.isClient && player != null) {
            BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
            if (blockEntity instanceof EnergyHolder holder) {
                player.sendMessage(
                        HTUtils.getText("gui", "battery_charge",
                                new LiteralText(HTUtils.formatEnergy(holder.getEnergy().getAmount())).formatted(Formatting.WHITE),
                                new LiteralText(HTUtils.formatEnergy(holder.getEnergy().getCapacity())).formatted(Formatting.WHITE),
                                new LiteralText(HTUtils.dtt(((double) holder.getEnergy().getAmount() / holder.getEnergy().getCapacity()) * 100) + "%").formatted(Formatting.WHITE)
                        ).formatted(Formatting.GRAY), true);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }
}
