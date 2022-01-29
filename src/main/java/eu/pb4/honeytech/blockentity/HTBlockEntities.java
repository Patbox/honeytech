package eu.pb4.honeytech.blockentity;

import eu.pb4.honeytech.block.HTBlocks;
import eu.pb4.honeytech.blockentity.basic_machines.EnchancedFurnaceBlockEntity;
import eu.pb4.honeytech.blockentity.basic_machines.TrashCanBlockEntity;
import eu.pb4.honeytech.blockentity.electric.*;
import eu.pb4.honeytech.blockentity.machines_common.GrinderBlockEntity;
import eu.pb4.honeytech.blockentity.machines_common.OreWasherBlockEntity;
import eu.pb4.honeytech.blockentity.storage.ItemExtractorBlockEntity;
import eu.pb4.honeytech.blockentity.storage.PipeBlockEntity;
import eu.pb4.honeytech.mixin.BlockEntityTypeAccessor;
import eu.pb4.honeytech.other.HTUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import team.reborn.energy.api.EnergyStorage;

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
    public static BlockEntityType<ElectricGrinderBlockEntity> ELECTRIC_GRINDER;
    public static BlockEntityType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE;
    public static BlockEntityType<ElectricOreWasherBlockEntity> ELECTRIC_ORE_WASHER;
    public static BlockEntityType<AutoCrafterBlockEntity> AUTO_CRAFTER;


    public static void register() {
        GRINDER = register("grinder", FabricBlockEntityTypeBuilder.create(GrinderBlockEntity::new, HTBlocks.BASIC_GRINDER).build(null));
        ORE_WASHER = register("ore_washer", FabricBlockEntityTypeBuilder.create(OreWasherBlockEntity::new, HTBlocks.BASIC_ORE_WASHER).build(null));
        PIPE = register("pipe", FabricBlockEntityTypeBuilder.create(PipeBlockEntity::new, HTBlocks.PIPE).build(null));
        EXTRACTOR = register("item_extractor", FabricBlockEntityTypeBuilder.create(ItemExtractorBlockEntity::new, HTBlocks.ITEM_EXTRACTOR).build(null));
        ENCHANCED_FURNACE = register("enchanced_furnace", FabricBlockEntityTypeBuilder.create(EnchancedFurnaceBlockEntity::new, HTBlocks.ENCHANCED_FURNACE).build(null));
        TRASHCAN = register("trash_can", FabricBlockEntityTypeBuilder.create(TrashCanBlockEntity::new, HTBlocks.TRASHCAN).build(null));
        CABLE = register("cable", FabricBlockEntityTypeBuilder.create(CableBlockEntity::new, HTBlocks.CABLE).build(null));
        COAL_GENERATOR = register("coal_generator", FabricBlockEntityTypeBuilder.create(CoalGeneratorBlockEntity::new, HTBlocks.COAL_GENERATOR_MK1, HTBlocks.COAL_GENERATOR_MK2, HTBlocks.COAL_GENERATOR_MK3).build(null));
        BATTERY = register("battery", FabricBlockEntityTypeBuilder.create(BatteryBlockEntity::new, HTBlocks.SMALL_BATTERY, HTBlocks.MEDIUM_BATTERY, HTBlocks.BIG_BATTERY).build(null));

        ELECTRIC_GRINDER = register("electric_grinder", FabricBlockEntityTypeBuilder.create(ElectricGrinderBlockEntity::new, HTBlocks.ELECTRIC_GRINDER_MK1, HTBlocks.ELECTRIC_GRINDER_MK2, HTBlocks.ELECTRIC_GRINDER_MK3).build(null));
        ELECTRIC_FURNACE = register("electric_furnace", FabricBlockEntityTypeBuilder.create(ElectricFurnaceBlockEntity::new, HTBlocks.ELECTRIC_FURNACE_MK1, HTBlocks.ELECTRIC_FURNACE_MK2, HTBlocks.ELECTRIC_FURNACE_MK3).build(null));
        ELECTRIC_ORE_WASHER = register("electric_ore_washer", FabricBlockEntityTypeBuilder.create(ElectricOreWasherBlockEntity::new, HTBlocks.ELECTRIC_ORE_WASHER_MK1, HTBlocks.ELECTRIC_ORE_WASHER_MK2, HTBlocks.ELECTRIC_ORE_WASHER_MK3).build(null));
        AUTO_CRAFTER = register("auto_crafter", FabricBlockEntityTypeBuilder.create(AutoCrafterBlockEntity::new, HTBlocks.AUTO_CRAFTER).build(null));
    }

    public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType<T> type) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, HTUtils.id(id), type);

        if (type.instantiate(BlockPos.ORIGIN, ((Block) ((BlockEntityTypeAccessor) type).getBlocks().toArray()[0]).getDefaultState()) instanceof EnergyHolder) {
            EnergyStorage.SIDED.registerForBlockEntity((myBlockEntity, direction) -> ((EnergyHolder) myBlockEntity).getEnergy(), type);
        }

        return type;
    }
}
