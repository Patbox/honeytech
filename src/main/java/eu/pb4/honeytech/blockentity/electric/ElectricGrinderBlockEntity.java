package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.machines_common.GrinderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ElectricGrinderBlockEntity extends GrinderBlockEntity implements EnergyHolder {
    public double energy;
    public int ticker;

    public ElectricGrinderBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.ELECTRIC_GRINDER, pos, state);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.energy = tag.getDouble("Energy");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
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
        return ((MachineBlock) this.getCachedState().getBlock()).getCapacity();
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return isDraining ? 0 : ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyInput();
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof ElectricGrinderBlockEntity self)) {
            return;
        }

        if (self.world.isClient) {
            return;
        }

        self.ticker++;
        EnergyHolder.takeEnergyFromSources(self, self.world, self.pos, ((MachineBlock) self.getCachedState().getBlock()).getMaxEnergyInput());

        int ticksRequired = (int) (10 / ((MachineBlock) self.getCachedState().getBlock()).getTier().speed);

        if (self.ticker >= ticksRequired) {
            self.ticker = 0;
            double use = ((MachineBlock) self.getCachedState().getBlock()).getPerTickEnergyUsage() * ticksRequired;
            if (self.energy >= use) {
                ActionResult result = self.useHandle(self.getCachedState(), self.getWorld(), self.getPos(), null, null, null);
                if (result == ActionResult.SUCCESS) {
                    self.energy -= use;
                }
            }
        }
    }
}
