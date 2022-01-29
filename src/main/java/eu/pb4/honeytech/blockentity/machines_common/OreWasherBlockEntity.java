package eu.pb4.honeytech.blockentity.machines_common;

import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.honeytech.blockentity.electric.ElectricOreWasherBlockEntity;
import eu.pb4.honeytech.other.HTLootTables;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OreWasherBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, SidedInventory, HandlePoweredBlockEntity, VirtualObject {
    private static final int[] SLOTS_18 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
    private static final int[] SLOTS_27 = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

    private final Random random = new Random();
    private double offset = -1;

    public final Set<OreWasherGui> openGuis = new HashSet<>();
    protected DefaultedList<ItemStack> items;

    public OreWasherBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.ORE_WASHER, pos, state);
        this.items = DefaultedList.ofSize(18, ItemStack.EMPTY);
    }

    public OreWasherBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.items = DefaultedList.ofSize(27, ItemStack.EMPTY);
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
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag, this.items);
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

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            OreWasherGui gui = new OreWasherGui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }

    @Override
    public ActionResult useHandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (hand == Hand.OFF_HAND) {
            return ActionResult.success(false);
        }

        if (this.emptySlotsBefore(18) < 3) {
            return ActionResult.FAIL;
        }

        ItemStack itemStack = null;

        if (player != null) {
            itemStack = player.getMainHandStack();
        } else {
            for (int x = 18; x < this.items.size(); x++) {
                ItemStack stack = this.items.get(x);

                if (!stack.isEmpty()) {
                    itemStack = stack;
                    break;
                }
            }
        }

        if (itemStack == null) {
            return ActionResult.FAIL;
        }

        Identifier itemId = Registry.ITEM.getId(itemStack.getItem());

        LootTable lootTable = world.getServer().getLootManager().getTable(new Identifier(itemId.getNamespace(), "ore_washing/" + itemId.getPath()));

        if (lootTable != LootTable.EMPTY) {
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world)).random(random)
                    .parameter(LootContextParameters.BLOCK_ENTITY, this);

            if (player != null) {
                builder = builder.parameter(LootContextParameters.THIS_ENTITY, player).luck(player.getLuck());
            }

            List<ItemStack> items = lootTable.generateLoot(builder.build(HTLootTables.ORE_WASHING_CONTEXT));

            if (items.size() == 0) {
                return ActionResult.success(false);
            }

            for (ItemStack item : items) {
                if (!this.canInsertSlotLimited(item)) {
                    return ActionResult.success(false);
                }
            }
            ItemStack copy = itemStack.copy();
            itemStack.decrement(1);

            SoundEvent event = SoundEvents.BLOCK_GRAVEL_BREAK;
            if (copy.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) copy.getItem()).getBlock();
                event = block.getSoundGroup(block.getDefaultState()).getBreakSound();
            }

            if (this.offset < 0) {
                this.offset = this.getCachedState().getCollisionShape(this.world, this.pos).getMax(Direction.Axis.Y);
            }

            world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, event, SoundCategory.BLOCKS, 0.6f, 1f);
            ((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, copy),
                    pos.getX() + 0.5d,
                    pos.getY() + this.offset,
                    pos.getZ() + 0.5d, 20,0.1f, 0.1f, 0.1f, 0.1f);

            for (ItemStack item : items) {
                this.addStack(item);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.success(false);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return this.items.size() == 18 ? SLOTS_18 : SLOTS_27;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot >= 18;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot < 18;
    }

    private boolean canInsertSlotLimited(ItemStack stack) {
        int x = 0;
        for (ItemStack itemStack : this.getItems()) {
            if (x >= 18) {
                return false;
            }
            if (itemStack.isEmpty() || this.canCombine(itemStack, stack) && itemStack.getCount() < itemStack.getMaxCount()) {
                return true;
            }
            x++;
        }
        return false;
    }

    private int emptySlotsBefore(int max) {
        int x = 0;

        for (int y = 0; y < max; y++) {
            if (this.items.get(y).isEmpty()) {
                x++;
            }
        }

        return x;
    }

    public static class OreWasherGui extends SimpleGui {
        private final OreWasherBlockEntity blockEntity;
        private final long energyLast = -1;
        private static final Style BATTERY_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GRAY);

        public OreWasherGui(OreWasherBlockEntity blockEntity, ServerPlayerEntity player) {
            super(blockEntity.items.size() == 18 ? ScreenHandlerType.GENERIC_9X2 : ScreenHandlerType.GENERIC_9X4, player, false);
            this.setTitle(new TranslatableText(blockEntity.getCachedState().getBlock().getTranslationKey()));
            this.blockEntity = blockEntity;

            if (this.blockEntity instanceof ElectricOreWasherBlockEntity) {
                GuiElementBuilder builder = new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText(""));

                for (int x = 0; x < 9; x++) {
                    this.setSlot(x + 9, builder);
                }

                if (this.blockEntity instanceof EnergyHolder energyHolder) {
                    this.setSlot(13, HTUtils.createBatteryIcon(energyHolder.getEnergy()));
                }

                for (int x = 0; x < 9; x++) {
                    this.setSlotRedirect(x, new Slot(blockEntity, x + 18, 0, 0));
                }

                for (int x = 0; x < 18; x++) {
                    this.setSlotRedirect(x + 18, new OutputSlot(blockEntity, x, 0, 0));
                }
            } else {
                for (int x = 0; x < 18; x++) {
                    this.setSlotRedirect(x, new OutputSlot(blockEntity, x, 0, 0));
                }
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
            if (this.blockEntity instanceof EnergyHolder energyHolder && energyHolder.getEnergy().getAmount() != this.energyLast) {
                this.setSlot(13, HTUtils.createBatteryIcon(energyHolder.getEnergy()));

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
