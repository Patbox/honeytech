package eu.pb4.honeytech.block.basic_machines;

import eu.pb4.honeytech.other.ImplementedInventory;
import eu.pb4.honeytech.recipe_types.TableSawRecipe;
import eu.pb4.polymer.block.VirtualBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TableSawBlock extends Block implements VirtualBlock {
    public static final EnumProperty<Parts> PART = EnumProperty.of("part", Parts.class);

    public TableSawBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PART, Parts.CENTER_NS));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(PART);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        Identifier identifier = this.getLootTableId();
        if (identifier == LootTables.EMPTY || !(state.get(PART) == Parts.CENTER_EW || state.get(PART) == Parts.CENTER_NS)) {
            return Collections.emptyList();
        } else {
            LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
            ServerWorld serverWorld = lootContext.getWorld();
            LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
            return lootTable.generateLoot(lootContext);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.FAIL;

        Optional<TableSawRecipe> match = world.getRecipeManager()
                .getFirstMatch(TableSawRecipe.Type.INSTANCE, ImplementedInventory.of(DefaultedList.copyOf(ItemStack.EMPTY, player.getStackInHand(hand))), world);

        if (match.isPresent()) {
            TableSawRecipe recipe = match.get();
            ItemStack copy = player.getStackInHand(hand).copy();
            player.getStackInHand(hand).decrement(1);
            player.getInventory().offerOrDrop(recipe.getOutput().copy());
            SoundEvent event = SoundEvents.BLOCK_WOOD_BREAK;
            if (copy.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) copy.getItem()).getBlock();
                event = block.getSoundGroup(block.getDefaultState()).getBreakSound();
            }

            Parts part = state.get(PART);

            world.playSound(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, event, SoundCategory.BLOCKS, 0.6f, 1f);
            ((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, copy),
                    pos.getX() + 0.5d + part.xOffset,
                    pos.getY() + 1d,
                    pos.getZ() + 0.5d + part.zOffset, 20, 0.1f, 0.1f, 0.1f, 0.1f);

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (state.getBlock() != newState.getBlock()) {
            switch (state.get(PART)) {
                case CENTER_NS:
                    world.setBlockState(pos.north(), Blocks.AIR.getDefaultState());
                    world.setBlockState(pos.south(), Blocks.AIR.getDefaultState());
                    break;
                case CENTER_EW:
                    world.setBlockState(pos.east(), Blocks.AIR.getDefaultState());
                    world.setBlockState(pos.west(), Blocks.AIR.getDefaultState());
                    break;
                case WEST:
                    world.breakBlock(pos.east(), true);
                    break;
                case EAST:
                    world.breakBlock(pos.west(), true);
                    break;
                case NORTH:
                    world.breakBlock(pos.south(), true);
                    break;
                case SOUTH:
                    world.breakBlock(pos.north(), true);
                    break;
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        boolean can;
        boolean eastOrWest = false;

        Direction d = Direction.fromRotation(ctx.getPlayerYaw());

        if (d == Direction.EAST || d == Direction.WEST) {
            eastOrWest = true;
        }

        if (eastOrWest) {
            can = ctx.getWorld().getBlockState(blockPos.east()).canReplace(ctx) && ctx.getWorld().getBlockState(blockPos.west()).canReplace(ctx);
        } else {
            can = ctx.getWorld().getBlockState(blockPos.north()).canReplace(ctx) && ctx.getWorld().getBlockState(blockPos.south()).canReplace(ctx);
        }

        return blockPos.getY() < ctx.getWorld().getHeight() && can ? eastOrWest ? this.getDefaultState().with(PART, Parts.CENTER_EW) : this.getDefaultState().with(PART, Parts.CENTER_NS) : null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        boolean eastOrWest = false;

        Direction d = Direction.fromRotation(placer.getYaw());

        if (d == Direction.EAST || d == Direction.WEST) {
            eastOrWest = true;
        }


        if (eastOrWest) {
            placer.world.setBlockState(pos.east(), this.getDefaultState().with(PART, Parts.EAST), 3);
            placer.world.setBlockState(pos.west(), this.getDefaultState().with(PART, Parts.WEST), 3);
        } else {
            placer.world.setBlockState(pos.north(), this.getDefaultState().with(PART, Parts.NORTH), 3);
            placer.world.setBlockState(pos.south(), this.getDefaultState().with(PART, Parts.SOUTH), 3);
        }
    }

    @Override
    public Block getVirtualBlock() {
        return Blocks.STONECUTTER;
    }

    @Override
    public BlockState getDefaultVirtualBlockState() {
        return Blocks.STONECUTTER.getDefaultState();
    }

    @Override
    public BlockState getVirtualBlockState(BlockState state) {
        if (state.get(PART) == Parts.CENTER_EW) {
            return Blocks.STONECUTTER.getDefaultState().with(StonecutterBlock.FACING, Direction.NORTH);
        } else if (state.get(PART) == Parts.CENTER_NS) {
            return Blocks.STONECUTTER.getDefaultState().with(StonecutterBlock.FACING, Direction.EAST);
        }

        return Blocks.SMOOTH_STONE_SLAB.getDefaultState();
    }


    enum Parts implements StringIdentifiable {
        CENTER_EW("center_ew", 0, 0),
        CENTER_NS("center_ns", 0, 0),
        WEST("west", 1, 0),
        EAST("east", -1, 0),
        NORTH("north", 0, 1),
        SOUTH("south", 0, -1);

        public final int xOffset;
        public final int zOffset;
        private final String s;

        Parts(String s, int x, int z) {
            this.s = s;
            this.xOffset = x;
            this.zOffset = z;
        }

        @Override
        public String asString() {
            return this.s;
        }
    }
}
