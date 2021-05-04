package eu.pb4.honeytech.item.general;

import eu.pb4.honeytech.block.BlockWithItemTooltip;
import eu.pb4.polymer.block.VirtualHeadBlock;
import eu.pb4.polymer.item.VirtualHeadBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class HeadBlockItem extends VirtualHeadBlockItem {
    public HeadBlockItem(VirtualHeadBlock block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void addTextToTooltip(List<Text> tooltip, ItemStack stack, ServerPlayerEntity player) {
        if (this.getBlock() instanceof BlockWithItemTooltip) {
            for (Text text : ((BlockWithItemTooltip) this.getBlock()).getTooltip()) {
                tooltip.add(text);
            }
        }
    }
}
