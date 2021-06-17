package eu.pb4.honeytech.item;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.item.general.GlowingBlockItem;
import eu.pb4.honeytech.item.general.GlowingItem;
import eu.pb4.honeytech.item.general.HeadItem;
import eu.pb4.honeytech.item.general.HeadMachineItem;
import eu.pb4.honeytech.item.other.GuideItem;
import eu.pb4.honeytech.item.other.PortableCraftingItem;
import eu.pb4.honeytech.item.other.PortableTrashCanItem;
import eu.pb4.honeytech.item.tools.AmmeterItem;
import eu.pb4.honeytech.item.tools.WrenchItem;
import eu.pb4.honeytech.other.HTDataPack;
import eu.pb4.honeytech.other.HTUtils;
import eu.pb4.polymer.item.BasicVirtualItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.tuple.Pair;


public class HTItems {
    public static Item GUIDE_BOOK = new GuideItem(new Item.Settings().maxCount(1));

    public static Item WRENCH = new WrenchItem(new Item.Settings().maxCount(1));

    public static Item BASIC_GRINDER = new GlowingBlockItem(HTBlocks.BASIC_GRINDER, Items.DISPENSER, new Item.Settings());
    public static Item TABLE_SAW = new GlowingBlockItem(HTBlocks.TABLE_SAW, Items.STONECUTTER, new Item.Settings());
    public static Item BASIC_ORE_WASHER = new GlowingBlockItem(HTBlocks.BASIC_ORE_WASHER, Items.CAULDRON, new Item.Settings());

    public static Item PORTABLE_CRAFTING = new PortableCraftingItem(new Item.Settings().maxCount(1));

    public static Item COAL_DUST = new BasicVirtualItem(new Item.Settings(), Items.GUNPOWDER);
    public static Item RAW_ALUMINIUM = new BasicVirtualItem(new Item.Settings(), Items.RAW_IRON);

    public static Item ALUMINIUM_INGOT = new BasicVirtualItem(new Item.Settings(), Items.IRON_INGOT);

    public static Item PIPE = new GlowingBlockItem(HTBlocks.PIPE, Items.DIORITE_WALL, new Item.Settings());
    public static Item ITEM_EXTRACTOR = new GlowingBlockItem(HTBlocks.ITEM_EXTRACTOR, Items.GRINDSTONE, new Item.Settings());

    public static Item ENCHANCED_FURNACE = new GlowingBlockItem(HTBlocks.ENCHANCED_FURNACE, Items.FURNACE, new Item.Settings());

    public static Item TRASHCAN = new HeadMachineItem(HTBlocks.TRASHCAN, new Item.Settings());
    public static Item PORTABLE_TRASHCAN = new PortableTrashCanItem(new Item.Settings());

    public static Item COPPER_WIRE = new GlowingItem(new Item.Settings(), Items.STRING);
    public static Item MAGNET = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJhOGViYzRjNmE4MTczMDk0NzQ5OWJmN2UxZDVlNzNmZWQ2YzFiYjJjMDUxZTk2ZDM1ZWIxNmQyNDYxMGU3In19fQ==");
    public static Item MOTOR = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiY2EwMTJmNjdlNTRkZTlhZWU3MmZmNDI0ZTA1NmMyYWU1OGRlNWVhY2M5NDlhYjJiY2Q5NjgzY2VjIn19fQ==");
    public static Item CIRCUIT_BOARD = new GlowingItem(new Item.Settings(), Items.ACTIVATOR_RAIL);
    public static Item FAN = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUxOTQ0YjQ4OGUxMWNkYTY1MTc3ZDU5MTFkNjUxMjgyYjMwMTI2NjVlNjNiODkyOWUxYjZhNDc0NGI3Y2E4In19fQ==");
    public static Item BATTERY = new HeadItem(new Item.Settings(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjRmMjFjZjVjMjM0ZmM5NmRiOTBhMGEzMTFkNmZiZTEyZjg3ODliN2ZhODE1NTcxNjc1N2ZkNTE2YjE4MTEifX19");
    public static Item ENERGY_REGULATOR = new HeadItem(new Item.Settings(), "ewogICJ0aW1lc3RhbXAiIDogMTYyMDA2NDE1MDczNCwKICAicHJvZmlsZUlkIiA6ICI4OGU0YWNiYTQwOTc0YWZkYmE0ZDM1YjdlYzdmNmJmYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJKb2FvMDkxNSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzhlMTYwM2E0MGY0Y2Y3Y2VhMzY4NGQ0ODg2YzdjMWZmOWY2NDM5YjhlM2FiOWFiYjgyNzZlNTM5OTdjYTE4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");

    public static Item CABLE = new GlowingBlockItem(HTBlocks.CABLE, Items.NETHER_BRICK_FENCE, new Item.Settings());
    public static Item COAL_GENERATOR_MK1 = new HeadMachineItem(HTBlocks.COAL_GENERATOR_MK1, new Item.Settings());
    public static Item COAL_GENERATOR_MK2 = new HeadMachineItem(HTBlocks.COAL_GENERATOR_MK2, new Item.Settings());
    public static Item COAL_GENERATOR_MK3 = new HeadMachineItem(HTBlocks.COAL_GENERATOR_MK3, new Item.Settings());

    public static Item SMALL_BATTERY = new HeadMachineItem(HTBlocks.SMALL_BATTERY, new Item.Settings());
    public static Item MEDIUM_BATTERY = new HeadMachineItem(HTBlocks.MEDIUM_BATTERY, new Item.Settings());
    public static Item BIG_BATTERY = new HeadMachineItem(HTBlocks.BIG_BATTERY, new Item.Settings());

    public static Item AMMETER = new AmmeterItem(new Item.Settings());

    public static Item ELECTRIC_GRINDER_MK1 = new HeadMachineItem(HTBlocks.ELECTRIC_GRINDER_MK1, new Item.Settings());
    public static Item ELECTRIC_GRINDER_MK2 = new HeadMachineItem(HTBlocks.ELECTRIC_GRINDER_MK2, new Item.Settings());
    public static Item ELECTRIC_GRINDER_MK3 = new HeadMachineItem(HTBlocks.ELECTRIC_GRINDER_MK3, new Item.Settings());

    public static Item ELECTRIC_FURNACE_MK1 = new HeadMachineItem(HTBlocks.ELECTRIC_FURNACE_MK1, new Item.Settings());
    public static Item ELECTRIC_FURNACE_MK2 = new HeadMachineItem(HTBlocks.ELECTRIC_FURNACE_MK2, new Item.Settings());
    public static Item ELECTRIC_FURNACE_MK3 = new HeadMachineItem(HTBlocks.ELECTRIC_FURNACE_MK3, new Item.Settings());

    public static Item ELECTRIC_ORE_WASHER_MK1 = new HeadMachineItem(HTBlocks.ELECTRIC_ORE_WASHER_MK1, new Item.Settings());
    public static Item ELECTRIC_ORE_WASHER_MK2 = new HeadMachineItem(HTBlocks.ELECTRIC_ORE_WASHER_MK2, new Item.Settings());
    public static Item ELECTRIC_ORE_WASHER_MK3 = new HeadMachineItem(HTBlocks.ELECTRIC_ORE_WASHER_MK3, new Item.Settings());


    public static Item GUIDE_ITEM_ELECTRONICS = new HeadItem(new Item.Settings(), "ewogICJ0aW1lc3RhbXAiIDogMTYxOTk3MDIyMjQzOCwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI0OGVhYTQxNGNjZjA1NmJhOTY5ZTdkODAxZmI2YTkyNzhkMGZlYWUxOGUyMTczNTZjYzhhOTQ2NTY0MzU1ZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");

    //public static Item ENGINE = new HeadItem(new Item.Settings(), "");

    public static void createItems() {
        register("guide_book", GUIDE_BOOK);
        register("wrench", WRENCH);

        register("basic_grinder", BASIC_GRINDER);
        register("table_saw", TABLE_SAW);
        register("basic_ore_washer", BASIC_ORE_WASHER);
        register("enchanced_furnace", ENCHANCED_FURNACE);

        register("portable_crafting", PORTABLE_CRAFTING);

        register("coal_dust", COAL_DUST);
        register("raw_aluminium", RAW_ALUMINIUM);

        register("aluminium_ingot", ALUMINIUM_INGOT);

        register("pipe", PIPE);
        register("item_extractor", ITEM_EXTRACTOR);

        register("trashcan", TRASHCAN);
        register("portable_trashcan", PORTABLE_TRASHCAN);

        register("copper_wire", COPPER_WIRE);
        register("magnet", MAGNET);
        register("motor", MOTOR);
        register("circuit_board", CIRCUIT_BOARD);
        register("fan", FAN);
        register("battery", BATTERY);
        register("energy_regulator", ENERGY_REGULATOR);

        register("cable", CABLE);
        register("coal_generator_mk1", COAL_GENERATOR_MK1);
        register("coal_generator_mk2", COAL_GENERATOR_MK2);
        register("coal_generator_mk3", COAL_GENERATOR_MK3);

        register("ammeter", AMMETER);

        register("small_battery", SMALL_BATTERY);
        register("medium_battery", MEDIUM_BATTERY);
        register("big_battery", BIG_BATTERY);

        register("electric_grinder_mk1", ELECTRIC_GRINDER_MK1);
        register("electric_grinder_mk2", ELECTRIC_GRINDER_MK2);
        register("electric_grinder_mk3", ELECTRIC_GRINDER_MK3);

        register("electric_furnace_mk1", ELECTRIC_FURNACE_MK1);
        register("electric_furnace_mk2", ELECTRIC_FURNACE_MK2);
        register("electric_furnace_mk3", ELECTRIC_FURNACE_MK3);

        register("electric_ore_washer_mk1", ELECTRIC_ORE_WASHER_MK1);
        register("electric_ore_washer_mk2", ELECTRIC_ORE_WASHER_MK2);
        register("electric_ore_washer_mk3", ELECTRIC_ORE_WASHER_MK3);
    }

    public static void createRecipes() {
        HTDataPack.createCraftingShaped(WRENCH, 1,"I I| S | S ", p("I", Items.IRON_INGOT), p("S", Items.STICK));
        HTDataPack.createCraftingShaped(AMMETER, 1, "I I| S | S ", p("I", Items.GOLD_INGOT), p("S", Items.STICK));
        HTDataPack.createCraftingShaped(BASIC_GRINDER, 1, "CPC|CFC|CFC", p("C", Items.COBBLESTONE), p("P", "minecraft:planks"), p("F", Items.FLINT));
        HTDataPack.createCraftingShaped(BASIC_ORE_WASHER, 1, "T|W|C", p("C", Items.CAULDRON), p("T", "minecraft:trapdoors"), p("W", "minecraft:wool"));
        HTDataPack.createCraftingShaped(BATTERY, 1, "RIR|ADA|ACA", p("I", Items.IRON_INGOT), p("R", Items.REDSTONE), p("C", Items.COPPER_INGOT), p("A", ALUMINIUM_INGOT), p("D", Items.GLOWSTONE));
        HTDataPack.createCraftingShaped(BIG_BATTERY, 1, "IBI|BEB|IBI", p("I", Items.GOLD_INGOT), p("E", ENERGY_REGULATOR), p("B", MEDIUM_BATTERY));

        HTDataPack.createCraftingShaped(CABLE, 6, "NNN|III|NNN", p("N", Items.NETHER_BRICK), p("I", Items.COPPER_INGOT));

        HTDataPack.createCraftingShaped(COAL_GENERATOR_MK1, 1, "IMI|IFI|IBI", p("F", Items.FURNACE), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(COAL_GENERATOR_MK2, 1, "III|MFM|IBI", p("F", COAL_GENERATOR_MK1), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(COAL_GENERATOR_MK3, 1, "III|MFM|IBI", p("F", COAL_GENERATOR_MK2), p("I", Items.IRON_BLOCK), p("M", MOTOR), p("B", BATTERY));

        HTDataPack.createCraftingShaped(COPPER_WIRE, 6, "CC|  ", p("C", Items.COPPER_INGOT));

        HTDataPack.createCraftingShaped(ELECTRIC_FURNACE_MK1, 1, "IMI|IFI|IBI", p("F", Items.FURNACE), p("I", Items.IRON_INGOT), p("M", ENERGY_REGULATOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_FURNACE_MK2, 1, "III|MFM|IBI", p("F", ELECTRIC_FURNACE_MK1), p("I", Items.IRON_INGOT), p("M", ENERGY_REGULATOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_FURNACE_MK3, 1, "III|MFM|IBI", p("F", ELECTRIC_FURNACE_MK2), p("I", Items.IRON_BLOCK), p("M", ENERGY_REGULATOR), p("B", BATTERY));

        HTDataPack.createCraftingShaped(ELECTRIC_GRINDER_MK1, 1, "IMI|IFI|IBI", p("F", BASIC_GRINDER), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_GRINDER_MK2, 1, "III|MFM|IBI", p("F", ELECTRIC_GRINDER_MK1), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_GRINDER_MK3, 1, "III|MFM|IBI", p("F", ELECTRIC_GRINDER_MK2), p("I", Items.IRON_BLOCK), p("M", MOTOR), p("B", BATTERY));

        HTDataPack.createCraftingShaped(ENCHANCED_FURNACE, 1, "III|IFI|III", p("I", Items.IRON_INGOT), p("F", Items.FURNACE));

        HTDataPack.createCraftingShaped(ENERGY_REGULATOR, 1, "RIR|IAI|CIC", p("I", Items.IRON_INGOT), p("R", Items.REDSTONE), p("C", COPPER_WIRE), p("A", ALUMINIUM_INGOT));
        HTDataPack.createCraftingShaped(ITEM_EXTRACTOR, 1, "A A|CHC|ACA", p("A", ALUMINIUM_INGOT), p("C", Items.COPPER_INGOT), p("H", Items.HOPPER));
        HTDataPack.createCraftingShaped(MAGNET, 1, "I I|R L|RIL", p("I", Items.IRON_INGOT), p("R", Items.REDSTONE_BLOCK), p("L", Items.LAPIS_BLOCK));

        HTDataPack.createCraftingShaped(MEDIUM_BATTERY, 1, "IBI|IRI|IBI", p("I", Items.IRON_BLOCK), p("R", ENERGY_REGULATOR), p("B", SMALL_BATTERY));
        HTDataPack.createCraftingShaped(MOTOR, 1, "RCR|IMI|CIC", p("I", Items.IRON_INGOT), p("R", Items.REDSTONE_BLOCK), p("C", COPPER_WIRE), p("M", MAGNET));

        HTDataPack.createCraftingShaped(PIPE, 2,"AAA|   |AAA", p("A", ALUMINIUM_INGOT));
        HTDataPack.createCraftingShaped(PORTABLE_CRAFTING, 1, "C |S ", p("C", Items.CRAFTING_TABLE), p("S", Items.STICK));
        HTDataPack.createCraftingShaped(PORTABLE_TRASHCAN, 1, "C |S ", p("C", TRASHCAN), p("S", Items.STICK));
        HTDataPack.createCraftingShaped(SMALL_BATTERY, 1, "SBC|SES|CBS", p("S", Items.BLACKSTONE), p("E", ENERGY_REGULATOR), p("C",COPPER_WIRE), p("B", BATTERY));

        HTDataPack.createCraftingShaped(TABLE_SAW, 1, " I |2S2| L ", p("I", Items.IRON_INGOT), p("S", Items.STONE), p("2", Items.SMOOTH_STONE_SLAB), p("L", "minecraft:logs"));

        HTDataPack.createCraftingShaped(TRASHCAN, 1, "ICI| I |   ", p("I", Items.IRON_INGOT), p("C", Items.CACTUS));


        HTDataPack.createSmeltingRecipe(ALUMINIUM_INGOT, RAW_ALUMINIUM, 200, 0.7);
        HTDataPack.createBlastingRecipe(ALUMINIUM_INGOT, RAW_ALUMINIUM, 100, 0.35);

        HTDataPack.createCraftingShaped(ELECTRIC_ORE_WASHER_MK1, 1, "IMI|IFI|IBI", p("F", BASIC_ORE_WASHER), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_ORE_WASHER_MK2, 1, "III|MFM|IBI", p("F", ELECTRIC_ORE_WASHER_MK1), p("I", Items.IRON_INGOT), p("M", MOTOR), p("B", BATTERY));
        HTDataPack.createCraftingShaped(ELECTRIC_ORE_WASHER_MK3, 1, "III|MFM|IBI", p("F", ELECTRIC_ORE_WASHER_MK2), p("I", Items.IRON_BLOCK), p("M", MOTOR), p("B", BATTERY));
    }

    private static Pair<String, Object> p(String left, Object right) {
        return Pair.of(left, right);
    }

    private static void register(String path, Item item) {
        Registry.register(Registry.ITEM, HTUtils.id(path), item);
    }
}
