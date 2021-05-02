package eu.pb4.honeytech.blockentity.machines_common;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.blockentity.HandlePoweredBlockEntity;
import eu.pb4.honeytech.mixin.BlockSoundGroupAccessor;
import eu.pb4.honeytech.other.HTLootTables;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.other.OutputSlot;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.ClickType;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OreWasherBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, HandlePoweredBlockEntity, VirtualObject {
    public final Set<OreWasherGui> openGuis = new HashSet<>();
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(18, ItemStack.EMPTY);

    public OreWasherBlockEntity() {
        super(HTBlockEntities.ORE_WASHER);
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

        ItemStack itemStack = player.getMainHandStack();
        Identifier itemId = Registry.ITEM.getId(itemStack.getItem());

        LootTable lootTable = world.getServer().getLootManager().getTable(new Identifier(itemId.getNamespace(), "ore_washing/" + itemId.getPath()));

        if (lootTable != LootTable.EMPTY) {
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld)this.world))
                    .random(player.getRandom())
                    .parameter(LootContextParameters.BLOCK_ENTITY, this)
                    .parameter(LootContextParameters.THIS_ENTITY, player).luck(player.getLuck());

            List<ItemStack> items = lootTable.generateLoot(builder.build(HTLootTables.ORE_WASHING_CONTEXT));

            if (items.size() == 0) {
                return ActionResult.success(false);
            }

            for (ItemStack item : items) {
                if (!this.canInsert(item)) {
                    return ActionResult.success(false);
                }
            }
            ItemStack copy = player.getMainHandStack().copy();
            player.getMainHandStack().decrement(1);

            SoundEvent event = SoundEvents.BLOCK_GRAVEL_BREAK;
            if (copy.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) copy.getItem()).getBlock();
                event = ((BlockSoundGroupAccessor) block.getSoundGroup(block.getDefaultState())).getBreakSoundServer();
            }

            world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, event, SoundCategory.BLOCKS, 0.6f, 1f);
            ((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, copy),
                    pos.getX() + 0.5d,
                    pos.getY() + 1d,
                    pos.getZ() + 0.5d, 20,0.1f, 0.1f, 0.1f, 0.1f);

            for (ItemStack item : items) {
                this.addStack(item);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.success(false);
    }

    public static class OreWasherGui extends SimpleGui {
        private final OreWasherBlockEntity blockEntity;

        public OreWasherGui(OreWasherBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.GENERIC_9X2, player, false);
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
        public void onUpdate(boolean firstUpdate) {
            for (int x = 0; x < 9; x++) {
                this.setSlotRedirect(x, new OutputSlot(blockEntity, x, 0, 0));
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
