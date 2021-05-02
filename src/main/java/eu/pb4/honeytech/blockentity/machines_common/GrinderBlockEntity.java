package eu.pb4.honeytech.blockentity.machines_common;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.mixin.BlockSoundGroupAccessor;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.RecipeBooks;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.honeytech.recipe_types.GrinderRecipe;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GrinderBlockEntity extends LockableContainerBlockEntity implements SidedInventory, HandlePoweredBlockEntity, VirtualObject {
    private final ImplementedInventory input;
    private final ImplementedInventory output;
    public final Set<GrinderGui> openGuis = new HashSet<>();
    private int click = 0;

    public GrinderBlockEntity() {
        super(HTBlockEntities.GRINDER);
        this.input = ImplementedInventory.ofSize(9);
        this.output = ImplementedInventory.ofSize(9);
    }


    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        CompoundTag input = new CompoundTag();
        Inventories.toTag(input, this.input.getItems());

        CompoundTag output = new CompoundTag();
        Inventories.toTag(output, this.output.getItems());

        tag.put("input", input);
        tag.put("output", output);

        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        try {
            Inventories.fromTag(tag.getCompound("input"), this.input.getItems());
            Inventories.fromTag(tag.getCompound("output"), this.output.getItems());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @Nullable
    public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return null;
    }

    public void openInventory(ServerPlayerEntity player) {
        if (this.checkUnlocked(player)) {
            GrinderGui gui = new GrinderGui(this, player);
            this.openGuis.add(gui);
            gui.open();
        }
    }



    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot < 9 && dir != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot >= 9;
    }

    @Override
    public int size() {
        return this.input.size() + this.output.size();
    }

    @Override
    public boolean isEmpty() {
        return this.input.isEmpty() && this.output.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot < 9 ? this.input.getStack(slot) : this.output.getStack(slot - 9);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return slot < 9 ? this.input.removeStack(slot, amount) : this.output.removeStack(slot - 9, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return slot < 9 ? this.input.removeStack(slot) : this.output.removeStack(slot - 9);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot < 9) {
            this.input.setStack(slot, stack);
        } else {
            this.output.setStack(slot - 9, stack);
        }

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.input.clear();
        this.output.clear();
    }

    @Override
    public ActionResult useHandle(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (this.input.isEmpty()) {
            return ActionResult.FAIL;
        }

        int power = ((GrinderBlock) state.getBlock()).grindingPower;

        Optional<GrinderRecipe> match = world.getRecipeManager()
                .getFirstMatch(GrinderRecipe.Type.INSTANCE, this.input, world);

        if (match.isPresent()) {
            GrinderRecipe recipe = match.get();
            if (power < recipe.getRequiredPower()) {
                return ActionResult.FAIL;
            }
            this.click += 1;
            if (player != null) {
                world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.1f, 1f);
            }
            if (this.click < recipe.getRequiredClicks()) {
                return ActionResult.SUCCESS;
            }
            for (int x = 0; x < this.input.size(); x++) {
                ItemStack stack = this.input.getStack(x).copy();
                if (!stack.isEmpty()) {
                    ItemStack output = recipe.getOutput();
                    if (this.output.canInsert(output)) {
                        this.input.removeStack(x, 1);
                        this.output.addStack(output);
                        this.click = 0;
                         ((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack),
                                pos.getX() + 0.5d,
                                pos.getY() + 1d,
                                pos.getZ() + 0.5d, 20,0.1f, 0.1f, 0.1f, 0.1f);

                        SoundEvent event = SoundEvents.BLOCK_STONE_BREAK;
                        if (stack.getItem() instanceof BlockItem) {
                            Block block = ((BlockItem) stack.getItem()).getBlock();
                            event = ((BlockSoundGroupAccessor) block.getSoundGroup(block.getDefaultState())).getBreakSoundServer();
                        }

                        world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, event, SoundCategory.BLOCKS, 0.4f, 1f);

                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                }
            }
        }

        return ActionResult.FAIL;
    }

    public static class GrinderGui extends SimpleGui {
        private final GrinderBlockEntity blockEntity;
        private final int grinderPower;

        public GrinderGui(GrinderBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X3, player, false);
            GrinderBlock block = (GrinderBlock) blockEntity.getCachedState().getBlock();
            this.setTitle(new TranslatableText(block.getTranslationKey()));
            this.blockEntity = blockEntity;
            this.grinderPower = block.grindingPower;
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
        public void onUpdate(boolean firstUpdate) {
            for (int x = 0; x < 9; x++) {
                this.setSlotRedirect((x / 3) * 9 + x % 3, new Slot(blockEntity.input, x, 0, 0));
                if (firstUpdate) {
                    this.setSlot((x / 3) * 9 + x % 3 + 3, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE).setName(new LiteralText("")));
                }
                this.setSlotRedirect((x / 3) * 9 + x % 3 + 6, new OutputSlot(blockEntity.output, x, 0, 0));
            }

            if (firstUpdate) {
                this.setSlot(4 + 9 * 2,
                        new GuiElementBuilder(Items.KNOWLEDGE_BOOK)
                                .setName(HTUtils.getText("gui", "show_recipes"))
                                .setCallback((index, type, action) -> {
                                    this.close(false);

                                    RecipeBooks.openGrinderRecipeBook(player, this.grinderPower, () -> blockEntity.openInventory(player));
                                }));

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




