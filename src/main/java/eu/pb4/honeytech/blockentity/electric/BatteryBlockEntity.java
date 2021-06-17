package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.block.electric.BatteryBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BatteryBlockEntity extends BlockEntity implements EnergyHolder, VirtualObject {
    private double energy = 0;

    public BatteryBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.BATTERY, pos, state);
    }


    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof BatteryBlockEntity battery)) {
            return;
        }

        for (Direction dir : Direction.values()) {
            BlockEntity entity = world.getBlockEntity(pos.offset(dir));
            if (entity instanceof EnergyHolder) {
                EnergyHolder holder = (EnergyHolder) entity;
                double proc = holder.getEnergyAmount() / holder.getMaxEnergyCapacity();

                if (holder.isEnergySource() && !battery.isFullEnergy() && proc > 0.65) {
                    double newAmount = Math.max(0,
                            Math.min(battery.getMaxEnergyTransferCapacity(dir.getOpposite(), false),
                                    battery.getMaxEnergyCapacity() - battery.getEnergyAmount()));
                    double tr = holder.transferEnergy(-newAmount, dir.getOpposite());
                    battery.setEnergyAmount(battery.getEnergyAmount() - tr);
                } else if (!holder.isFullEnergy() && holder.isEnergyConsumer() && proc < 0.60) {
                    double amount = Math.min(battery.getMaxEnergyTransferCapacity(dir.getOpposite(), true), battery.getEnergyAmount());
                    if (amount >= 0.001) {
                        double tr = holder.transferEnergy(amount, dir.getOpposite());
                        battery.setEnergyAmount(battery.getEnergyAmount() - tr);
                    }
                }
            }
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putDouble("Energy", energy);
        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
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
        return ((MachineBlock) this.getCachedState().getBlock()).getCapacity();
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return isDraining ? ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyOutput() : ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyInput();
    }
}