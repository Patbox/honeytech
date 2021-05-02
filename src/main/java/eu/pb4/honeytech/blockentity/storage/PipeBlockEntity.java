package eu.pb4.honeytech.blockentity.storage;

import eu.pb4.honeytech.block.HTBlocks;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class PipeBlockEntity extends BlockEntity implements ImplementedInventory, Tickable, VirtualObject {
    DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int tickA = 0;
    private int tickB = 0;

    public PipeBlockEntity() {
        super(HTBlockEntities.PIPE);
    }


    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, this.items);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        try {
            Inventories.fromTag(tag, this.items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        this.tickA++;
        this.tickB++;
        if (this.tickA == 5) {
            this.tickA = 0;
            if (!this.isEmpty()) {
                if (!insert()) {
                ((ServerWorld) this.world).spawnParticles(new DustParticleEffect(1, 0, 0, 5f),
                        this.pos.getX() + 0.5d,
                        this.pos.getY() + 0.5d,
                        this.pos.getZ() + 0.5d, 1,0.01f, 0.01f, 0.01f, 0.0f);
                }
            }
        }

        if (this.tickB == 20) {
            this.tickB = 0;

            Direction dir = this.getCachedState().get(PipeBlock.OUTPUT_DIR);
            ((ServerWorld) this.world).spawnParticles(new DustParticleEffect(1, 1, 1, 2.5f),
                    this.pos.getX() + 0.5d + ((double) dir.getOffsetX()) / 2.5,
                    this.pos.getY() + 0.5d + ((double) dir.getOffsetY()) / 2.5,
                    this.pos.getZ() + 0.5d + ((double) dir.getOffsetZ()) / 2.5, 1,0.01f, 0.01f, 0.01f, 0.2f);
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
