package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.blockentity.basic_machines.EnchancedFurnaceBlockEntity;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EnchancedFurnaceBlock extends AbstractFurnaceBlock implements VirtualBlock {
    public final float speedMulti;
    public final float burnMulti;

    public EnchancedFurnaceBlock(Settings settings, float speedMulti, float burnMulti) {
        super(settings);
        this.speedMulti = speedMulti;
        this.burnMulti = burnMulti;
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EnchancedFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.FURNACE;
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return Blocks.FURNACE.getDefaultState()
                .with(AbstractFurnaceBlock.FACING, state.get(AbstractFurnaceBlock.FACING))
                .with(AbstractFurnaceBlock.LIT, state.get(AbstractFurnaceBlock.LIT));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new EnchancedFurnaceBlockEntity();
    }

}
