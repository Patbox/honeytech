package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.block.machines_common.HandleBlock;
import eu.pb4.honeytech.block.machines_common.OreWasherBlock;
import eu.pb4.polymer.api.block.PolymerBlock;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BasicOreWasherBlock extends OreWasherBlock implements PolymerBlock {
    public BasicOreWasherBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.CAULDRON;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        int water = state.get(WATER);
        return water == 0
                ? Blocks.CAULDRON.getDefaultState()
                : Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, water);
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
        world.setBlockState(pos.up(), HTBlocks.BASIC_ORE_WASHER_HANDLE.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.fromRotation(placer.getYaw()).getOpposite()), 3);
    }
}
