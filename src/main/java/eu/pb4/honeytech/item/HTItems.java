package eu.pb4.honeytech.item;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.item.general.GlowingBlockItem;
import eu.pb4.honeytech.item.general.GlowingItem;
import eu.pb4.honeytech.item.general.HeadBlockItem;
import eu.pb4.honeytech.item.general.HeadItem;
import eu.pb4.honeytech.item.other.GuideItem;
import eu.pb4.honeytech.item.other.PortableCraftingItem;
import eu.pb4.honeytech.item.other.PortableTrashCanItem;
import eu.pb4.honeytech.item.tools.AmmeterItem;
import eu.pb4.honeytech.item.tools.WrenchItem;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.item.BasicVirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;


public class HTItems {
    public static Item GUIDE_BOOK = new GuideItem(new Item.Settings().maxCount(1));

    public static Item WRENCH = new WrenchItem(new Item.Settings().maxCount(1));

    public static Item BASIC_GRINDER = new GlowingBlockItem(HTBlocks.BASIC_GRINDER, Items.DISPENSER, new Item.Settings());
    public static Item TABLE_SAW = new GlowingBlockItem(HTBlocks.TABLE_SAW, Items.STONECUTTER, new Item.Settings());
    public static Item BASIC_ORE_WASHER = new GlowingBlockItem(HTBlocks.BASIC_ORE_WASHER, Items.CAULDRON, new Item.Settings());

    public static Item PORTABLE_CRAFTING = new PortableCraftingItem(new Item.Settings().maxCount(1));

    public static Item IRON_DUST = new BasicVirtualItem(new Item.Settings(), Items.GUNPOWDER);
    public static Item GOLD_DUST = new BasicVirtualItem(new Item.Settings(), Items.GLOWSTONE_DUST);
    public static Item DIAMOND_DUST = new BasicVirtualItem(new Item.Settings(), Items.LIGHT_BLUE_DYE);
    public static Item COAL_DUST = new BasicVirtualItem(new Item.Settings(), Items.GUNPOWDER);
    public static Item COPPER_DUST = new BasicVirtualItem(new Item.Settings(), Items.GLOWSTONE_DUST);
    public static Item ALUMINIUM_DUST = new BasicVirtualItem(new Item.Settings(), Items.SUGAR);

    public static Item COPPER_INGOT = new BasicVirtualItem(new Item.Settings(), Items.BRICK);
    public static Item ALUMINIUM_INGOT = new BasicVirtualItem(new Item.Settings(), Items.IRON_INGOT);

    public static Item PIPE = new GlowingBlockItem(HTBlocks.PIPE, Items.DIORITE_WALL, new Item.Settings());
    public static Item ITEM_EXTRACTOR = new GlowingBlockItem(HTBlocks.ITEM_EXTRACTOR, Items.GRINDSTONE, new Item.Settings());

    public static Item ENCHANCED_FURNACE = new GlowingBlockItem(HTBlocks.ENCHANCED_FURNACE, Items.FURNACE, new Item.Settings());

    public static Item TRASHCAN = new HeadBlockItem(HTBlocks.TRASHCAN, new Item.Settings());
    public static Item PORTABLE_TRASHCAN = new PortableTrashCanItem(new Item.Settings());

    public static Item COPPER_WIRE = new GlowingItem(new Item.Settings(), Items.STRING);
    public static Item MAGNET = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==");
    public static Item MOTOR = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==");
    public static Item CIRCUIT_BOARD = new GlowingItem(new Item.Settings(), Items.ACTIVATOR_RAIL);
    public static Item FAN = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUxOTQ0YjQ4OGUxMWNkYTY1MTc3ZDU5MTFkNjUxMjgyYjMwMTI2NjVlNjNiODkyOWUxYjZhNDc0NGI3Y2E4In19fQ==");
    public static Item BATTERY = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjRmMjFjZjVjMjM0ZmM5NmRiOTBhMGEzMTFkNmZiZTEyZjg3ODliN2ZhODE1NTcxNjc1N2ZkNTE2YjE4MTEifX19");
    public static Item ENERGY_REGULATOR = new HeadItem(new Item.Settings(), "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA2NDE1MDczNCwKICAicHJvZmlsZUlkIiA6ICI4OGU0YWNiYTQwOTc0YWZkYmE0ZDM1YjdlYzdmNmJmYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJKb2FvMDkxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzhlMTYwM2E0MGY0Y2Y3Y2VhMzY4NGQ0ODg2YzdjMWZmOWY2NDM5YjhlM2FiOWFiYjgyNzZlNTM5OTdjYTE4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");

    public static Item CABLE = new GlowingBlockItem(HTBlocks.CABLE, Items.NETHER_BRICK_FENCE, new Item.Settings());
    public static Item COAL_GENERATOR_MK1 = new HeadBlockItem(HTBlocks.COAL_GENERATOR_MK1, new Item.Settings());
    public static Item COAL_GENERATOR_MK2 = new HeadBlockItem(HTBlocks.COAL_GENERATOR_MK2, new Item.Settings());
    public static Item COAL_GENERATOR_MK3 = new HeadBlockItem(HTBlocks.COAL_GENERATOR_MK3, new Item.Settings());

    public static Item SMALL_BATTERY = new HeadBlockItem(HTBlocks.SMALL_BATTERY, new Item.Settings());
    public static Item MEDIUM_BATTERY = new HeadBlockItem(HTBlocks.MEDIUM_BATTERY, new Item.Settings());
    public static Item BIG_BATTERY = new HeadBlockItem(HTBlocks.BIG_BATTERY, new Item.Settings());

    public static Item AMMETER = new AmmeterItem(new Item.Settings());

    public static Item ELECTRIC_GRINDER_MK1 = new HeadBlockItem(HTBlocks.ELECTRIC_GRINDER_MK1, new Item.Settings());
    public static Item ELECTRIC_GRINDER_MK2 = new HeadBlockItem(HTBlocks.ELECTRIC_GRINDER_MK2, new Item.Settings());
    public static Item ELECTRIC_GRINDER_MK3 = new HeadBlockItem(HTBlocks.ELECTRIC_GRINDER_MK3, new Item.Settings());

    public static Item ELECTRIC_FURNACE_MK1 = new HeadBlockItem(HTBlocks.ELECTRIC_FURNACE_MK1, new Item.Settings());
    public static Item ELECTRIC_FURNACE_MK2 = new HeadBlockItem(HTBlocks.ELECTRIC_FURNACE_MK2, new Item.Settings());
    public static Item ELECTRIC_FURNACE_MK3 = new HeadBlockItem(HTBlocks.ELECTRIC_FURNACE_MK3, new Item.Settings());


    public static Item GUIDE_ITEM_ELECTRONICS = new HeadItem(new Item.Settings(), "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDIyMjQzOCwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI0OGVhYTQxNGNjZjA1NmJhOTY5ZTdkODAxZmI2YTkyNzhkMGZlYWUxOGUyMTczNTZjYzhhOTQ2NTY0MzU1ZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");

    //public static Item ENGINE = new HeadItem(new Item.Settings(), "");

    public static void register() {
        Registry.register(Registry.ITEM, HTUtils.id("guide_book"), GUIDE_BOOK);
        Registry.register(Registry.ITEM, HTUtils.id("wrench"), WRENCH);

        Registry.register(Registry.ITEM, HTUtils.id("basic_grinder"), BASIC_GRINDER);
        Registry.register(Registry.ITEM, HTUtils.id("table_saw"), TABLE_SAW);
        Registry.register(Registry.ITEM, HTUtils.id("basic_ore_washer"), BASIC_ORE_WASHER);
        Registry.register(Registry.ITEM, HTUtils.id("enchanced_furnace"), ENCHANCED_FURNACE);

        Registry.register(Registry.ITEM, HTUtils.id("portable_crafting"), PORTABLE_CRAFTING);

        Registry.register(Registry.ITEM, HTUtils.id("iron_dust"), IRON_DUST);
        Registry.register(Registry.ITEM, HTUtils.id("gold_dust"), GOLD_DUST);
        Registry.register(Registry.ITEM, HTUtils.id("diamond_dust"), DIAMOND_DUST);
        Registry.register(Registry.ITEM, HTUtils.id("coal_dust"), COAL_DUST);
        Registry.register(Registry.ITEM, HTUtils.id("copper_dust"), COPPER_DUST);
        Registry.register(Registry.ITEM, HTUtils.id("aluminium_dust"), ALUMINIUM_DUST);

        Registry.register(Registry.ITEM, HTUtils.id("copper_ingot"), COPPER_INGOT);
        Registry.register(Registry.ITEM, HTUtils.id("aluminium_ingot"), ALUMINIUM_INGOT);

        Registry.register(Registry.ITEM, HTUtils.id("pipe"), PIPE);
        Registry.register(Registry.ITEM, HTUtils.id("item_extractor"), ITEM_EXTRACTOR);

        Registry.register(Registry.ITEM, HTUtils.id("trashcan"), TRASHCAN);
        Registry.register(Registry.ITEM, HTUtils.id("portable_trashcan"), PORTABLE_TRASHCAN);

        Registry.register(Registry.ITEM, HTUtils.id("copper_wire"), COPPER_WIRE);
        Registry.register(Registry.ITEM, HTUtils.id("magnet"), MAGNET);
        Registry.register(Registry.ITEM, HTUtils.id("motor"), MOTOR);
        Registry.register(Registry.ITEM, HTUtils.id("circuit_board"), CIRCUIT_BOARD);
        Registry.register(Registry.ITEM, HTUtils.id("fan"), FAN);
        Registry.register(Registry.ITEM, HTUtils.id("battery"), BATTERY);
        Registry.register(Registry.ITEM, HTUtils.id("energy_regulator"), ENERGY_REGULATOR);

        Registry.register(Registry.ITEM, HTUtils.id("cable"), CABLE);
        Registry.register(Registry.ITEM, HTUtils.id("coal_generator_mk1"), COAL_GENERATOR_MK1);
        Registry.register(Registry.ITEM, HTUtils.id("coal_generator_mk2"), COAL_GENERATOR_MK2);
        Registry.register(Registry.ITEM, HTUtils.id("coal_generator_mk3"), COAL_GENERATOR_MK3);

        Registry.register(Registry.ITEM, HTUtils.id("ammeter"), AMMETER);

        Registry.register(Registry.ITEM, HTUtils.id("small_battery"), SMALL_BATTERY);
        Registry.register(Registry.ITEM, HTUtils.id("medium_battery"), MEDIUM_BATTERY);
        Registry.register(Registry.ITEM, HTUtils.id("big_battery"), BIG_BATTERY);

        Registry.register(Registry.ITEM, HTUtils.id("electric_grinder_mk1"), ELECTRIC_GRINDER_MK1);
        Registry.register(Registry.ITEM, HTUtils.id("electric_grinder_mk2"), ELECTRIC_GRINDER_MK2);
        Registry.register(Registry.ITEM, HTUtils.id("electric_grinder_mk3"), ELECTRIC_GRINDER_MK3);

        Registry.register(Registry.ITEM, HTUtils.id("electric_furnace_mk1"), ELECTRIC_FURNACE_MK1);
        Registry.register(Registry.ITEM, HTUtils.id("electric_furnace_mk2"), ELECTRIC_FURNACE_MK2);
        Registry.register(Registry.ITEM, HTUtils.id("electric_furnace_mk3"), ELECTRIC_FURNACE_MK3);

    }
}
