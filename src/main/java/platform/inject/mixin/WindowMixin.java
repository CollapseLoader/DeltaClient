package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.ResizeEvent;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Window.class})
public class WindowMixin {
    @Inject(method = {"setIcon"}, at = {@At("TAIL")})
    private void onSetIcon(CallbackInfo ci) {
        EventManager.a(new ResizeEvent());
    }

    @Inject(method = {"onFramebufferSizeChanged"}, at = {@At("TAIL")})
    private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        EventManager.a(new ResizeEvent());
    }
}
