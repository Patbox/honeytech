package eu.pb4.honeytech.blockentity;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.electric.BatteryBlockEntity;
import eu.pb4.honeytech.blockentity.electric.CableBlockEntity;
import eu.pb4.honeytech.blockentity.basic_machines.EnchancedFurnaceBlockEntity;
import eu.pb4.honeytech.blockentity.basic_machines.TrashCanBlockEntity;
import eu.pb4.honeytech.blockentity.electric.CoalGeneratorBlockEntity;
import eu.pb4.honeytech.blockentity.machines_common.GrinderBlockEntity;
import eu.pb4.honeytech.blockentity.machines_common.OreWasherBlockEntity;
import eu.pb4.honeytech.blockentity.storage.ItemExtractorBlockEntity;
import eu.pb4.honeytech.blockentity.storage.PipeBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class HTBlockEntities {
    public static BlockEntityType<GrinderBlockEntity> GRINDER;
    public static BlockEntityType<OreWasherBlockEntity> ORE_WASHER;
    public static BlockEntityType<PipeBlockEntity> PIPE;
    public static BlockEntityType<ItemExtractorBlockEntity> EXTRACTOR;
    public static BlockEntityType<EnchancedFurnaceBlockEntity> ENCHANCED_FURNACE;
    public static BlockEntityType<TrashCanBlockEntity> TRASHCAN;
    public static BlockEntityType<CableBlockEntity> CABLE;
    public static BlockEntityType<CoalGeneratorBlockEntity> COAL_GENERATOR;
    public static BlockEntityType<BatteryBlockEntity> BATTERY;


    public static void register() {
        GRINDER = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:grinder", BlockEntityType.Builder.create(GrinderBlockEntity::new, HTBlocks.BASIC_GRINDER).build(null));
        ORE_WASHER = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:ore_washer", BlockEntityType.Builder.create(OreWasherBlockEntity::new, HTBlocks.BASIC_ORE_WASHER).build(null));
        PIPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:pipe", BlockEntityType.Builder.create(PipeBlockEntity::new, HTBlocks.PIPE).build(null));
        EXTRACTOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:item_extractor", BlockEntityType.Builder.create(ItemExtractorBlockEntity::new, HTBlocks.ITEM_EXTRACTOR).build(null));
        ENCHANCED_FURNACE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:enchanced_furnace", BlockEntityType.Builder.create(EnchancedFurnaceBlockEntity::new, HTBlocks.ENCHANCED_FURNACE).build(null));
        TRASHCAN = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:trash_can", BlockEntityType.Builder.create(TrashCanBlockEntity::new, HTBlocks.TRASHCAN).build(null));
        CABLE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:cable", BlockEntityType.Builder.create(CableBlockEntity::new, HTBlocks.CABLE).build(null));
        COAL_GENERATOR = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:coal_generator", BlockEntityType.Builder.create(CoalGeneratorBlockEntity::new, HTBlocks.COAL_GENERATOR_MK1, HTBlocks.COAL_GENERATOR_MK2, HTBlocks.COAL_GENERATOR_MK3).build(null));
        BATTERY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "honeytech:battery", BlockEntityType.Builder.create(BatteryBlockEntity::new, HTBlocks.SMALL_BATTERY, HTBlocks.MEDIUM_BATTERY, HTBlocks.BIG_BATTERY).build(null));
    }
}
