package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.item.HTItems;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class AutoCrafterBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, SidedInventory, Tickable, EnergyHolder, VirtualObject {
    private static final int[] SLOTS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

    public final Set<Gui> openGuis = new HashSet<>();
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(28, ItemStack.EMPTY);
    private double energy = 0;
    private ItemStack invalidItemStack = null;

    private final CraftingInventory craftingInventory = new CraftingInventory(null, 3, 3);

    private CraftingRecipe recipe = null;

    public AutoCrafterBlockEntity() {
        super(HTBlockEntities.AUTO_CRAFTER);
    }

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            Gui gui = new Gui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.energy = tag.getDouble("Energy");
        Inventories.fromTag(tag, items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag out = super.toTag(tag);
        out.putDouble("Energy", this.energy);
        Inventories.toTag(tag, items);
        return tag;
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
        return false;
    }

    @Override
    public boolean isEnergyConsumer() {
        return true;
    }

    @Override
    public double getMaxEnergyCapacity() {
        return ((MachineBlock) this.getCachedState().getBlock()).getCapacity();
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return isDraining ? 0 : ((MachineBlock) this.getCachedState().getBlock()).getMaxEnergyInput();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText(this.getCachedState().getBlock().getTranslationKey());
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot != 0 && slot < 19;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot != 0 && slot > 18;
    }

    @Override
    public void tick() {
        if (this.world.isClient || this.items.get(0).isEmpty() || this.invalidItemStack == this.items.get(0)) {
            return;
        }

        if (this.recipe == null || this.recipe.getOutput().isItemEqualIgnoreDamage(this.items.get(0))) {
            this.recipe = findRecipe();
            if (this.recipe == null) {
                this.invalidItemStack = this.items.get(0);
            }
        }

        if (this.re)
    }

    private CraftingRecipe findRecipe() {
        ItemStack target = this.items.get(0);

        for (CraftingRecipe recipe : this.world.getRecipeManager().listAllOfType(RecipeType.CRAFTING)) {
            if (recipe.getOutput().isItemEqualIgnoreDamage(target)) {
                return recipe;
            }
        }
    }

    public static class Gui extends SimpleGui {
        private static final Style BATTERY_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GRAY);
        private final AutoCrafterBlockEntity blockEntity;
        private final double energyLast = -1;

        public Gui(AutoCrafterBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X6, player, false);
            this.setTitle(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey()));
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
            Slot slot = this.getSlotRedirect(index);

            if (slot instanceof OutputSlot) {
                this.player.networkHandler.sendPacket(new InventoryS2CPacket(this.syncId, this.screenHandler.getStacks()));
                this.player.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, -1, this.player.inventory.getCursorStack()));
            }

            return super.onAnyClick(index, type, action);
        }

        @Override
        public void onTick() {
            if (this.blockEntity instanceof EnergyHolder && !MathHelper.approximatelyEquals(((EnergyHolder) this.blockEntity).getEnergyAmount(), this.energyLast)) {
                this.getSlot(33).getItemStack().setCustomName(HTUtils.getText("gui", "battery_charge",
                        new LiteralText(HTUtils.formatEnergy(((EnergyHolder) this.blockEntity).getEnergyAmount())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.formatEnergy(((EnergyHolder) this.blockEntity).getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.dtt(((EnergyHolder) this.blockEntity).getEnergyAmount() / ((EnergyHolder) this.blockEntity).getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                ).setStyle(BATTERY_STYLE));
            }

            super.onTick();
        }

        @Override
        public void onUpdate(boolean firstUpdate) {
            if (firstUpdate) {
                GuiElementBuilder builder = new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText(""));

                for (int x = 0; x < 27; x++) {
                    this.setSlot(x + 18, builder);
                }

                this.setSlotRedirect(29, new Slot(blockEntity, 0, 0, 0));

                this.setSlot(33, new GuiElementBuilder(HTItems.BATTERY).setName(HTUtils.getText("gui", "battery_charge",
                        new LiteralText(HTUtils.formatEnergy(((EnergyHolder) this.blockEntity).getEnergyAmount())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.formatEnergy(((EnergyHolder) this.blockEntity).getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.dtt(((EnergyHolder) this.blockEntity).getEnergyAmount() / ((EnergyHolder) this.blockEntity).getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                ).setStyle(BATTERY_STYLE)));

                for (int x = 0; x < 18; x++) {
                    this.setSlotRedirect(x, new Slot(blockEntity, x + 1, 0, 0));
                }

                for (int x = 0; x < 9; x++) {
                    this.setSlotRedirect(x + 45, new OutputSlot(blockEntity, x + 19, 0, 0));
                }
            }
            super.onUpdate(firstUpdate);
        }

        @Override
        public void onClose() {
            this.blockEntity.openGuis.remove(this);
            super.onClose();
        }
    }
}
