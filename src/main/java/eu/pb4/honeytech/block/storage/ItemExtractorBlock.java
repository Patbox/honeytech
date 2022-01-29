package eu.pb4.honeytech.block.storage;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.storage.ItemExtractorBlockEntity;
import eu.pb4.honeytech.blockentity.storage.PipeBlockEntity;
import eu.pb4.polymer.api.block.PolymerBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ItemExtractorBlock extends Block implements BlockEntityProvider, PolymerBlock, WrenchableBlock {
    public static DirectionProperty INPUT_DIR = DirectionProperty.of("input", Direction.values());
    public static DirectionProperty OUTPUT_DIR = DirectionProperty.of("output", Direction.values());

    public ItemExtractorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(INPUT_DIR, Direction.UP).with(INPUT_DIR, Direction.DOWN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(INPUT_DIR, OUTPUT_DIR, Properties.WATERLOGGED);
    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.GRINDSTONE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState out = this.getDefaultState().with(ItemExtractorBlock.INPUT_DIR, ctx.getPlayerFacing()).with(ItemExtractorBlock.OUTPUT_DIR, Direction.UP);

        if (blockPos.getY() < ctx.getWorld().getHeight()) {
            for (Direction dir : Direction.values()) {
                BlockPos updatePos = ctx.getBlockPos().add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());

                BlockState state = ctx.getWorld().getBlockState(updatePos);

                BlockEntity entity = ctx.getWorld().getBlockEntity(updatePos);

                if (!(entity instanceof PipeBlockEntity) && (entity instanceof Inventory || (entity instanceof InventoryProvider && ((InventoryProvider) entity).getInventory(state, ctx.getWorld(), updatePos).getAvailableSlots(dir.getOpposite()).length > 0))) {
                    out = out.with(ItemExtractorBlock.INPUT_DIR, dir);
                    out = out.with(ItemExtractorBlock.OUTPUT_DIR, dir.getOpposite());
                }
            }

            return out;
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {

        for (Direction dir : Direction.values()) {
            BlockPos updatePos = pos.add(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());

            BlockState updateState = world.getBlockState(updatePos);
            if (updateState.getBlock() instanceof PipeBlock) {
                world.setBlockState(updatePos, updateState.with(PipeBlock.INPUT_DIR, dir.getOpposite()), 3);
                return;
            }
        }
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {

        Direction input = state.get(INPUT_DIR);

        if (input.getAxis() == Direction.Axis.Y) {
            return Blocks.GRINDSTONE.getDefaultState().with(GrindstoneBlock.FACE, input.getDirection() == Direction.AxisDirection.POSITIVE ? WallMountLocation.CEILING : WallMountLocation.FLOOR);
        }
        return Blocks.GRINDSTONE.getDefaultState()
                .with(GrindstoneBlock.FACE, WallMountLocation.WALL)
                .with(GrindstoneBlock.FACING, input.getOpposite());

    }


    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(INPUT_DIR, OUTPUT_DIR);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemExtractorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ItemExtractorBlockEntity::tick;
    }
}
