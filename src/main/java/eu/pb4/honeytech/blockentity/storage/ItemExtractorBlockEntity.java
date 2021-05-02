package eu.pb4.honeytech.blockentity.storage;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.block.storage.PipeBlock;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class ItemExtractorBlockEntity extends BlockEntity implements Tickable, VirtualObject {
    private int tickA = 0;
    private int tickB = 0;

    public ItemExtractorBlockEntity() {
        super(HTBlockEntities.EXTRACTOR);
    }


    @Override
    public void tick() {
        this.tickA++;
        this.tickB++;
        if (this.tickA == 5) {
            this.tickA = 0;
            checkAndTransfer();
        }

        if (this.tickB == 10) {
            this.tickB = 0;

            Direction dir = this.getCachedState().get(PipeBlock.OUTPUT_DIR);
            ((ServerWorld) world).spawnParticles(new DustParticleEffect(1, 1, 1, 3f),
                    this.pos.getX() + 0.5d + ((double) dir.getOffsetX()) / 3,
                    this.pos.getY() + 0.5d + ((double) dir.getOffsetY()) / 3,
                    this.pos.getZ() + 0.5d + ((double) dir.getOffsetZ()) / 3, 1,0.01f, 0.01f, 0.01f, 0.0f);
        }
    }

    @Nullable
    private Inventory getOutputInventory() {
        Direction direction = this.getCachedState().get(PipeBlock.OUTPUT_DIR);
        return HopperBlockEntity.getInventoryAt(this.getWorld(), this.pos.offset(direction));
    }

    @Nullable
    private Inventory getInputInventory() {
        Direction direction = this.getCachedState().get(PipeBlock.INPUT_DIR);
        return HopperBlockEntity.getInventoryAt(this.getWorld(), this.pos.offset(direction));
    }

    private boolean checkAndTransfer() {
        Inventory outputInventory = this.getOutputInventory();
        Inventory inputInventory = this.getInputInventory();

        if (outputInventory != null && inputInventory != null) {
            Direction dirOut = this.getCachedState().get(PipeBlock.OUTPUT_DIR).getOpposite();
            Direction dirInp = this.getCachedState().get(PipeBlock.INPUT_DIR).getOpposite();

            if (!this.isInventoryFull(outputInventory, dirOut)) {
                for (int i = 0; i < inputInventory.size(); ++i) {
                    ItemStack is = inputInventory.getStack(i);
                    if (!is.isEmpty() && canExtract(inputInventory, is, i, dirInp) && canInsert(outputInventory, is, i, dirOut)) {
                        ItemStack itemStack = inputInventory.getStack(i).copy();
                        ItemStack itemStack2 = HopperBlockEntity.transfer(inputInventory, outputInventory, inputInventory.removeStack(i, 1), dirOut);
                        if (itemStack2.isEmpty()) {
                            outputInventory.markDirty();
                            return true;
                        }

                        inputInventory.setStack(i, itemStack);
                    }
                }

            }
        }
        return false;
    }

    private static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
    }

    private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        if (!inventory.isValid(slot, stack)) {
            return false;
        } else {
            return !(inventory instanceof SidedInventory) || ((SidedInventory)inventory).canInsert(slot, stack, side);
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
