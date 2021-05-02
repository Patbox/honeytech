package eu.pb4.honeytech.block;

import net.minecraft.state.property.Property;

import java.util.Collection;

public interface WrenchableBlock {
    Collection<Property<?>> getWrenchableProperies();
}
