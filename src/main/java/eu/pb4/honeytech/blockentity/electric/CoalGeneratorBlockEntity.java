package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.electric.CoalGeneratorBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.item.HTItems;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.polymer.interfaces.VirtualObject;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.impl.content.registry.FuelRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CoalGeneratorBlockEntity extends LockableContainerBlockEntity implements Tickable, EnergyHolder, ImplementedInventory, VirtualObject {
    public static Random RANDOM = new Random();
    public final Set<Gui> openGuis = new HashSet<>();
    public boolean isGenerating = false;
    public int cooldown = -1;
    public int maxCooldown = 0;
    public boolean isPaused = false;
    private double energy = 0;
    private final int ticker = 0;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public CoalGeneratorBlockEntity() {
        super(HTBlockEntities.COAL_GENERATOR);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }


    @Override
    public void tick() {
        int slot = -1;

        Map<Item, Integer> fuels = FuelRegistryImpl.INSTANCE.getFuelTimes();

        for (int x = 0; x < this.size(); x++) {
            ItemStack stack = this.getStack(x);
            if (!stack.isEmpty() && fuels.containsKey(stack.getItem())) {
                slot = x;
                break;
            }
        }

        if (this.cooldown > 0 && !this.isFullEnergy()) {
            this.cooldown--;
            this.isPaused = false;
            this.energy += 2 * ((CoalGeneratorBlock) this.getCachedState().getBlock()).multiplier;
        } else {
            this.isPaused = true;
        }

        if (cooldown <= 0 && !this.isFullEnergy() && slot != -1) {
            ItemStack stack = this.removeStack(slot, 1);

            if (stack.getCount() > 0) {
                this.maxCooldown = fuels.get(stack.getItem()).intValue() / ((CoalGeneratorBlock) this.getCachedState().getBlock()).multiplier;
                this.cooldown = this.maxCooldown;
                this.isGenerating = true;
                if (!this.getCachedState().get(Properties.LIT)) {
                    this.world.setBlockState(this.pos, this.getCachedState().with(Properties.LIT, true));
                }

                if (stack.getItem() instanceof BucketItem) {
                    this.setStack(slot, Items.BUCKET.getDefaultStack());
                }
            }
        }

        if (slot == -1 || this.isFullEnergy()) {
            this.isGenerating = false;
            if (this.getCachedState().get(Properties.LIT)) {
                this.world.setBlockState(this.pos, this.getCachedState().with(Properties.LIT, false));
            }
        }

        EnergyHolder.provideEnergyToConsumers(this, this.world, this.pos, 512);

        if (this.isGenerating) {
            try {
                if (RANDOM.nextDouble() < 0.1) {
                    //Vec3d vec = HTUtils.vecFromPolar(0, this.getCachedState().get(Properties.ROTATION) * 24 - 180);

                    double x = pos.getX() + 0.5;
                    double y = pos.getY() + 0.25;
                    double z = pos.getZ() + 0.5;

                    //double xR = RANDOM.nextDouble() * vec.x;
                    //double zR = RANDOM.nextDouble() * vec.z;

                    if (RANDOM.nextDouble() < 0.1) {
                        this.world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    }

                    ((ServerWorld) this.world).spawnParticles(ParticleTypes.FLAME,
                            x /*+ vec.x / 3 + zR*/,
                            y,
                            z /*+ vec.z / 3 + xR*/, 1, 0.15f, 0.15f, 0.15f, 0.0001f);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("Energy", this.energy);
        tag.putInt("Cooldown", this.cooldown);
        tag.putInt("MaxCooldown", this.maxCooldown);

        Inventories.toTag(tag, items);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.energy = tag.getDouble("Energy");
        this.cooldown = tag.getInt("Cooldown");
        this.maxCooldown = tag.getInt("MaxCooldown");
        Inventories.fromTag(tag, items);
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
    public double getEnergyAmount() {
        return this.energy;
    }

    @Override
    public void setEnergyAmount(double amount) {
        this.energy = Math.min(amount, this.getMaxEnergyCapacity());
        if (this.energy < 0.001) {
            this.energy = 0;
        }
    }

    @Override
    public boolean isEnergySource() {
        return true;
    }

    @Override
    public boolean isEnergyConsumer() {
        return false;
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
        private final CoalGeneratorBlockEntity blockEntity;
        private double energyLast = -1;
        private int lastCooldown = -1;
        private boolean wasPaused = false;

        public Gui(CoalGeneratorBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.FURNACE, player, false);
            Block block = blockEntity.getCachedState().getBlock();
            this.setTitle(new TranslatableText(block.getTranslationKey()));
            this.blockEntity = blockEntity;
        }

        @Override
        public void onUpdate(boolean firstUpdate) {
            if (firstUpdate) {
                this.setSlotRedirect(0, new Slot(this.blockEntity, 0, 0, 0));
                this.setSlotRedirect(1, new Slot(this.blockEntity, 1, 0, 0));

                this.setSlot(2, new GuiElementBuilder(HTItems.BATTERY).setName(HTUtils.getText("gui", "battery_charge",
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
                    this.getSlot(2).getItemStack().setCustomName(HTUtils.getText("gui", "battery_charge",
                            new LiteralText(HTUtils.formatEnergy(this.blockEntity.getEnergyAmount())).formatted(Formatting.WHITE),
                            new LiteralText(HTUtils.formatEnergy(this.blockEntity.getMaxEnergyCapacity())).formatted(Formatting.WHITE),
                            new LiteralText(HTUtils.dtt(this.blockEntity.getEnergyAmount() / this.blockEntity.getMaxEnergyCapacity() * 100) + "%").formatted(Formatting.WHITE)
                    ).setStyle(BATTERY_STYLE));
                }

                if (this.lastCooldown != this.blockEntity.cooldown || this.wasPaused != this.blockEntity.isPaused) {
                    this.lastCooldown = this.blockEntity.cooldown;
                    this.wasPaused = this.blockEntity.isPaused;
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 1, this.blockEntity.maxCooldown));
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 0,
                            this.blockEntity.cooldown > 0 ? this.blockEntity.cooldown : 0));

                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 2, 2));
                    this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 3,
                            this.blockEntity.cooldown > 0 ? this.blockEntity.isPaused ? 1 : 2 : 0));

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
