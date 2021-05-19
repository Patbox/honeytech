package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.block.machines_common.OreWasherBlock;
import eu.pb4.honeytech.blockentity.electric.ElectricOreWasherBlockEntity;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class ElectricOreWasherBlock extends OreWasherBlock implements VirtualHeadBlock, MachineBlock {
    public final HTTier tier;
    public ElectricOreWasherBlock(Settings settings, HTTier tier) {
        super(settings);
        this.tier = tier;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ElectricOreWasherBlockEntity();
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWY1ZDc0NjY4YmRiNTRkMzRjNDgyNThkYjU2YTIxOGFjZGIzYjBmODMwOTZkYTY5MjIzZTZhMGMzNmM1ODZkIn19fQ==";
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.PLAYER_HEAD;
    }

    @Override
    public double getPerTickEnergyUsage() {
        return 64;
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
        return tier.energyCapacity / 16;
    }

    @Override
    public double getCapacity() {
        return tier.energyCapacity;
    }

    @Override
    public HTTier getTier() {
        return this.tier;
    }
}
