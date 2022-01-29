package eu.pb4.honeytech.item.general;

import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.api.block.PolymerHeadBlock;
import eu.pb4.polymer.api.item.PolymerHeadBlockItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeadMachineItem extends PolymerHeadBlockItem {
    public HeadMachineItem(PolymerHeadBlock block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.getBlock() instanceof ElectricMachine) {
            ElectricMachine machine = (ElectricMachine) this.getBlock();

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
