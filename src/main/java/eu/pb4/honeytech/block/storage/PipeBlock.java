package eu.pb4.honeytech.block.storage;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.storage.PipeBlockEntity;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PipeBlock extends Block implements Waterloggable, BlockEntityProvider, VirtualBlock, WrenchableBlock {
    public static DirectionProperty INPUT_DIR = DirectionProperty.of("input", Direction.values());
    public static DirectionProperty OUTPUT_DIR = DirectionProperty.of("output", Direction.values());

    public PipeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(INPUT_DIR, Direction.UP).with(INPUT_DIR, Direction.DOWN).with(Properties.WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(INPUT_DIR, OUTPUT_DIR, Properties.WATERLOGGED);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PipeBlockEntity) {
                ItemScatterer.spawn(world, pos, (PipeBlockEntity) blockEntity);
                world.updateComparators(pos,this);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.DIORITE_WALL;
    }

    @Override
    public BlockState getDefaultVirtualBlockState() {
        return Blocks.DIORITE_WALL.getDefaultState();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState out = this.getDefaultState().with(PipeBlock.INPUT_DIR, ctx.getPlayerLookDirection()).with(PipeBlock.OUTPUT_DIR, ctx.getPlayerLookDirection().getOpposite());

        if (blockPos.getY() < ctx.getWorld().getHeight()) {
            boolean extrFound = false;
            boolean hasOutput = false;

            for (Direction dirBase : ctx.getPlacementDirections()) {
                Direction dir = dirBase.getOpposite();
                BlockPos updatePos = ctx.getBlockPos().add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());

                BlockState state = ctx.getWorld().getBlockState(updatePos);

                if (state.getBlock() instanceof ItemExtractorBlock) {
                    out = out.with(PipeBlock.INPUT_DIR, dir);
                    extrFound = true;
                }

                if (!extrFound && state.getBlock() instanceof PipeBlock) {
                    out = out.with(PipeBlock.INPUT_DIR, dir);
                }

                BlockEntity entity = ctx.getWorld().getBlockEntity(updatePos);
                if (!hasOutput && dir != ctx.getPlayerLookDirection() && (entity instanceof Inventory || (entity instanceof InventoryProvider && ((InventoryProvider) entity).getInventory(state, ctx.getWorld(), updatePos).getAvailableSlots(dir.getOpposite()).length > 0))) {
                    out = out.with(PipeBlock.OUTPUT_DIR, dir);
                    hasOutput = true;
                }
            }

            for (Direction dir : Direction.values()) {
                BlockPos updatePos = ctx.getBlockPos().add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());

                BlockState state = ctx.getWorld().getBlockState(updatePos);

                if (state.getBlock() instanceof ItemExtractorBlock && state.get(ItemExtractorBlock.INPUT_DIR) == dir) {
                    out = out.with(PipeBlock.OUTPUT_DIR, dir.getOpposite());
                    extrFound = true;
                }

                if (!extrFound && state.getBlock() instanceof PipeBlock && state.get(PipeBlock.INPUT_DIR) == dir) {
                    out = out.with(PipeBlock.OUTPUT_DIR, dir.getOpposite());
                }

                BlockEntity entity = ctx.getWorld().getBlockEntity(updatePos);
                if (!hasOutput && dir != ctx.getPlayerLookDirection() && (entity instanceof Inventory || (entity instanceof InventoryProvider && ((InventoryProvider) entity).getInventory(state, ctx.getWorld(), updatePos).getAvailableSlots(dir.getOpposite()).length > 0))) {
                    out = out.with(PipeBlock.OUTPUT_DIR, dir);
                    hasOutput = true;
                }
            }

            return out;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        Direction dir = state.get(INPUT_DIR);

        BlockPos updatePos = pos.add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());

        BlockState updateState = world.getBlockState(updatePos);

        if (updateState.getBlock() instanceof PipeBlock) {
            world.setBlockState(updatePos, updateState.with(PipeBlock.OUTPUT_DIR, dir.getOpposite()), 3);
        } else if (updateState.getBlock() instanceof ItemExtractorBlock) {
            world.setBlockState(updatePos, updateState.with(ItemExtractorBlock.OUTPUT_DIR, dir.getOpposite()), 3);
        }
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        Direction input = state.get(INPUT_DIR);
        Direction output = state.get(OUTPUT_DIR);


        if (output.getAxis() == Direction.Axis.Y && input.getAxis() == Direction.Axis.Y) {
            return (output.getDirection() == Direction.AxisDirection.POSITIVE ? Blocks.DIORITE_WALL : Blocks.ANDESITE_WALL).getDefaultState()
                    .with(WallBlock.UP, true)
                    .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
        } else if (output.getAxis() == Direction.Axis.Y) {
            return (output.getDirection() == Direction.AxisDirection.POSITIVE ? Blocks.DIORITE_STAIRS : Blocks.ANDESITE_STAIRS).getDefaultState()
                    .with(StairsBlock.HALF, output.getDirection() == Direction.AxisDirection.POSITIVE ? BlockHalf.TOP : BlockHalf.BOTTOM)
                    .with(StairsBlock.FACING, input)
                    .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
        } else if (input.getAxis() == Direction.Axis.Y) {
            return (output.getDirection() == Direction.AxisDirection.POSITIVE ? Blocks.DIORITE_STAIRS : Blocks.ANDESITE_STAIRS).getDefaultState()
                    .with(StairsBlock.HALF, input.getDirection() == Direction.AxisDirection.POSITIVE ? BlockHalf.TOP : BlockHalf.BOTTOM)
                    .with(StairsBlock.FACING, output)
                    .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
        } else {
            return (output.getDirection() == Direction.AxisDirection.POSITIVE ? Blocks.DIORITE_WALL : Blocks.ANDESITE_WALL).getDefaultState()
                    .with(WallBlock.UP, input.getAxis() != output.getAxis() || input == output)
                    .with(WallBlock.EAST_SHAPE, input == Direction.EAST || output == Direction.EAST ? WallShape.LOW : WallShape.NONE)
                    .with(WallBlock.WEST_SHAPE, input == Direction.WEST || output == Direction.WEST ? WallShape.LOW : WallShape.NONE)
                    .with(WallBlock.NORTH_SHAPE, input == Direction.NORTH || output == Direction.NORTH ? WallShape.LOW : WallShape.NONE)
                    .with(WallBlock.SOUTH_SHAPE, input == Direction.SOUTH || output == Direction.SOUTH ? WallShape.LOW : WallShape.NONE)
                    .with(Properties.WATERLOGGED, state.get(Properties.WATERLOGGED));
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.get(Properties.WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(INPUT_DIR, OUTPUT_DIR);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PipeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return PipeBlockEntity::tick;
    }
}
