package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.electric.ElectricFurnaceBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.item.HTItems;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ElectricFurnaceBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable, EnergyHolder, VirtualObject {
    public final Set<Gui> openGuis = new HashSet<>();

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private SmeltingRecipe lastRecipe = null;
    private double energy = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 9999;
    private SmeltingRecipe currentRecipe = null;
    private Identifier recipeId = null;

    public ElectricFurnaceBlockEntity() {
        super(HTBlockEntities.ELECTRIC_FURNACE);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{side != Direction.DOWN ? 0 : 1};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }

    @Override
    public void provideRecipeInputs(RecipeFinder finder) {
        finder.addItem(this.items.get(0));
    }

    @Nullable
    @Override
    public Recipe<?> getLastRecipe() {
        return this.lastRecipe;
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe instanceof SmeltingRecipe) {
            this.lastRecipe = (SmeltingRecipe) recipe;
        }
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.energy = tag.getDouble("Energy");
        this.cookTime = tag.getInt("CookTime");
        this.cookTimeTotal = tag.getInt("CookTimeTotal");
        this.recipeId = Identifier.tryParse(tag.getString("Recipe"));
        Inventories.fromTag(tag, this.items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("Energy", this.energy);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.cookTimeTotal);
        if (this.currentRecipe != null) {
            tag.putString("Recipe", this.currentRecipe.getId().toString());
        }
        Inventories.toTag(tag, this.items);
        return tag;
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            EnergyHolder.takeEnergyFromSources(this, this.world, this.pos, 32);

            if (this.recipeId != null) {
                Optional<?> recipe = this.world.getRecipeManager().get(this.recipeId);
                if (recipe.isPresent()) {
                    this.currentRecipe = (SmeltingRecipe) recipe.get();
                }
                this.recipeId = null;
            }

            if (this.getItems().get(0).isEmpty()
                    || this.energy < 8
                    || (this.currentRecipe != null && !this.canAcceptRecipeOutput(this.currentRecipe))) {
                this.cookTime = MathHelper.clamp(this.cookTime - 1, 0, this.cookTimeTotal);
                return;
            } else if (this.energy == 8) {
                return;
            }

            if (this.currentRecipe == null || !this.currentRecipe.matches(this, this.world)) {
                this.currentRecipe = this.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this, this.world).get();
                this.cookTime = 0;
                return;
            }
            if (this.canAcceptRecipeOutput(this.currentRecipe)) {
                this.cookTime += ((ElectricFurnaceBlock) this.getCachedState().getBlock()).speedMulti;
                this.energy -= 8;
                if (this.cookTime >= this.cookTimeTotal) {
                    this.items.get(0).decrement(1);
                    ItemStack stack = this.items.get(1);
                    if (stack.isEmpty()) {
                        this.setStack(1, currentRecipe.getOutput().copy());
                    } else {
                        stack.increment(1);
                    }
                    this.cookTime = 0;
                    this.cookTimeTotal = this.currentRecipe.getCookTime();
                } else {
                    return;
                }
            }
        }
    }

    protected boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe) {
        if (!(this.items.get(0)).isEmpty() && recipe != null) {
            ItemStack itemStack = recipe.getOutput();
            if (itemStack.isEmpty()) {
                return false;
            } else {
                ItemStack itemStack2 = this.items.get(1);
                if (itemStack2.isEmpty()) {
                    return true;
                } else if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
                    return false;
                } else if (itemStack2.getCount() < this.getMaxCountPerStack() && itemStack2.getCount() < itemStack2.getMaxCount()) {
                    return true;
                } else {
                    return itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        } else {
            return false;
        }
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
        return 2048;
    }

    @Override
    public double getMaxEnergyTransferCapacity(Direction dir, boolean isDraining) {
        return 256;
    }

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            Gui gui = new Gui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    public static class Gui extends SimpleGui {
        private static final Style BATTERY_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GRAY);
        private final ElectricFurnaceBlockEntity blockEntity;
        private final double energyLast = -1;
        private int lastCookTime = -1;
        private int lastCookTimeMax = -1;

        public Gui(ElectricFurnaceBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.FURNACE, player, false);
            Block block = blockEntity.getCachedState().getBlock();
            this.setTitle(new TranslatableText(block.getTranslationKey()));
            this.blockEntity = blockEntity;
        }

        @Override
        public void onUpdate(boolean firstUpdate) {
            if (firstUpdate) {
                this.setSlotRedirect(0, new Slot(this.blockEntity, 0, 0, 0));
                this.setSlotRedirect(2, new OutputSlot(this.blockEntity, 1, 0, 0));

                this.setSlot(1, new GuiElementBuilder(HTItems.BATTERY).setName(HTUtils.getText("gui", "battery_charge",
                        new LiteralText(HTUtils.formatEnergy(this.blockEntity.getEnergyAmount())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.formatEnergy(this.blockEntity.getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                        new LiteralText(HTUtils.dtt(this.blockEntity.getEnergyAmount() / this.blockEntity.getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                ).setStyle(BATTERY_STYLE)));
            }
            super.onUpdate(firstUpdate);
        }

        @Override
        public void onTick() {
            if (this.isOpen()) {
                if (!MathHelper.approximatelyEquals(this.blockEntity.getEnergyAmount(), this.energyLast)) {
                    this.getSlot(1).getItemStack().setCustomName(HTUtils.getText("gui", "battery_charge",
                            new LiteralText(HTUtils.formatEnergy(this.blockEntity.getEnergyAmount())).formatted(Formatting.WHITE),
                            new LiteralText(HTUtils.formatEnergy(this.blockEntity.getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                            new LiteralText(HTUtils.dtt(this.blockEntity.getEnergyAmount() / this.blockEntity.getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                    ).setStyle(BATTERY_STYLE));


                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 1, (int) this.blockEntity.getMaxEnergyCapacity() - 30));
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 0,
                            (int) this.blockEntity.energy - 30));
                }

                if (this.lastCookTime != this.blockEntity.cookTime || this.lastCookTimeMax != this.blockEntity.cookTimeTotal) {
                    this.lastCookTime = this.blockEntity.cookTime;
                    this.lastCookTimeMax = this.blockEntity.cookTimeTotal;

                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 3, this.lastCookTimeMax));
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 2,
                            this.lastCookTime > 0 ? this.lastCookTime : 0));
                }
            }
            super.onTick();
        }

        @Override
        public void onClose() {
            this.blockEntity.openGuis.remove(this);
            super.onClose();
        }
    }
}