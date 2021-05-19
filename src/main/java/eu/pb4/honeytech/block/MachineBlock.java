package eu.pb4.honeytech.block;


import eu.pb4.honeytech.other.HTTier;

public interface MachineBlock {
    double getPerTickEnergyUsage();
    double getPerTickEnergyProduction();
    double getMaxEnergyOutput();
    double getMaxEnergyInput();
    double getCapacity();
    HTTier getTier();
}
