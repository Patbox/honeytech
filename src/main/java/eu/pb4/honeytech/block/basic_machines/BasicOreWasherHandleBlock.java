package eu.pb4.honeytech.block.basic_machines;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.block.machines_common.HandleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;

import java.util.Collection;

public class BasicOreWasherHandleBlock extends HandleBlock implements WrenchableBlock {

    public BasicOreWasherHandleBlock(Settings settings) {
        super(settings, Blocks.OAK_TRAPDOOR);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(HorizontalFacingBlock.FACING);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.OAK_TRAPDOOR.getDefaultState().with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING));
    }

    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(HorizontalFacingBlock.FACING);
    }
}
