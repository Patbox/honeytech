package eu.pb4.honeytech.blockentity.electric;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.block.electric.CoalGeneratorBlock;
import eu.pb4.honeytech.blockentity.EnergyHolder;
import eu.pb4.honeytech.blockentity.HTBlockEntities;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CoalGeneratorBlockEntity extends LockableContainerBlockEntity implements ImplementedInventory, EnergyHolder {
    public static Random RANDOM = new Random();
    public final Set<Gui> openGuis = new HashSet<>();
    public boolean isGenerating = false;
    public int cooldown = -1;
    public int maxCooldown = 0;
    public boolean isPaused = false;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public final SimpleEnergyStorage energyStorage;

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(HTBlockEntities.COAL_GENERATOR, pos ,state);
        this.energyStorage = HTUtils.createEnergyStorage(this, ElectricMachine.of(state));

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }


    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState state, T t) {
        if (!(t instanceof CoalGeneratorBlockEntity self)) {
            return;
        }
        if (self.world.isClient) {
            return;
        }

        int slot = -1;


        ElectricMachine machine = ((ElectricMachine) self.getCachedState().getBlock());

        for (int x = 0; x < self.size(); x++) {
            ItemStack stack = self.getStack(x);
            if (!stack.isEmpty() && FuelRegistry.INSTANCE.get(stack.getItem()) != 0) {
                slot = x;
                break;
            }
        }

        if (self.cooldown > 0 && self.energyStorage.getCapacity() > self.energyStorage.getAmount()) {
            self.cooldown--;
            self.isPaused = false;
            self.energyStorage.amount = Math.min(self.energyStorage.getAmount() + machine.getPerTickEnergyProduction(), self.energyStorage.getCapacity());
        } else {
            self.isPaused = true;
        }

        if (self.cooldown <= 0 && self.energyStorage.getCapacity() > self.energyStorage.getAmount() && slot != -1) {
            ItemStack stack = self.removeStack(slot, 1);

            if (stack.getCount() > 0) {
                self.maxCooldown = (int) (FuelRegistry.INSTANCE.get(stack.getItem()).intValue() / ((CoalGeneratorBlock) self.getCachedState().getBlock()).tier.energyMultiplier);
                self.cooldown = self.maxCooldown;
                self.isGenerating = true;
                if (!self.getCachedState().get(Properties.LIT)) {
                    self.world.setBlockState(self.pos, self.getCachedState().with(Properties.LIT, true));
                }

                if (stack.getItem() instanceof BucketItem) {
                    self.setStack(slot, Items.BUCKET.getDefaultStack());
                }
            }
        }

        if (slot == -1 || self.energyStorage.getCapacity() <= self.energyStorage.getAmount()) {
            self.isGenerating = false;
            if (self.getCachedState().get(Properties.LIT)) {
                self.world.setBlockState(self.pos, self.getCachedState().with(Properties.LIT, false));
            }
        }

        for (var dir : Direction.values()) {
            var storage = EnergyStorage.SIDED.find(world, self.getPos().offset(dir), dir.getOpposite());

            if (storage != null && storage.supportsInsertion()) {
                try (var transaction = Transaction.openOuter()) {
                    // Try to extract, will return how much was actually extracted
                    long amountExtracted = self.energyStorage.extract(machine.getMaxEnergyOutput(), transaction);
                    if (amountExtracted != 0) {
                        var rest = amountExtracted - storage.insert(amountExtracted, transaction);
                        if (rest != amountExtracted) {
                            self.energyStorage.insert(rest, transaction);
                            transaction.commit();
                        } else {
                            transaction.abort();
                        }
                    }
                }
            }
        }

        if (self.isGenerating) {
            try {
                if (RANDOM.nextDouble() < 0.1) {
                    //Vec3d vec = HTUtils.vecFromPolar(0, this.getCachedState().get(Properties.ROTATION) * 24 - 180);

                    double x = self.pos.getX() + 0.5;
                    double y = self.pos.getY() + 0.25;
                    double z = self.pos.getZ() + 0.5;

                    //double xR = RANDOM.nextDouble() * vec.x;
                    //double zR = RANDOM.nextDouble() * vec.z;

                    if (RANDOM.nextDouble() < 0.1) {
                        self.world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    }

                    ((ServerWorld) self.world).spawnParticles(ParticleTypes.FLAME,
                            x /*+ vec.x / 3 + zR*/,
                            y,
                            z /*+ vec.z / 3 + xR*/, 1, 0.15f, 0.15f, 0.15f, 0.0001f);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putLong("Energy", this.energyStorage.amount);
        tag.putInt("Cooldown", this.cooldown);
        tag.putInt("MaxCooldown", this.maxCooldown);

        Inventories.writeNbt(tag, items);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        this.energyStorage.amount = tag.getLong("Energy");
        this.cooldown = tag.getInt("Cooldown");
        this.maxCooldown = tag.getInt("MaxCooldown");
        Inventories.readNbt(tag, items);
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
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
        private final long energyLast = -1;
        private int lastCooldown = -1;
        private boolean wasPaused = false;

        public Gui(CoalGeneratorBlockEntity blockEntity, ServerPlayerEntity player) {
            super(ScreenHandlerType.FURNACE, player, false);
            Block block = blockEntity.getCachedState().getBlock();
            this.setTitle(new TranslatableText(block.getTranslationKey()));
            this.blockEntity = blockEntity;
            this.setSlotRedirect(0, new Slot(this.blockEntity, 0, 0, 0));
            this.setSlotRedirect(1, new Slot(this.blockEntity, 1, 0, 0));
            updateBattery();
            this.lastCooldown = this.blockEntity.cooldown;
            this.wasPaused = this.blockEntity.isPaused;
            this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 1, this.blockEntity.maxCooldown));
            this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 0,
                    this.blockEntity.cooldown > 0 ? this.blockEntity.cooldown : 0));

            this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 2, 2));
            this.player.networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(this.syncId, 3,
                    this.blockEntity.cooldown > 0 ? this.blockEntity.isPaused ? 1 : 2 : 0));
        }

        private void updateBattery() {
            this.setSlot(2, HTUtils.createBatteryIcon(this.blockEntity.energyStorage));
        }

        @Override
        public void onTick() {
            if (this.isOpen()) {
                if (this.blockEntity.energyStorage.amount != this.energyLast) {
                    this.updateBattery();
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

    @Override
    public EnergyStorage getEnergy() {
        return this.energyStorage;
    }
}
