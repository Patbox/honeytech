package eu.pb4.honeytech.blockentity.storage;

import eu.pb4.honeytech.block.storage.PipeBlock;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class PipeBlockEntity extends BlockEntity implements ImplementedInventory, VirtualObject {
    DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int tickA = 0;
    private int tickB = 0;

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.PIPE, pos, state);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, this.items);

        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        try {
            Inventories.readNbt(tag, this.items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof PipeBlockEntity self)) {
            return;
        }

        self.tickA++;
        self.tickB++;
        if (self.tickA == 5) {
            self.tickA = 0;
            if (!self.isEmpty()) {
                if (!self.insert()) {
                ((ServerWorld) self.world).spawnParticles(new DustParticleEffect(new Vec3f(1, 0, 0), 5f),
                        self.pos.getX() + 0.5d,
                        self.pos.getY() + 0.5d,
                        self.pos.getZ() + 0.5d, 1,0.01f, 0.01f, 0.01f, 0.0f);
                }
            }
        }

        if (self.tickB == 20) {
            self.tickB = 0;

            Direction dir = self.getCachedState().get(PipeBlock.OUTPUT_DIR);
            ((ServerWorld) self.world).spawnParticles(new DustParticleEffect(new Vec3f(1, 1, 1), 2.5f),
                    self.pos.getX() + 0.5d + ((double) dir.getOffsetX()) / 2.5,
                    self.pos.getY() + 0.5d + ((double) dir.getOffsetY()) / 2.5,
                    self.pos.getZ() + 0.5d + ((double) dir.getOffsetZ()) / 2.5, 1,0.01f, 0.01f, 0.01f, 0.2f);
        }
    }

    @Nullable
    private Inventory getOutputInventory() {
        Direction direction = this.getCachedState().get(PipeBlock.OUTPUT_DIR);
        return HopperBlockEntity.getInventoryAt(this.getWorld(), this.pos.offset(direction));
    }

    private boolean insert() {
        Inventory inventory = this.getOutputInventory();
        if (inventory == null) {
            return false;
        } else {
            Direction direction = this.getCachedState().get(PipeBlock.OUTPUT_DIR).getOpposite();
            if (!this.isInventoryFull(inventory, direction)) {
                for (int i = 0; i < this.size(); ++i) {
                    if (!this.getStack(i).isEmpty()) {
                        ItemStack itemStack = this.getStack(i).copy();
                        ItemStack itemStack2 = HopperBlockEntity.transfer(this, inventory, this.removeStack(i, 1), direction);
                        if (itemStack2.isEmpty()) {
                            inventory.markDirty();
                            return true;
                        }

                        this.setStack(i, itemStack);
                    }
                }

            }
            return false;
        }
    }


    private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
    }

    private boolean isInventoryFull(Inventory inv, Direction direction) {
        return getAvailableSlots(inv, direction).allMatch((i) -> {
            ItemStack itemStack = inv.getStack(i);
            return itemStack.getCount() >= itemStack.getMaxCount();
        });
    }
}
