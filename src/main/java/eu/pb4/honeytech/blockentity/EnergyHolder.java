package eu.pb4.honeytech.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public interface EnergyHolder {
    // Gets amount of stored energy
    double getEnergyAmount();

    // Sets amount of stored energy
    void setEnergyAmount(double amount);

    // Checks if it's energy source (battery/generator)
    boolean isEnergySource();

    // Checks if it can use energy
    boolean isEnergyConsumer();

    // Maximum amount of energy that can be holded
    double getMaxEnergyCapacity();

    // Checks if it's full
    default boolean isFullEnergy() {
        return this.getMaxEnergyCapacity() <= this.getEnergyAmount() || MathHelper.approximatelyEquals(this.getMaxEnergyCapacity(), this.getEnergyAmount()); // Good enough
    }

    // Transfers amount of energy, returns amount of actually transferred (can be negative)
    default double transferEnergy(double amount, Direction direction) {
        boolean isDraining = amount < 0;

        amount = Math.min(Math.abs(amount), this.getMaxEnergyTransferCapacity(direction, isDraining));
        double en = this.getEnergyAmount();

        if (!isDraining && this.isEnergyConsumer() && this.canConsumeEnergyFrom(direction)) {
            double newEnergy = Math.min(en + amount, this.getMaxEnergyCapacity());
            this.setEnergyAmount(newEnergy);
            return newEnergy - en;
        } else if (isDraining && this.isEnergySource() && this.canProvideEnergyFrom(direction)) {
            double newEnergy = Math.max(en - amount, 0);
            this.setEnergyAmount(newEnergy);
            return newEnergy - en;
        }

        return 0;
    }

    default boolean canConsumeEnergyFrom(Direction direction) {
        return this.isEnergyConsumer();
    }

    default boolean canProvideEnergyFrom(Direction direction) {
        return this.isEnergySource();
    }

    double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining);

    static int provideEnergyToConsumers(EnergyHolder source, World world, BlockPos pos, double value) {
        int conn = 0;
        if (source.isEnergySource()) {
            for (Direction dir : Direction.values()) {
                BlockEntity entity = world.getBlockEntity(pos.offset(dir));
                if (entity instanceof EnergyHolder) {
                    EnergyHolder holder = (EnergyHolder) entity;
                    if (!holder.isFullEnergy() && holder.isEnergyConsumer()) {
                        Double amount = Math.min(value, source.getEnergyAmount());
                        if (amount >= 0.01) {
                            Double tr = holder.transferEnergy(amount, dir.getOpposite());
                            source.setEnergyAmount(source.getEnergyAmount() - tr);
                        }
                        conn++;
                    }
                }
            }
        }
        return conn;
    }

    static int takeEnergyFromSources(EnergyHolder consumer, World world, BlockPos pos, double amount) {
        int conn = 0;
        if (consumer.isEnergyConsumer()) {
            for (Direction dir : Direction.values()) {
                BlockEntity entity = world.getBlockEntity(pos.offset(dir));
                if (entity instanceof EnergyHolder) {
                    EnergyHolder holder = (EnergyHolder) entity;
                    if (holder.isEnergySource() && !consumer.isFullEnergy()) {
                        double newAmount = Math.max(0, Math.min(amount, consumer.getMaxEnergyCapacity() - consumer.getEnergyAmount()));
                        Double tr = holder.transferEnergy(-newAmount, dir.getOpposite());
                        consumer.setEnergyAmount(consumer.getEnergyAmount() - tr);
                        conn++;
                    }
                }
            }
        }
        return conn;
    }
}
