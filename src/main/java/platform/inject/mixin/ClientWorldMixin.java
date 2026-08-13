package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.BlockChangeEvent;
import aethereal.event.PotionEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientWorld.class})
public class ClientWorldMixin {
    @Inject(method = {"syncWorldEvent(Lnet/minecraft/entity/player/PlayerEntity;ILnet/minecraft/util/math/BlockPos;I)V"}, at = {@At("HEAD")})
    private void onSyncWorldEvent(PlayerEntity player, int eventId, BlockPos pos, int data, CallbackInfo ci) {
        if (eventId == 2002) {
            EventManager.a(new PotionEvent(PotionEvent.a.PARTICLES, data, pos));
        }
    }

    @Inject(method = {"handleBlockUpdate"}, at = {@At("HEAD")})
    private void onHandleBlockUpdate(BlockPos pos, BlockState state, int flags, CallbackInfo ci) {
        ClientWorld world = (ClientWorld) (Object) this;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != state) {
            EventManager.a(new BlockChangeEvent(pos.toImmutable(), oldState, state));
        }
    }
}
