package eu.pb4.honeytech.other;

import eu.pb4.honeytech.HoneyTechMod;
import eu.pb4.polymer.block.VirtualHeadBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HTUtils {
    public static Identifier id(String path) {
        return new Identifier(HoneyTechMod.ID, path);
    }

    public static CompoundTag getHeadSkullOwnerTag(String value) {
        CompoundTag skullOwner = new CompoundTag();
        CompoundTag properties = new CompoundTag();
        CompoundTag data = new CompoundTag();
        ListTag textures = new ListTag();
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

    public static Vec3d vecFromPolar(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float g = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float h = -MathHelper.cos(-pitch * 0.017453292F);
        float i = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((g * h), i, (f * h));
    }
}
