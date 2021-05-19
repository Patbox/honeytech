package eu.pb4.honeytech.block.electric;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.electric.CoalGeneratorBlockEntity;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CoalGeneratorBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, WrenchableBlock, MachineBlock {
    public final HTTier tier;
    public CoalGeneratorBlock(Settings settings, HTTier tier) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.LIT, false));
        this.tier = tier;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public Block getVirtualBlock() {
    return Blocks.PLAYER_HEAD;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CoalGeneratorBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof CoalGeneratorBlockEntity) {
            ((CoalGeneratorBlockEntity) entity).openInventory((ServerPlayerEntity) player);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CoalGeneratorBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos,this);

                for (CoalGeneratorBlockEntity.Gui gui : ((CoalGeneratorBlockEntity) blockEntity).openGuis) {
                    gui.close(false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return Blocks.PLAYER_HEAD.getDefaultState().with(Properties.ROTATION, state.get(Properties.HORIZONTAL_FACING).getOpposite().getHorizontal() * 4);
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE0YTJlYTIxZDZjOTY5MzhhMjcxZmNmZjUyM2E2NTA3YjQ1NGY4NGJhZDk1OTkzZjQ0OTJhNmZiYzMwOTRmNSJ9fX0=";
    }

    public static int getLightLevel(BlockState state) {
        return state.get(Properties.LIT) ? 10 : 0;
    }

    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(Properties.HORIZONTAL_FACING);
    }

    @Override
    public double getPerTickEnergyUsage() {
        return 0;
    }

    @Override
    public double getPerTickEnergyProduction() {
        return this.tier.energyCapacity / 16;
    }

    @Override
    public double getMaxEnergyOutput() {
        return this.getPerTickEnergyProduction();
    }

    @Override
    public double getMaxEnergyInput() {
        return 0;
    }

    @Override
    public double getCapacity() {
        return this.tier.energyCapacity;
    }

    @Override
    public HTTier getTier() {
        return this.tier;
    }
}
