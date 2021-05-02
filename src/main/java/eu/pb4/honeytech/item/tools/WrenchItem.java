package eu.pb4.honeytech.item.tools;

import java.util.Collection;

import eu.pb4.honeytech.block.WrenchableBlock;
import eu.pb4.honeytech.item.AdditionalItemActions;
import eu.pb4.honeytech.item.general.GlowingItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WrenchItem extends GlowingItem implements AdditionalItemActions {

    public WrenchItem(Settings settings) {
        super(settings, Items.IRON_HOE);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();
        if (!world.isClient && playerEntity != null) {
            BlockPos blockPos = context.getBlockPos();
            if (this.use(playerEntity, world.getBlockState(blockPos), world, blockPos,  true, context.getStack())) {
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    public ActionResult preMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (miner instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) miner).networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, state));
            this.use((ServerPlayerEntity) miner, state, world, pos, false, stack);
        }

        return ActionResult.FAIL;
    }

    private boolean use(PlayerEntity player, BlockState state, WorldAccess world, BlockPos pos, boolean update, ItemStack stack) {
        if (state.getBlock() instanceof WrenchableBlock) {
            Block block = state.getBlock();
            StateManager<Block, BlockState> stateManager = block.getStateManager();
            Collection<Property<?>> collection = ((WrenchableBlock) state.getBlock()).getWrenchableProperies();
            String blockId = Registry.BLOCK.getId(block).toString();
            if (collection.isEmpty()) {
                sendMessage(player, new TranslatableText(Items.DEBUG_STICK.getTranslationKey() + ".empty", blockId));
            } else {
                String targetPropertyBlock = stack.getOrCreateTag().getString("TargetPropertyBlock");
                Property<?> property = blockId.equals(targetPropertyBlock) ? stateManager.getProperty(stack.getTag().getString("TargetPropertyState")) : null;
                if (update) {
                    if (property == null) {
                        property = collection.iterator().next();
                    }

                    BlockState blockState = nextBlockState(state, property, player.shouldCancelInteraction());

                    world.setBlockState(pos, blockState, 18);
                    sendMessage(player, new TranslatableText(Items.DEBUG_STICK.getTranslationKey() + ".update", property.getName(), getValueString(blockState, property)));
                    return true;
                } else {
                    property = cycle(collection, property, player.shouldCancelInteraction());
                    String string3 = property.getName();
                    stack.getTag().putString("TargetPropertyBlock", blockId);
                    stack.getTag().putString("TargetPropertyState", string3);

                    sendMessage(player, new TranslatableText(Items.DEBUG_STICK.getTranslationKey() + ".select", string3, getValueString(state, property)));
                }

            }
        }

        return false;
    }

    private static <T extends Comparable<T>> BlockState nextBlockState(BlockState state, Property<T> property, boolean inverse) {
        return state.with(property, cycle(property.getValues(), state.get(property), inverse));
    }

    private static <T> T cycle(Iterable<T> elements, @Nullable T current, boolean inverse) {
        return inverse ? Util.previous(elements, current) : Util.next(elements, current);
    }

    private static void sendMessage(PlayerEntity player, Text message) {
        ((ServerPlayerEntity)player).sendMessage(message, MessageType.GAME_INFO, Util.NIL_UUID);
    }

    private static <T extends Comparable<T>> String getValueString(BlockState state, Property<T> property) {
        return property.name(state.get(property));
    }
}
