package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.blockentity.electric.AutoCrafterBlockEntity;
import eu.pb4.honeytech.blockentity.electric.CoalGeneratorBlockEntity;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AutoCrafterBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, MachineBlock {
    private final HTTier tier;

    public AutoCrafterBlock(Settings settings) {
        super(settings);
        this.tier = HTTier.IRON;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof AutoCrafterBlockEntity) {
            ((AutoCrafterBlockEntity) entity).openInventory((ServerPlayerEntity) player);
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

                for (AutoCrafterBlockEntity.Gui gui : ((AutoCrafterBlockEntity) blockEntity).openGuis) {
                    gui.close(false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public double getPerTickEnergyUsage() {
        return 32;
    }

    @Override
    public double getPerTickEnergyProduction() {
        return 0;
    }

    @Override
    public double getMaxEnergyOutput() {
        return 32;
    }

    @Override
    public double getMaxEnergyInput() {
        return this.tier.energyCapacity / 16;
    }

    @Override
    public double getCapacity() {
        return this.tier.energyCapacity;
    }

    @Override
    public HTTier getTier() {
        return this.tier;
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return HTUtils.INVALID_TEXTURE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new AutoCrafterBlockEntity();
    }
}
