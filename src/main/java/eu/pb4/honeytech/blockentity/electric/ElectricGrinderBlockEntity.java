package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.machines_common.GrinderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class ElectricGrinderBlockEntity extends GrinderBlockEntity implements EnergyHolder, Tickable {
    public double energy;
    public int ticker;

    public ElectricGrinderBlockEntity() {
        super(HTBlockEntities.ELECTRIC_GRINDER);
    }


    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.energy = tag.getDouble("Energy");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("Energy", this.energy);
        return tag;
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
        return 2048;
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return 256;
    }

    @Override
    public void tick() {
        this.ticker++;
        EnergyHolder.takeEnergyFromSources(this, this.world, this.pos, 256);

        if (this.ticker >= 10) {
            this.ticker = 0;
            if (this.energy >= 128) {
                ActionResult result = this.useHandle(this.getCachedState(), this.getWorld(), this.getPos(), null, null, null);
                if (result == ActionResult.SUCCESS) {
                    this.energy -= 128;
                }
            }
        }
    }
}
