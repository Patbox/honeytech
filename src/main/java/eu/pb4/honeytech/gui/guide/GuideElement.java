package eu.pb4.honeytech.gui.guide;

import eu.pb4.sgui.api.elements.GuiElement;
import net.minecraft.item.ItemStack;

public class GuideElement extends GuiElement {
    public final GuideGui.GuideTab tab;

    public GuideElement(ItemStack item, GuideGui.GuideTab tab) {
        super(item, (x, y, z) -> {});
        this.tab = tab;
    }
}
