package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.block.electric.ElectricFurnaceBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ElectricFurnaceBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, SidedInventory, RecipeUnlocker, RecipeInputProvider, EnergyHolder {
    public final Set<Gui> openGuis = new HashSet<>();

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private SmeltingRecipe lastRecipe = null;
    public final SimpleEnergyStorage energyStorage;
    private int cookTime = 0;
    private int cookTimeTotal = 9999;
    private SmeltingRecipe currentRecipe = null;
    private Identifier recipeId = null;

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.ELECTRIC_FURNACE, pos, state);
        this.energyStorage = HTUtils.createEnergyStorage(this, ElectricMachine.of(state));
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
    public void provideRecipeInputs(RecipeMatcher finder) {
        finder.addInput(this.items.get(0));
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
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.energyStorage.amount = tag.getLong("Energy");
        this.cookTime = tag.getInt("CookTime");
        this.cookTimeTotal = tag.getInt("CookTimeTotal");
        this.recipeId = Identifier.tryParse(tag.getString("Recipe"));
        Inventories.readNbt(tag, this.items);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putDouble("Energy", this.energyStorage.amount);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.cookTimeTotal);
        if (this.currentRecipe != null) {
            tag.putString("Recipe", this.currentRecipe.getId().toString());
        }
        Inventories.writeNbt(tag, this.items);
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof ElectricFurnaceBlockEntity self)) {
            return;
        }

        if (self.world.isClient) {
            return;
        }
        ElectricMachine machine = (ElectricMachine) self.getCachedState().getBlock();

        //EnergyHolder.takeEnergyFromSources(self, self.world, self.pos, machine.getMaxEnergyInput());

        if (self.recipeId != null) {
            Optional<?> recipe = self.world.getRecipeManager().get(self.recipeId);
            if (recipe.isPresent()) {
                self.currentRecipe = (SmeltingRecipe) recipe.get();
                self.cookTimeTotal = self.currentRecipe.getCookTime();
            }
            self.recipeId = null;
        }

        if (self.getItems().get(0).isEmpty()
                || self.energyStorage.amount < machine.getPerTickEnergyUsage()
                || (self.currentRecipe != null && !self.canAcceptRecipeOutput(self.currentRecipe))) {
            self.cookTime = MathHelper.clamp(self.cookTime - 1, 0, self.cookTimeTotal);
            return;
        } else if (self.energyStorage.amount == 8) {
            return;
        }

        if (self.currentRecipe == null || !self.currentRecipe.matches(self, self.world)) {
            Optional<SmeltingRecipe> optional = self.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, self, self.world);
            if (optional.isPresent()) {
                self.currentRecipe = optional.get();
                self.cookTime = 0;
                self.cookTimeTotal = self.currentRecipe.getCookTime();
            } else {
                self.cookTime = MathHelper.clamp(self.cookTime - 1, 0, self.cookTimeTotal);
            }
            return;
        }
        if (self.canAcceptRecipeOutput(self.currentRecipe)) {
            self.cookTime += (int) ((ElectricFurnaceBlock) self.getCachedState().getBlock()).tier.speed;

            self.energyStorage.amount -= machine.getPerTickEnergyUsage();
            if (self.cookTime >= self.cookTimeTotal) {
                self.items.get(0).decrement(1);
                ItemStack stack = self.items.get(1);
                if (stack.isEmpty()) {
                    self.setStack(1, self.currentRecipe.getOutput().copy());
                } else {
                    stack.increment(1);
                }
                self.cookTime = 0;
                self.cookTimeTotal = self.currentRecipe.getCookTime();
            } else {
                return;
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

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            Gui gui = new Gui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    public static class Gui extends SimpleGui {
        private final ElectricFurnaceBlockEntity blockEntity;
        private final long energyLast = -1;
        private int lastCookTime = -1;
        private int lastCookTimeMax = -1;

        public Gui(ElectricFurnaceBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.FURNACE, player, false);
            Block block = blockEntity.getCachedState().getBlock();
            this.setTitle(new TranslatableText(block.getTranslationKey()));
            this.blockEntity = blockEntity;

            this.setSlotRedirect(0, new Slot(this.blockEntity, 0, 0, 0));
            this.setSlotRedirect(2, new OutputSlot(this.blockEntity, 1, 0, 0));

            this.setSlot(1, HTUtils.createBatteryIcon(this.blockEntity.energyStorage));
        }

        @Override
        public void onTick() {
            if (this.isOpen()) {
                if (this.blockEntity.energyStorage.getAmount() != this.energyLast) {
                    this.setSlot(1, HTUtils.createBatteryIcon(this.blockEntity.energyStorage));


                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 1, (int) this.blockEntity.energyStorage.getCapacity() - 30));
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 0,
                            (int) this.blockEntity.energyStorage.getAmount() - 30));
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

    @Override
    public EnergyStorage getEnergy() {
        return this.energyStorage;
    }
}
