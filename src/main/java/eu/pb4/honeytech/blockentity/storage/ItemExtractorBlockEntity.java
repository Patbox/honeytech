package eu.pb4.honeytech.blockentity.storage;

import eu.pb4.honeytech.block.storage.PipeBlock;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class ItemExtractorBlockEntity extends BlockEntity implements VirtualObject {
    private int tickA = 0;
    private int tickB = 0;

    public ItemExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.EXTRACTOR, pos, state);
    }


    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof ItemExtractorBlockEntity self)) {
            return;
        }

        self.tickA++;
        self.tickB++;
        if (self.tickA == 5) {
            self.tickA = 0;
            self.checkAndTransfer();
        }

        if (self.tickB == 10) {
            self.tickB = 0;

            Direction dir = self.getCachedState().get(PipeBlock.OUTPUT_DIR);
            ((ServerWorld) self.world).spawnParticles(new DustParticleEffect(new Vec3f(1, 1, 1), 3f),
                    self.pos.getX() + 0.5d + ((double) dir.getOffsetX()) / 3,
                    self.pos.getY() + 0.5d + ((double) dir.getOffsetY()) / 3,
                    self.pos.getZ() + 0.5d + ((double) dir.getOffsetZ()) / 3, 1,0.01f, 0.01f, 0.01f, 0.0f);
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
