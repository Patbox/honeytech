package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.honeytech.blockentity.basic_machines.TrashCanBlockEntity;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrashCanBlock extends Block implements BlockEntityProvider, HandlePoweredBlockEntity, VirtualHeadBlock {
    public TrashCanBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof TrashCanBlockEntity) {
            ((TrashCanBlockEntity) entity).openInventory((ServerPlayerEntity) player);
        }
        return ActionResult.SUCCESS;

    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TrashCanBlockEntity) {
                world.updateComparators(pos,this);

                for (TrashCanBlockEntity.TrashCanGui gui : ((TrashCanBlockEntity) blockEntity).openGuis) {
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

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQxZjRlZGJjNjhjOTA2MTM1NTI0MmJkNzNlZmZjOTI5OWEzMjUyYjlmMTFlODJiNWYxZWM3YjNiNmFjMCJ9fX0=";
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.PLAYER_HEAD;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrashCanBlockEntity(pos, state);
    }
}
