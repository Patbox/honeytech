package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.blockentity.HTBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CableBlockEntity extends BlockEntity {
    //public final SimpleEnergyStorage energyStorage;

    private final int ticker = 0;
    public static final long TRANSFER_RATE = 512;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.CABLE, pos, state);
    }

    /*public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof CableBlockEntity self)) {
            return;
        }

        if (self.world.isClient) {
            return;
        }

        ArrayList<CableBlockEntity> cables = new ArrayList<>();

        if (self.energy > 0) {
            for (Direction dir : Direction.values()) {
                BlockEntity entity = self.world.getBlockEntity(self.pos.offset(dir));

                if (entity instanceof CableBlockEntity) {
                    CableBlockEntity cable = (CableBlockEntity) entity;
                    cables.add(cable);
                }
            }
        }

        if (!cables.isEmpty()) {
            cables.add(self);

            double power = 0;

            for (CableBlockEntity cable : cables) {
                power += cable.getEnergyAmount();
            }
            double perCable = power / cables.size();

            for (CableBlockEntity cable : cables) {
                cable.setEnergyAmount(perCable);
            }
        }

        self.ticker += 1;

        if (self.ticker == 20) {
            self.ticker = 0;
            ((ServerWorld) self.world).spawnParticles(new DustParticleEffect(new Vec3f((float) (self.energy / self.getMaxEnergyCapacity()), 0, 0), 2.5f),
                    self.pos.getX() + 0.5d,
                    self.pos.getY() + 0.5d,
                    self.pos.getZ() + 0.5d, 1,0.01f, 0.01f, 0.01f, 0.2f);
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putDouble("Energy", energy);
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
        return TRANSFER_RATE;
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return 256;
    }*/
}
