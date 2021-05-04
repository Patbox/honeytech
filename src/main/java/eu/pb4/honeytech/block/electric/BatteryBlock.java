package eu.pb4.honeytech.block.electric;

import eu.pb4.honeytech.block.BlockWithItemTooltip;
import eu.pb4.honeytech.blockentity.electric.BatteryBlockEntity;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BatteryBlock extends Block implements VirtualHeadBlock, BlockEntityProvider, BlockWithItemTooltip {
    public static IntProperty LEVEL = IntProperty.of("level", 0, 4);
    public final int capacity;
    private final int texture;

    public BatteryBlock(Settings settings, int capacity, int texture) {
        super(settings);
        this.capacity = capacity;
        this.texture = texture;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public Block getVirtualBlock() {
    return Blocks.PLAYER_HEAD;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BatteryBlockEntity();
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        return Blocks.PLAYER_HEAD.getDefaultState();
    }

    @Override
    public String getVirtualHeadSkin(BlockState state) {
        if (this.texture == 1) {
            switch (state.get(LEVEL)) {
                case 0:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTA1MTQyMywKICAicHJvZmlsZUlkIiA6ICIzOTg5OGFiODFmMjU0NmQxOGIyY2ExMTE1MDRkZGU1MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNeVV1aWRJcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82NDVjYTI4NTUzMTQ2Y2M4ZTEwODE4ODgzYjkwYTgwNjFlMzczZDYxMzY4MTMyODY3YzNhODdjYjA0NTZkNDgwIgogICAgfQogIH0KfQ==";
                case 1:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTA2NzQxOSwKICAicHJvZmlsZUlkIiA6ICJiNzQ3OWJhZTI5YzQ0YjIzYmE1NjI4MzM3OGYwZTNjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlZXgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc3NDBiODI1MTVlYzlhOTU0MjRlNmMyMDE2NTcwNmM0MTIwM2I0M2Y4Yjk0MjZmYWMwNDNhZjYwNTBlZjRlMiIKICAgIH0KICB9Cn0";
                case 2:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTA4MjY1OCwKICAicHJvZmlsZUlkIiA6ICJiMGQ0YjI4YmMxZDc0ODg5YWYwZTg2NjFjZWU5NmFhYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5lU2tpbl9vcmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RlM2FjZGMzZmJmNjcyODNlOTM3MTVmOTU2MTg3YmYxNjZlZmI5YTRkNzBmZGZjZTVjODI2M2NkZTAzZjA0YSIKICAgIH0KICB9Cn0";
                case 3:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTA5NTQ1MSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zY2VmZmU0YWM1ZDVhNWE1MzhkYzAyYjY0ZjgyNWU1ZGVjODcyOGYzY2ZlNmQ3NmRlM2YwNmE2ZmU1ZThmNTFlIgogICAgfQogIH0KfQ==";
                case 4:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTExMTM2MSwKICAicHJvZmlsZUlkIiA6ICJhMjk1ODZmYmU1ZDk0Nzk2OWZjOGQ4ZGE0NzlhNDNlZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWVydGVsdG9hc3RpaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg0YzFkZDgzMzE3ZTEyY2I3ZGJmYWFmMWRjZjY0MzA3MzQ1YWQyYjA0ZmY1MjZmNjA5MDM5ODBlMWFmZjA4MSIKICAgIH0KICB9Cn0=";
            }
        } else if (this.texture == 2) {
            switch (state.get(LEVEL)) {
                case 0:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDg3NzUzMCwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ZmM2MjI2YWZkZjkxMmIzMmFiZjYzZTBmMDE5ZjNhYzVlYWJjMDk3ZDE2MjkzMTJkN2I1ODFkMmUyOTNkMTIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
                case 1:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDg4MTQ0NSwKICAicHJvZmlsZUlkIiA6ICJiYzRlZGZiNWYzNmM0OGE3YWM5ZjFhMzlkYzIzZjRmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICI4YWNhNjgwYjIyNDYxMzQwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M5M2U5YmZjZDkxNDJjNDA4YmMzYTBiNGVhYzI0NDBiZmU3MTg5NzBlODYxNzA3OTM4M2U0OThmZmNlYzhiNmUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
                case 2:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDkyNDQ1OSwKICAicHJvZmlsZUlkIiA6ICIyMzYxYmNlZjZkMWM0ZWI1OGNhMDUzNDFjNGU4MGM0YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIaXJvQ2FwdWNjaW5vODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjUyZTIzMTM1YjkzMDY4ODM4OWU3NmYxYTJiNTM5NWRkYWJlODY0Y2U0N2Y0MGM2Y2IyMmUwMDY0ZGQ2YWM5NSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
                case 3:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDIyMjQzOCwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI0OGVhYTQxNGNjZjA1NmJhOTY5ZTdkODAxZmI2YTkyNzhkMGZlYWUxOGUyMTczNTZjYzhhOTQ2NTY0MzU1ZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
                case 4:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDg4NTY1NCwKICAicHJvZmlsZUlkIiA6ICJmNWQwYjFhZTQxNmU0YTE5ODEyMTRmZGQzMWU3MzA1YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJDYXRjaFRoZVdhdmUxMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYWVkMDJiMjRhZmVmNTUwYzIzMjA1NTY3MGFjNzEyODA5MGFiNWNjMDMxYmEzNWE1MWRlMmVkZTc1NmQyM2FkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
            }
        } else if (this.texture == 3) {
            switch (state.get(LEVEL)) {
                case 0:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTE0MzM3MywKICAicHJvZmlsZUlkIiA6ICJlNzkzYjJjYTdhMmY0MTI2YTA5ODA5MmQ3Yzk5NDE3YiIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVfSG9zdGVyX01hbiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mZjNlZWVhMDNiZTcyNjJjMzA5MmZjYmM5ZTc3NDYyY2Y0NzA0OTg1OGY5ZjRkZjQ3Yjk4NTMxN2NkMjQ3MGQ3IgogICAgfQogIH0KfQ==";
                case 1:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1NDI5OTAxOCwKICAicHJvZmlsZUlkIiA6ICJmN2RiZWFlNmE5MWQ0OTYxYTU4NjY5NThiMjc2MjJhNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJzbGltZWdhbWVyNDM3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M3YTNjMjc0MjRhYjUzMmI1YmFiNzczMzllNzcyZjAwMWQ2MTZjODM1YzUxMzUyOGJmYWUwZmU1NzEzODg4MjQiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ";
                case 2:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTE3NjM2MCwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOGFmZWVlYzg1MTFkM2E4ZTFlZWI1ZGFiMDY2NWE1NzM2MzJlZWUxOWJjZWRhMjI5MGE2ZTJmZDBmZWUwYmEwIgogICAgfQogIH0KfQ==";
                case 3:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTE5MjUwOSwKICAicHJvZmlsZUlkIiA6ICI1N2IzZGZiNWY4YTY0OWUyOGI1NDRlNGZmYzYzMjU2ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJYaWthcm8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTYxNjRjMWU1MDNmNmJkNjRkMDFhMThjYTIzOTg2ZDFiY2JjYzk3Mzk1NDhlYWQxZWQ3N2ZhNTZjMGFiYzY1YSIKICAgIH0KICB9Cn0";
                case 4:
                    return "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA1MTIwODM4MywKICAicHJvZmlsZUlkIiA6ICI0OWIzODUyNDdhMWY0NTM3YjBmN2MwZTFmMTVjMTc2NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiY2QyMDMzYzYzZWM0YmY4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQyYzE5NzI1OTNkODI4ZWYwMDE4YjFiMDAxOGNmZWQ5MWE1YTUxOTllNzNmMGNlZjZhZGIzZTAxYmNhM2E0MjEiCiAgICB9CiAgfQp9";
            }
        }

        return HTUtils.INVALID_TEXTURE;
    }

    @Override
    public Collection<Text> getTooltip() {
        List<Text> list = new ArrayList<>();
        list.add(HTUtils.styledTooltip("capacity", new LiteralText(HTUtils.formatEnergy(this.capacity)).formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_in",
                new LiteralText(HTUtils.formatEnergy(BatteryBlockEntity.getMaxTransfer(false)))
                        .formatted(Formatting.GRAY)));
        list.add(HTUtils.styledTooltip("energy_transfer_out",
                new LiteralText(HTUtils.formatEnergy(BatteryBlockEntity.getMaxTransfer(true)))
                        .formatted(Formatting.GRAY)));
        return list;
    }
}
