package eu.pb4.honeytech.block;

import eu.pb4.honeytech.block.basic_machines.*;
import eu.pb4.honeytech.block.electric.*;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.block.machines_common.HandleBlock;
import eu.pb4.honeytech.block.storage.ItemExtractorBlock;
import eu.pb4.honeytech.block.storage.PipeBlock;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public class HTBlocks {
    public static GrinderBlock BASIC_GRINDER = new BasicGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5));
    public static HandleBlock BASIC_GRINDER_HANDLE = new HandleBlock(AbstractBlock.Settings.of(Material.WOOD).strength(5).dropsNothing(), Blocks.OAK_FENCE);
    public static TableSawBlock TABLE_SAW = new TableSawBlock(AbstractBlock.Settings.of(Material.METAL).strength(4));

    public static BasicOreWasherBlock BASIC_ORE_WASHER = new BasicOreWasherBlock(AbstractBlock.Settings.of(Material.METAL).strength(4));
    public static HandleBlock BASIC_ORE_WASHER_HANDLE = new BasicOreWasherHandleBlock(AbstractBlock.Settings.of(Material.WOOD).strength(5).dropsNothing());

    public static PipeBlock PIPE = new PipeBlock(AbstractBlock.Settings.of(Material.METAL).strength(3));
    public static ItemExtractorBlock ITEM_EXTRACTOR = new ItemExtractorBlock(AbstractBlock.Settings.of(Material.METAL).strength(3));
    public static EnchancedFurnaceBlock ENCHANCED_FURNACE = new EnchancedFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(3), 1.5f, 2);
    public static TrashCanBlock TRASHCAN = new TrashCanBlock(AbstractBlock.Settings.of(Material.METAL).strength(4));

    public static CableBlock CABLE = new CableBlock(AbstractBlock.Settings.of(Material.METAL).strength(4));
    public static CoalGeneratorBlock COAL_GENERATOR_MK1 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), 1);
    public static CoalGeneratorBlock COAL_GENERATOR_MK2 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), 3);
    public static CoalGeneratorBlock COAL_GENERATOR_MK3 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), 6);

    public static BatteryBlock SMALL_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), 65536, 1);
    public static BatteryBlock MEDIUM_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), 262144, 2);
    public static BatteryBlock BIG_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), 1048576, 3);

    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK1 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 2);
    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK2 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 2);
    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK3 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 2);

    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK1 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 1);
    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK2 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 2);
    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK3 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), 5);

    public static void register() {
        Registry.register(Registry.BLOCK, HTUtils.id("basic_grinder"), BASIC_GRINDER);
        Registry.register(Registry.BLOCK, HTUtils.id("basic_grinder_handle"), BASIC_GRINDER_HANDLE);
        Registry.register(Registry.BLOCK, HTUtils.id("table_saw"), TABLE_SAW);
        Registry.register(Registry.BLOCK, HTUtils.id("basic_ore_washer"), BASIC_ORE_WASHER);
        Registry.register(Registry.BLOCK, HTUtils.id("basic_ore_washer_handle"), BASIC_ORE_WASHER_HANDLE);

        Registry.register(Registry.BLOCK, HTUtils.id("pipe"), PIPE);
        Registry.register(Registry.BLOCK, HTUtils.id("item_extractor"), ITEM_EXTRACTOR);

        Registry.register(Registry.BLOCK, HTUtils.id("enchanced_furnace"), ENCHANCED_FURNACE);

        Registry.register(Registry.BLOCK, HTUtils.id("trashcan"), TRASHCAN);

        Registry.register(Registry.BLOCK, HTUtils.id("cable"), CABLE);
        Registry.register(Registry.BLOCK, HTUtils.id("coal_generator_mk1"), COAL_GENERATOR_MK1);
        Registry.register(Registry.BLOCK, HTUtils.id("coal_generator_mk2"), COAL_GENERATOR_MK2);
        Registry.register(Registry.BLOCK, HTUtils.id("coal_generator_mk3"), COAL_GENERATOR_MK3);

        Registry.register(Registry.BLOCK, HTUtils.id("small_battery"), SMALL_BATTERY);
        Registry.register(Registry.BLOCK, HTUtils.id("medium_battery"), MEDIUM_BATTERY);
        Registry.register(Registry.BLOCK, HTUtils.id("big_battery"), BIG_BATTERY);

        Registry.register(Registry.BLOCK, HTUtils.id("electric_grinder_mk1"), ELECTRIC_GRINDER_MK1);
        Registry.register(Registry.BLOCK, HTUtils.id("electric_grinder_mk2"), ELECTRIC_GRINDER_MK2);
        Registry.register(Registry.BLOCK, HTUtils.id("electric_grinder_mk3"), ELECTRIC_GRINDER_MK3);


        Registry.register(Registry.BLOCK, HTUtils.id("electric_furnace_mk1"), ELECTRIC_FURNACE_MK1);
        Registry.register(Registry.BLOCK, HTUtils.id("electric_furnace_mk2"), ELECTRIC_FURNACE_MK2);
        Registry.register(Registry.BLOCK, HTUtils.id("electric_furnace_mk3"), ELECTRIC_FURNACE_MK3);

    }
}
