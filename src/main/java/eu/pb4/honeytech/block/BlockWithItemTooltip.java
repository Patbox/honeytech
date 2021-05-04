package eu.pb4.honeytech.block;

import net.minecraft.text.Text;

import java.util.Collection;

public interface BlockWithItemTooltip {
    Collection<Text> getTooltip();
}
