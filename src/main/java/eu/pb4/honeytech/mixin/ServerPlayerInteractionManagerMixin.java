package eu.pb4.honeytech.mixin;

import eu.pb4.honeytech.item.AdditionalItemActions;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 300)
public class ServerPlayerInteractionManagerMixin {

    @Shadow public ServerWorld world;

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"), cancellable = true)
    private void processAdditionalEvents(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, CallbackInfo ci) {
        if (action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK && this.world.canPlayerModifyAt(this.player, pos)) {
            if (this.player.getMainHandStack().getItem() instanceof AdditionalItemActions) {
                ActionResult result = ((AdditionalItemActions) this.player.getMainHandStack().getItem()).preMine(this.player.getMainHandStack(), this.world, world.getBlockState(pos), pos, this.player);

                if (result != ActionResult.PASS) {
                    ci.cancel();
                }
            }
        }
    }
}
