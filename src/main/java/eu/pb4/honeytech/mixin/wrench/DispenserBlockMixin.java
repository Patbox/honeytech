package eu.pb4.honeytech.mixin.wrench;

import com.google.common.collect.ImmutableList;
import eu.pb4.honeytech.block.WrenchableBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin implements WrenchableBlock {
    @Override
    public Collection<Property<?>> getWrenchableProperies() {
        return ImmutableList.of(DispenserBlock.FACING);
    }
}
