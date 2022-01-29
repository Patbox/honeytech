package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AutoCrafterBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, SidedInventory, EnergyHolder {
    private static final int[] SLOTS = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

    public final Set<Gui> openGuis = new HashSet<>();
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(28, ItemStack.EMPTY);
    private final ItemStack invalidItemStack = null;
    private long ticker = 0;

    public final SimpleEnergyStorage energyStorage;

    // Use the energy internally, for example in tick()
    public void tick() {
        if (!world.isClient && energyStorage.amount >= 10) {
            energyStorage.amount -= 10;
            // do something with the 10 energy we just used.
            markDirty();
        }
    }

    private final CraftingInventory craftingInventory = new CraftingInventory(new ScreenHandler(null, -1) {
        @Override
        public boolean canUse(PlayerEntity player) {
            return false;
        }
    }, 3, 3);

    private ArrayList<CraftingRecipe> recipes = null;

    public AutoCrafterBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.AUTO_CRAFTER, pos, state);
        var mach = ElectricMachine.of(state);
        this.energyStorage = HTUtils.createEnergyStorage(this, mach);
    }

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            Gui gui = new Gui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.energyStorage.amount = tag.getLong("Energy");
        Inventories.readNbt(tag, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong("Energy", this.energyStorage.amount);
        Inventories.writeNbt(nbt, items);
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


    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof AutoCrafterBlockEntity self)) {
            return;
        }

        if (world.isClient || self.items.get(0).isEmpty() || self.invalidItemStack == self.items.get(0)) {
            return;
        }

        self.ticker++;

        if (self.ticker % 20 != 0) {
            return;
        }

        if (self.validateOrFindRecipe()) {
            return;
        }


    }

    private boolean validateOrFindRecipe() {
        ItemStack target = this.items.get(0);
        if (this.recipes == null || this.recipes.size() == 0 || this.recipes.get(0).getOutput().isItemEqualIgnoreDamage(target)) {
            this.recipes = new ArrayList<>();

            for (CraftingRecipe recipe : this.world.getRecipeManager().listAllOfType(RecipeType.CRAFTING)) {
                if ((recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) &&  recipe.getOutput().isItemEqualIgnoreDamage(target)) {
                    recipes.add(recipe);
                }
            }
        }

        return this.recipes.size() > 0;
    }

    @Override
    public EnergyStorage getEnergy() {
        return this.energyStorage;
    }

    public static class Gui extends SimpleGui {
        private final AutoCrafterBlockEntity blockEntity;
        private final long energyLast = -1;

        public Gui(AutoCrafterBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X6, player, false);
            this.setTitle(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey()));
            this.blockEntity = blockEntity;

            var builder = new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText(""));

            for (int x = 0; x < 27; x++) {
                this.setSlot(x + 18, builder);
            }

            this.setSlotRedirect(29, new Slot(blockEntity, 0, 0, 0));
            this.updateEnergy();

            for (int x = 0; x < 18; x++) {
                this.setSlotRedirect(x, new Slot(blockEntity, x + 1, 0, 0));
            }

            for (int x = 0; x < 9; x++) {
                this.setSlotRedirect(x + 45, new OutputSlot(blockEntity, x + 19, 0, 0));
            }
        }

        @Override
        public boolean onAnyClick(int index, ClickType type, SlotActionType action) {
            Slot slot = this.getSlotRedirect(index);

            if (slot instanceof OutputSlot) {
                this.player.networkHandler.sendPacket(new InventoryS2CPacket(this.syncId, this.screenHandler.nextRevision(), this.screenHandler.getStacks(), this.screenHandler.getCursorStack()));
            }

            return super.onAnyClick(index, type, action);
        }

        @Override
        public void onTick() {
            if (this.blockEntity.energyStorage.getAmount() == this.energyLast) {
                this.updateEnergy();
            }

            super.onTick();
        }

        private void updateEnergy() {
            var storage = this.blockEntity.energyStorage;
            this.setSlot(33, HTUtils.createBatteryIcon(this.blockEntity.energyStorage));
        }

        @Override
        public void onClose() {
            this.blockEntity.openGuis.remove(this);
            super.onClose();
        }
    }
}
