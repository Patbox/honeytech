package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.machines_common.OreWasherBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class ElectricOreWasherBlockEntity extends OreWasherBlockEntity implements EnergyHolder, Tickable {
    private double energy = 0;
    private int ticker;

    public ElectricOreWasherBlockEntity() {
        super(HTBlockEntities.ELECTRIC_ORE_WASHER);
    }

    @Override
    public void tick() {
        if (this.world.isClient) {
            return;
        }

        this.ticker++;
        EnergyHolder.takeEnergyFromSources(this, this.world, this.pos, ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyInput());

        int ticksRequired = (int) (10 / ((MachineBlock) this.getCachedState().getBlock()).getTier().speed);

        if (this.ticker >= ticksRequired) {
            this.ticker = 0;
            double use = ((MachineBlock) this.getCachedState().getBlock()).getPerTickEnergyUsage() * ticksRequired;
            if (this.energy >= use) {
                ActionResult result = this.useHandle(this.getCachedState(), this.getWorld(), this.getPos(), null, null, null);
                if (result == ActionResult.SUCCESS) {
                    this.energy -= use;
                }
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("Energy", this.energy);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.energy = tag.getDouble("Energy");

    }

    @Override
    public double getEnergyAmount() {
        return this.energy;
    }

    @Override
    public void setEnergyAmount(double amount) {
        this.energy = amount;
    }

    @Override
    public boolean isEnergySource() {
        return false;
    }

    @Override
    public boolean isEnergyConsumer() {
        return true;
    }

    @Override
    public double getMaxEnergyCapacity() {
        return ((MachineBlock) this.getCachedState().getBlock()).getCapacity();
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return isDraining ? 0 : ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyInput();
    }
}
