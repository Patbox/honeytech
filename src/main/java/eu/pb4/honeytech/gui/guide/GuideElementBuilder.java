package eu.pb4.honeytech.gui.guide;

import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import net.minecraft.item.Item;

public class GuideElementBuilder extends GuiElementBuilder {

    public GuideElementBuilder() {}

    public GuideElementBuilder(Item item, int count) {
        super(item, count);
    }

    public GuideElementBuilder(Item item) {
        super(item);
    }


    public GuideElement build(GuideGui.GuideTab tab) {
        GuiElement tmp = this.build();

        return new GuideElement(tmp.getItemStack(), tab);
    }
}
