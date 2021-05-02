package eu.pb4.honeytech.blockentity.basic_machines;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashSet;
import java.util.Set;

public class TrashCanBlockEntity extends LockableContainerBlockEntity implements Inventory, VirtualObject {
    public final Set<TrashCanGui> openGuis = new HashSet<>();

    public TrashCanBlockEntity() {
        super(HTBlockEntities.TRASHCAN);
    }

    public DefaultedList<ItemStack> getItems() {
        return DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        return tag;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
    }

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            TrashCanGui gui = new TrashCanGui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {

    }

    public static class TrashCanGui extends SimpleGui {
        private final TrashCanBlockEntity blockEntity;
        private final ImplementedInventory inventory = ImplementedInventory.ofSize(18);

        public TrashCanGui(TrashCanBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X3, player, false);
            this.setTitle(HTUtils.getText("gui", "trashcan"));
            this.blockEntity = blockEntity;
        }

        @Override
        public void onUpdate(boolean firstUpdate) {
            for (int x = 0; x < 18; x++) {
                this.setSlotRedirect(x, new Slot(this.inventory, x, 0, 0));
            }
            super.onUpdate(firstUpdate);
        }

        @Override
        public void onClose() {
            if (this.blockEntity != null) {
                this.blockEntity.openGuis.remove(this);
            }
            super.onClose();
        }
    }
}
