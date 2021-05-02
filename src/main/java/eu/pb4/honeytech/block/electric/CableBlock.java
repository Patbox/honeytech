package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.electric.CableBlockEntity;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class CableBlock extends Block implements VirtualBlock, BlockEntityProvider {
    public CableBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
       builder.add(Properties.NORTH, Properties.SOUTH, Properties.WEST, Properties.EAST, Properties.UP, Properties.DOWN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockView world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        return super.getPlacementState(ctx)
                .with(Properties.UP, world.getBlockEntity(pos.up()) instanceof EnergyHolder)
                .with(Properties.DOWN, world.getBlockEntity(pos.down()) instanceof EnergyHolder)
                .with(Properties.NORTH, world.getBlockEntity(pos.north()) instanceof EnergyHolder)
                .with(Properties.SOUTH, world.getBlockEntity(pos.south()) instanceof EnergyHolder)
                .with(Properties.WEST, world.getBlockEntity(pos.west()) instanceof EnergyHolder)
                .with(Properties.EAST, world.getBlockEntity(pos.east()) instanceof EnergyHolder);

    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return state.with(this.propertyFromDir(direction), newState.getBlock() != Blocks.AIR);
    }

    private BooleanProperty propertyFromDir(Direction dir) {
        switch (dir) {
            case WEST:
                return Properties.WEST;
            case EAST:
                return Properties.EAST;
            case SOUTH:
                return Properties.SOUTH;
            case NORTH:
                return Properties.NORTH;
            case DOWN:
                return Properties.DOWN;
            case UP:
                return Properties.UP;
        }

        return Properties.NORTH;
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.NETHER_BRICK_FENCE;
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return Blocks.NETHER_BRICK_FENCE.getDefaultState()
                .with(Properties.NORTH, state.get(Properties.NORTH))
                .with(Properties.SOUTH, state.get(Properties.SOUTH))
                .with(Properties.WEST, state.get(Properties.WEST))
                .with(Properties.EAST, state.get(Properties.EAST));

    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CableBlockEntity();
    }
}
