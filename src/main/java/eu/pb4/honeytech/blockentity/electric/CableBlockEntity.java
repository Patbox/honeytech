package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.electric.CableBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;

public class CableBlockEntity extends BlockEntity implements Tickable, EnergyHolder, VirtualObject {
    private double energy = 0;
    private int ticker = 0;
    public static final double MAX_ENERGY_CAPACITY = 512;

    public CableBlockEntity() {
        super(HTBlockEntities.CABLE);
    }

    @Override
    public void tick() {
        if (this.world.isClient) {
            return;
        }

        ArrayList<CableBlockEntity> cables = new ArrayList<>();

        if (this.energy > 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity entity = this.world.getBlockEntity(this.pos.offset(dir));

                if (entity instanceof CableBlockEntity) {
                    CableBlockEntity cable = (CableBlockEntity) entity;
                    cables.add(cable);
                }
            }
        }

        if (!cables.isEmpty()) {
            cables.add(this);

            double power = 0;

            for (CableBlockEntity cable : cables) {
                power += cable.getEnergyAmount();
            }
            double perCable = power / cables.size();

            for (CableBlockEntity cable : cables) {
                cable.setEnergyAmount(perCable);
            }
        }

        this.ticker += 1;

        if (this.ticker == 20) {
            this.ticker = 0;
            ((ServerWorld) this.world).spawnParticles(new DustParticleEffect((float) (this.energy / this.getMaxEnergyCapacity()), 0, 0, 2.5f),
                    this.pos.getX() + 0.5d,
                    this.pos.getY() + 0.5d,
                    this.pos.getZ() + 0.5d, 1,0.01f, 0.01f, 0.01f, 0.2f);
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
        return MAX_ENERGY_CAPACITY;
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return 256;
    }
}
