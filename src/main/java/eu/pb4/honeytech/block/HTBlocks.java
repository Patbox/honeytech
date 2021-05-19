package eu.pb4.honeytech.block;

import eu.pb4.honeytech.block.basic_machines.*;
import eu.pb4.honeytech.block.electric.*;
import eu.pb4.honeytech.block.machines_common.GrinderBlock;
import eu.pb4.honeytech.block.machines_common.HandleBlock;
import eu.pb4.honeytech.block.storage.ItemExtractorBlock;
import eu.pb4.honeytech.block.storage.PipeBlock;
import eu.pb4.honeytech.other.HTDataPack;
import eu.pb4.honeytech.other.HTTier;
import eu.pb4.honeytech.other.HTUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
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
    public static CoalGeneratorBlock COAL_GENERATOR_MK1 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), HTTier.COPPER);
    public static CoalGeneratorBlock COAL_GENERATOR_MK2 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), HTTier.IRON);
    public static CoalGeneratorBlock COAL_GENERATOR_MK3 = new CoalGeneratorBlock(AbstractBlock.Settings.of(Material.METAL).strength(4).luminance(CoalGeneratorBlock::getLightLevel), HTTier.GOLD);

    public static BatteryBlock SMALL_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), HTTier.COPPER);
    public static BatteryBlock MEDIUM_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), HTTier.IRON);
    public static BatteryBlock BIG_BATTERY = new BatteryBlock(AbstractBlock.Settings.of(Material.METAL).strength(4), HTTier.GOLD);

    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK1 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.COPPER);
    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK2 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.IRON);
    public static ElectricGrinderBlock ELECTRIC_GRINDER_MK3 = new ElectricGrinderBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.GOLD);

    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK1 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.COPPER);
    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK2 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.IRON);
    public static ElectricFurnaceBlock ELECTRIC_FURNACE_MK3 = new ElectricFurnaceBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.GOLD);

    public static ElectricOreWasherBlock ELECTRIC_ORE_WASHER_MK1 = new ElectricOreWasherBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.COPPER);
    public static ElectricOreWasherBlock ELECTRIC_ORE_WASHER_MK2 = new ElectricOreWasherBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.IRON);
    public static ElectricOreWasherBlock ELECTRIC_ORE_WASHER_MK3 = new ElectricOreWasherBlock(AbstractBlock.Settings.of(Material.METAL).strength(5), HTTier.GOLD);

    public static AutoCrafterBlock AUTO_CRAFTER = new AutoCrafterBlock(AbstractBlock.Settings.of(Material.METAL).strength(5));

    public static void createBlocks() {
        register("basic_grinder", BASIC_GRINDER);
        register("basic_grinder_handle", BASIC_GRINDER_HANDLE);
        register("table_saw", TABLE_SAW);
        register("basic_ore_washer", BASIC_ORE_WASHER);
        register("basic_ore_washer_handle", BASIC_ORE_WASHER_HANDLE);

        register("pipe", PIPE);
        register("item_extractor", ITEM_EXTRACTOR);

        register("enchanced_furnace", ENCHANCED_FURNACE);

        register("trashcan", TRASHCAN);

        register("cable", CABLE);
        register("coal_generator_mk1", COAL_GENERATOR_MK1);
        register("coal_generator_mk2", COAL_GENERATOR_MK2);
        register("coal_generator_mk3", COAL_GENERATOR_MK3);

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

        register("auto_crafter", AUTO_CRAFTER);

    }

    public static void createRecipes() {
        HTDataPack.createSimpleDrop(BASIC_GRINDER);
        HTDataPack.createSimpleDrop(BASIC_ORE_WASHER);
        HTDataPack.createSimpleDrop(TABLE_SAW);
        HTDataPack.createSimpleDrop(PIPE);
        HTDataPack.createSimpleDrop(ITEM_EXTRACTOR);
        HTDataPack.createSimpleDrop(ENCHANCED_FURNACE);
        HTDataPack.createSimpleDrop(TRASHCAN);
        HTDataPack.createSimpleDrop(CABLE);
        HTDataPack.createSimpleDrop(COAL_GENERATOR_MK1);
        HTDataPack.createSimpleDrop(COAL_GENERATOR_MK2);
        HTDataPack.createSimpleDrop(COAL_GENERATOR_MK3);
        HTDataPack.createSimpleDrop(SMALL_BATTERY);
        HTDataPack.createSimpleDrop(MEDIUM_BATTERY);
        HTDataPack.createSimpleDrop(BIG_BATTERY);
        HTDataPack.createSimpleDrop(ELECTRIC_GRINDER_MK1);
        HTDataPack.createSimpleDrop(ELECTRIC_GRINDER_MK2);
        HTDataPack.createSimpleDrop(ELECTRIC_GRINDER_MK3);
        HTDataPack.createSimpleDrop(ELECTRIC_FURNACE_MK1);
        HTDataPack.createSimpleDrop(ELECTRIC_FURNACE_MK2);
        HTDataPack.createSimpleDrop(ELECTRIC_FURNACE_MK3);
        HTDataPack.createSimpleDrop(ELECTRIC_ORE_WASHER_MK1);
        HTDataPack.createSimpleDrop(ELECTRIC_ORE_WASHER_MK2);
        HTDataPack.createSimpleDrop(ELECTRIC_ORE_WASHER_MK3);
        HTDataPack.createSimpleDrop(AUTO_CRAFTER);
    }


    private static void register(String path, Block block) {
        Registry.register(Registry.BLOCK, HTUtils.id(path), block);
    }
}
