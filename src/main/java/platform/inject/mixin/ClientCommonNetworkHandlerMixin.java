package platform.inject.mixin;


import aethereal.core.Delta;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientCommonNetworkHandler.class})
public class ClientCommonNetworkHandlerMixin {
    @Inject(method = {"onResourcePackSend"}, at = {@At("HEAD")}, cancellable = true)
    private void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        if (Delta.getInstance().getModuleProcessor().t().v().m()) {
            ClientCommonNetworkHandler self = (ClientCommonNetworkHandler) (Object) this;
            self.sendPacket(new ResourcePackStatusC2SPacket(packet.id(), ResourcePackStatusC2SPacket.Status.DECLINED));
            ci.cancel();
        }
    }
}
