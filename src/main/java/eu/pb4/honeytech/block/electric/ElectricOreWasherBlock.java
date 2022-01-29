package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.block.machines_common.OreWasherBlock;
import eu.pb4.honeytech.blockentity.electric.ElectricOreWasherBlockEntity;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.polymer.api.block.PolymerHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ElectricOreWasherBlock extends OreWasherBlock implements PolymerHeadBlock, ElectricMachine {
    public final HTTier tier;
    public ElectricOreWasherBlock(Settings settings, HTTier tier) {
        super(settings);
        this.tier = tier;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricOreWasherBlockEntity(pos, state);
    }

    @Override
    public String getPolymerSkinValue(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY1ZDc0NjY4YmRiNTRkMzRjNDgyNThkYjU2YTIxOGFjZGIzYjBmODMwOTZkYTY5MjIzZTZhMGMzNmM1ODZkIn19fQ==";
    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.PLAYER_HEAD;
    }

    @Override
    public long getPerTickEnergyUsage() {
        return 64;
    }

    @Override
    public long getPerTickEnergyProduction() {
        return 0;
    }

    @Override
    public long getMaxEnergyOutput() {
        return 0;
    }

    @Override
    public long getMaxEnergyInput() {
        return tier.energyCapacity / 16;
    }

    @Override
    public long getCapacity() {
        return tier.energyCapacity;
    }

    @Override
    public HTTier getTier() {
        return this.tier;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ElectricOreWasherBlockEntity::tick;
    }
}
