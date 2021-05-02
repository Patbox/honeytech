package eu.pb4.honeytech.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface AdditionalItemActions {
    default ActionResult preMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) { return ActionResult.PASS; };
}
