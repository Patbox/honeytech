package eu.pb4.honeytech.mixin;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockSoundGroup.class)
public interface BlockSoundGroupAccessor {
    @Accessor("breakSound")
    SoundEvent getBreakSoundServer();
}
