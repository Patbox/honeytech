package eu.pb4.honeytech.block.machines_common;

import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.honeytech.blockentity.machines_common.OreWasherBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OreWasherBlock extends Block implements BlockEntityProvider, HandlePoweredBlockEntity {
    public static final IntProperty WATER = IntProperty.of("part", 0, 3);


    public OreWasherBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(WATER, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(WATER);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof OreWasherBlockEntity) {
            OreWasherBlockEntity oreWasher = (OreWasherBlockEntity) entity;
            oreWasher.openInventory((ServerPlayerEntity) player);
        }
        return ActionResult.SUCCESS;

    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof OreWasherBlockEntity) {
                ItemScatterer.spawn(world, pos, (OreWasherBlockEntity) blockEntity);
                world.updateComparators(pos,this);

                for (OreWasherBlockEntity.OreWasherGui gui : ((OreWasherBlockEntity) blockEntity).openGuis) {
                    gui.close(false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }


    @Override
    public ActionResult useHandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OreWasherBlockEntity(pos, state);
    }
}
