package eu.pb4.honeytech.block.electric;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.BlockWithItemTooltip;
import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.blockentity.electric.CoalGeneratorBlockEntity;
import eu.pb4.honeytech.other.HTUtils;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoalGeneratorBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, WrenchableBlock, BlockWithItemTooltip {
    public final int multiplier;
    public CoalGeneratorBlock(Settings settings, int multip) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(Properties.LIT, false));
        this.multiplier = multip;
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
    public Collection<Text> getTooltip() {
        List<Text> list = new ArrayList<>();
        list.add(HTUtils.styledTooltip("capacity", new LiteralText(HTUtils.formatEnergy(2048)).formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_out",
                new LiteralText(HTUtils.formatEnergy(256))
                        .formatted(Formatting.GRAY)));
        return list;
    }
}
