package eu.pb4.honeytech.other;

import eu.pb4.honeytech.HoneyTechMod;
import eu.pb4.honeytech.block.ElectricMachine;
import eu.pb4.honeytech.item.HTItems;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class HTUtils {
    public static Style WHITE_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.WHITE);
    private static final Style TOOLTIP_BASE = Style.EMPTY.withItalic(false).withColor(Formatting.DARK_GRAY);
    public static String INVALID_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGUyY2UzMzcyYTNhYzk3ZmRkYTU2MzhiZWYyNGIzYmM0OWY0ZmFjZjc1MWZlOWNhZDY0NWYxNWE3ZmI4Mzk3YyJ9fX0=";
    private static final Style BATTERY_STYLE = Style.EMPTY.withItalic(false).withColor(Formatting.GRAY);

    public static Identifier id(String path) {
        return new Identifier(HoneyTechMod.ID, path);
    }

    public static NbtCompound getHeadSkullOwnerTag(String value) {
        NbtCompound skullOwner = new NbtCompound();
        NbtCompound properties = new NbtCompound();
        NbtCompound data = new NbtCompound();
        NbtList textures = new NbtList();
        textures.add(data);

        data.putString("Value", value);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        skullOwner.putIntArray("Id", new int[] { 0, 0, 0, 0 });

        return skullOwner;
    }

    public static MutableText getText(String type, String path, Object... other) {
        return new TranslatableText(Util.createTranslationKey(type, id(path)), other).setStyle(Style.EMPTY.withItalic(false));
    }

    public static String dtt(double x) {
        return String.format("%.2f", ((double) Math.round(x * 100)) / 100);
    }

    public static String formatEnergy(double value) {
        if (value < 1_000) {
            return String.format("%.2fU", ((double) Math.round(value * 100)) / 100);
        } else if (value < 1000000) {
            return String.format("%.2fKU", value / 1000);
        } else if (value < 1000000000) {
            return String.format("%.2fMU", value / 1000000);
        } else {
            return String.format("%.2fGU", value / 1000000000);
        }
    }

    public static SimpleEnergyStorage createEnergyStorage(BlockEntity blockEntity, ElectricMachine machine) {
        return new SimpleEnergyStorage(machine.getCapacity(), machine.getMaxEnergyInput(), machine.getMaxEnergyOutput()) {
            @Override
            protected void onFinalCommit() {
                blockEntity.markDirty();
            }
        };
    }

    public static Vec3d vecFromPolar(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float g = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float h = -MathHelper.cos(-pitch * 0.017453292F);
        float i = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((g * h), i, (f * h));
    }

    public static Text styledTooltip(String path, Object... values) {
        return new LiteralText("» ").setStyle(TOOLTIP_BASE)
                .append(getText("tooltip", path, values).formatted(Formatting.GOLD));
    }

    public static GuiElementInterface createBatteryIcon(EnergyStorage storage) {
        return new GuiElementBuilder(HTItems.BATTERY).setName(HTUtils.getText("gui", "battery_charge",
                new LiteralText(HTUtils.formatEnergy(storage.getAmount())).formatted(Formatting.WHITE),
                new LiteralText(HTUtils.formatEnergy(storage.getCapacity())).formatted(Formatting.WHITE),
                new LiteralText(HTUtils.dtt(((double) storage.getAmount()) / storage.getCapacity() * 100) + "%").formatted(Formatting.WHITE)
        ).setStyle(BATTERY_STYLE)).build();
    }
}
