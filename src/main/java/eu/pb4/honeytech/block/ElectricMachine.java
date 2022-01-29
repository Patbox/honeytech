package eu.pb4.honeytech.block;


import eu.pb4.honeytech.other.HTTier;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

public interface ElectricMachine {
    ElectricMachine EMPTY = new ElectricMachine() {
        @Override
        public long getPerTickEnergyUsage() {
            return 0;
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
            return 0;
        }

        @Override
        public long getCapacity() {
            return 0;
        }

        @Override
        public HTTier getTier() {
            return HTTier.BASIC;
        }
    };

    static ElectricMachine of(BlockState state) {
        return state.getBlock() instanceof ElectricMachine machineBlock ? machineBlock : EMPTY;
    }

    @Nullable
    static ElectricMachine ofNullable(BlockState state) {
        return state.getBlock() instanceof ElectricMachine machineBlock ? machineBlock : null;
    }

    long getPerTickEnergyUsage();
    long getPerTickEnergyProduction();
    long getMaxEnergyOutput();
    long getMaxEnergyInput();
    long getCapacity();
    HTTier getTier();
}
