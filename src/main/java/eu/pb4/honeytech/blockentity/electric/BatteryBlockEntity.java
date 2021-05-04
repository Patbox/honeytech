package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.electric.BatteryBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

public class BatteryBlockEntity extends BlockEntity implements Tickable, EnergyHolder, VirtualObject {
    private double energy = 0;

    public BatteryBlockEntity() {
        super(HTBlockEntities.BATTERY);
    }

    @Override
    public void tick() {
        for (Direction dir : Direction.values()) {
            BlockEntity entity = this.world.getBlockEntity(pos.offset(dir));
            if (entity instanceof EnergyHolder) {
                EnergyHolder holder = (EnergyHolder) entity;
                double proc = holder.getEnergyAmount() / holder.getMaxEnergyCapacity();

                if (holder.isEnergySource() && !this.isFullEnergy() && proc > 0.65) {
                    double newAmount = Math.max(0,
                            Math.min(this.getMaxEnergyTransferCapacity(dir.getOpposite(), false),
                                    this.getMaxEnergyCapacity() - this.getEnergyAmount()));
                    double tr = holder.transferEnergy(-newAmount, dir.getOpposite());
                    this.setEnergyAmount(this.getEnergyAmount() - tr);
                } else if (!holder.isFullEnergy() && holder.isEnergyConsumer() && proc < 0.60) {
                    double amount = Math.min(this.getMaxEnergyTransferCapacity(dir.getOpposite(), true), this.getEnergyAmount());
                    if (amount >= 0.001) {
                        double tr = holder.transferEnergy(amount, dir.getOpposite());
                        this.setEnergyAmount(this.getEnergyAmount() - tr);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("Energy", energy);
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

        int lvl = this.getCachedState().get(BatteryBlock.LEVEL);
        double proc = this.energy / this.getMaxEnergyCapacity();
        int newLvl;

        if (proc > 0.95) {
            newLvl = 4;
        } else if (proc > 0.70) {
            newLvl = 3;
        } else if (proc > 0.40) {
            newLvl = 2;
        } else if (proc > 0.10) {
            newLvl = 1;
        } else {
            newLvl = 0;
        }

        if (newLvl != lvl) {
            this.world.setBlockState(this.pos, this.getCachedState().with(BatteryBlock.LEVEL, newLvl));
        }
    }

    @Override
    public boolean isEnergySource() {
        return true;
    }

    @Override
    public boolean isEnergyConsumer() {
        return true;
    }

    @Override
    public double getMaxEnergyCapacity() {
        return ((BatteryBlock) this.getCachedState().getBlock()).capacity;
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return getMaxTransfer(isDraining);
    }

    public static double getMaxTransfer(boolean isDraining) {
        return isDraining ? 256 : 64;
    }
}