package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.block.machines_common.HandleBlock;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BasicGrinderBlock extends GrinderBlock implements VirtualBlock {
    public BasicGrinderBlock(Settings settings) {
        super(settings, 1);
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.DISPENSER;
    }

    @Override
    public BlockState getDefaultVirtualBlockState() {
        return Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, Direction.UP);
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return this.getDefaultVirtualBlockState();
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (state.getBlock() != newState.getBlock()) {
            BlockPos above = pos.up();

            if (world.getBlockState(above).getBlock() instanceof HandleBlock) {
                world.setBlockState(above, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        return blockPos.getY() < ctx.getWorld().getHeight() && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx) ? super.getPlacementState(ctx) : null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), HTBlocks.BASIC_GRINDER_HANDLE.getDefaultState(), 3);
    }
}
