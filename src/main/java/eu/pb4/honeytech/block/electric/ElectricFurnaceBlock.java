package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.BlockWithItemTooltip;
import eu.pb4.honeytech.blockentity.electric.ElectricFurnaceBlockEntity;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ElectricFurnaceBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, BlockWithItemTooltip {
    public final float speedMulti;

    public ElectricFurnaceBlock(Settings settings, float speedMulti) {
        super(settings);
        this.speedMulti = speedMulti;
        //this.setDefaultState(this.stateManager.getDefaultState().with(Properties.FACING, Direction.NORTH).with(Properties.LIT, false));
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
    public String getVirtualHeadSkin(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODI5NmYwOTI1MjRhZTljMmEyZTg3ODgxY2I0MTVhZGIzNThkNmNiNzczYzg1ZGM5NzIwMmZlZmI3NTRjMSJ9fX0=";
    }

    @Override
    public Collection<Text> getTooltip() {
        List<Text> list = new ArrayList<>();
        list.add(HTUtils.styledTooltip("capacity", new LiteralText(HTUtils.formatEnergy(2048)).formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_in",
                new LiteralText(HTUtils.formatEnergy(32))
                        .formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_consumption",
                new LiteralText(HTUtils.formatEnergy(30))
                        .formatted(Formatting.GRAY)));
        return list;
    }
}
