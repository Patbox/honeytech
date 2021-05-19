package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.blockentity.electric.ElectricGrinderBlockEntity;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;


public class ElectricGrinderBlock extends GrinderBlock implements VirtualHeadBlock, MachineBlock {
    public final HTTier tier;
    public ElectricGrinderBlock(Settings settings, HTTier tier) {
        super(settings, tier.level);
        this.tier = tier;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new ElectricGrinderBlockEntity();
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA2NTA3NDY5OCwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZjQ0MmUxZTgxNDJlMThhNDIwZjhjNzFiYzE5ZGZiYTJjYWE5NjEyN2I0MWRhMzFkYTYwOGYyNmRhNGMyYWViIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.PLAYER_HEAD;
    }


    @Override
    public double getPerTickEnergyUsage() {
        return 64;
    }

    @Override
    public double getPerTickEnergyProduction() {
        return 0;
    }

    @Override
    public double getMaxEnergyOutput() {
        return 0;
    }

    @Override
    public double getMaxEnergyInput() {
        return tier.energyCapacity / 16;
    }

    @Override
    public double getCapacity() {
        return tier.energyCapacity;
    }

    @Override
    public HTTier getTier() {
        return this.tier;
    }
}
