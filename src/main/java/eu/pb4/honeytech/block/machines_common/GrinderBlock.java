package eu.pb4.honeytech.block.machines_common;

import eu.pb4.honeytech.blockentity.machines_common.GrinderBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends Block implements BlockEntityProvider {
    public final int grindingPower;

    public GrinderBlock(Settings settings, int grindingPower) {
        super(settings);
        this.grindingPower = grindingPower;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof GrinderBlockEntity && state.getBlock() instanceof GrinderBlock) {
            GrinderBlockEntity grinder = (GrinderBlockEntity) entity;
            grinder.openInventory((ServerPlayerEntity) player);
        }
        return ActionResult.SUCCESS;

    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof GrinderBlockEntity) {
                ItemScatterer.spawn(world, pos, (GrinderBlockEntity) blockEntity);
                world.updateComparators(pos,this);

                for (GrinderBlockEntity.GrinderGui gui : ((GrinderBlockEntity) blockEntity).openGuis) {
                    gui.close(false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GrinderBlockEntity(pos, state);
    }
}
