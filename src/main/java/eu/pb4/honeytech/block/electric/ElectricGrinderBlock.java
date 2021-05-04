package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.BlockWithItemTooltip;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.blockentity.electric.ElectricGrinderBlockEntity;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ElectricGrinderBlock extends GrinderBlock implements VirtualHeadBlock, BlockWithItemTooltip {
    public ElectricGrinderBlock(Settings settings, int grindingPower) {
        super(settings, grindingPower);
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
    public Collection<Text> getTooltip() {
        List<Text> list = new ArrayList<>();
        list.add(HTUtils.styledTooltip("capacity", new LiteralText(HTUtils.formatEnergy(2048)).formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_in",
                new LiteralText(HTUtils.formatEnergy(512))
                        .formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_out",
                new LiteralText(HTUtils.formatEnergy(512))
                        .formatted(Formatting.GRAY)));
        return list;
    }
}
