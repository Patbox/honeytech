package eu.pb4.honeytech.block.machines_common;

import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.polymer.block.BasicVirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HandleBlock extends BasicVirtualBlock {
    public HandleBlock(Settings settings, Block virtualBlock) {
        super(settings.dropsNothing(), virtualBlock);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockPos down = pos.down();
        BlockEntity blockEntity = world.getBlockEntity(down);

        if (blockEntity instanceof HandlePoweredBlockEntity && hand != Hand.OFF_HAND) {
            return ((HandlePoweredBlockEntity) blockEntity).useHandle(world.getBlockState(down), world, pos.down(), player, hand, hit);
        }

        return ActionResult.FAIL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockPos down = pos.down();

            if (world.getBlockState(down).getBlock() instanceof BlockEntityProvider) {
                world.breakBlock(down, true);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }
}
