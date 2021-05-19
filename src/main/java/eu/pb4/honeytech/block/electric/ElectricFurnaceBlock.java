package eu.pb4.honeytech.block.electric;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.electric.ElectricFurnaceBlockEntity;
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

import java.util.Collection;

public class ElectricFurnaceBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, MachineBlock, WrenchableBlock {
    public final HTTier tier;

    public ElectricFurnaceBlock(Settings settings, HTTier tier) {
        super(settings);
        this.tier = tier;
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.LIT, false));
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ElectricFurnaceBlockEntity) {
            ((ElectricFurnaceBlockEntity) entity).openInventory((ServerPlayerEntity) player);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.PLAYER_HEAD;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ElectricFurnaceBlockEntity();
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return Blocks.PLAYER_HEAD.getDefaultState().with(Properties.ROTATION, state.get(Properties.HORIZONTAL_FACING).getOpposite().getHorizontal() * 4);
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI5NmYwOTI1MjRhZTljMmEyZTg3ODgxY2I0MTVhZGIzNThkNmNiNzczYzg1ZGM5NzIwMmZlZmI3NTRjMSJ9fX0=";
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ElectricFurnaceBlockEntity) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateComparators(pos,this);

                for (ElectricFurnaceBlockEntity.Gui gui : ((ElectricFurnaceBlockEntity) blockEntity).openGuis) {
                    gui.close(false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(Properties.HORIZONTAL_FACING);
    }

    @Override
    public double getPerTickEnergyUsage() {
        return 128;
    }

    @Override
    public double getPerTickEnergyProduction() {
        return 0;
    }

    @Override
    public double getMaxEnergyOutput() {
        return 0;
    }

    @Override
    public double getMaxEnergyInput() {
        return 256;
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
