package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.machines_common.OreWasherBlockEntity;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class ElectricOreWasherBlockEntity extends OreWasherBlockEntity implements EnergyHolder {
    public final SimpleEnergyStorage energyStorage;
    private int ticker;

    public ElectricOreWasherBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.ELECTRIC_ORE_WASHER, pos, state);
        this.energyStorage = HTUtils.createEnergyStorage(this, ElectricMachine.of(state));

    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof ElectricOreWasherBlockEntity self)) {
            return;
        }

        if (self.world.isClient) {
            return;
        }

        self.ticker++;
        //EnergyHolder.takeEnergyFromSources(self, self.world, self.pos, ((ElectricMachine) self.getCachedState().getBlock()).getMaxEnergyInput());

        int ticksRequired = (int) (10 / ((ElectricMachine) self.getCachedState().getBlock()).getTier().speed);

        if (self.ticker >= ticksRequired) {
            self.ticker = 0;
            double use = ((ElectricMachine) self.getCachedState().getBlock()).getPerTickEnergyUsage() * ticksRequired;
            if (self.energyStorage.amount >= use) {
                ActionResult result = self.useHandle(self.getCachedState(), self.getWorld(), self.getPos(), null, null, null);
                if (result == ActionResult.SUCCESS) {
                    self.energyStorage.amount -= use;
                }
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putLong("Energy", this.energyStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.energyStorage.amount = tag.getLong("Energy");

    }

    @Override
    public EnergyStorage getEnergy() {
        return this.energyStorage;
    }
}
