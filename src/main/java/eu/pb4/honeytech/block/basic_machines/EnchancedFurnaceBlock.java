package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.blockentity.basic_machines.EnchancedFurnaceBlockEntity;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnchancedFurnaceBlockEntity(pos, state);
    }
}
