package eu.pb4.honeytech.item.general;

import eu.pb4.honeytech.block.MachineBlock;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.block.VirtualHeadBlock;
import eu.pb4.polymer.item.VirtualHeadBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class HeadMachineItem extends VirtualHeadBlockItem {
    public HeadMachineItem(VirtualHeadBlock block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void modifyTooltip(List<Text> tooltip, ItemStack stack, ServerPlayerEntity player) {
        if (this.getBlock() instanceof MachineBlock) {
            MachineBlock machine = (MachineBlock) this.getBlock();

            if (machine.getCapacity() > 0) {
                tooltip.add(HTUtils.styledTooltip("capacity",
                        new LiteralText(HTUtils.formatEnergy(machine.getCapacity()))
                                .formatted(Formatting.WHITE)));
            }

            if (machine.getMaxEnergyInput() > 0) {

                tooltip.add(HTUtils.styledTooltip("energy_transfer_in",
                        new LiteralText(HTUtils.formatEnergy(machine.getMaxEnergyInput()) + "/tick")
                                .formatted(Formatting.WHITE)));
            }

            if (machine.getMaxEnergyOutput() > 0) {
                tooltip.add(HTUtils.styledTooltip("energy_transfer_out",
                        new LiteralText(HTUtils.formatEnergy(machine.getMaxEnergyOutput()) + "/tick")
                                .formatted(Formatting.WHITE)));
            }

            if (machine.getPerTickEnergyUsage() > 0) {
                tooltip.add(HTUtils.styledTooltip("energy_consumption",
                        new LiteralText(HTUtils.formatEnergy(machine.getPerTickEnergyUsage()) + "/tick")
                                .formatted(Formatting.WHITE)));
            }

            if (machine.getPerTickEnergyProduction() > 0) {
                tooltip.add(HTUtils.styledTooltip("energy_production",
                        new LiteralText(HTUtils.formatEnergy(machine.getPerTickEnergyProduction()) + "/tick")
                                .formatted(Formatting.WHITE)));
            }
        }
    }
}
