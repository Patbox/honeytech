package eu.pb4.honeytech.item.tools;

import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.item.AdditionalItemActions;
import eu.pb4.honeytech.item.general.GlowingItem;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

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
            if (blockEntity instanceof EnergyHolder) {
                EnergyHolder holder = (EnergyHolder) blockEntity;
                player.sendMessage(
                        HTUtils.getText("gui", "battery_charge",
                                new LiteralText(HTUtils.formatEnergy(holder.getEnergyAmount())).formatted(Formatting.WHITE),
                                new LiteralText(HTUtils.formatEnergy(holder.getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                                new LiteralText(HTUtils.dtt(holder.getEnergyAmount() / holder.getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                        ).formatted(Formatting.GRAY), true);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.FAIL;
    }
}
