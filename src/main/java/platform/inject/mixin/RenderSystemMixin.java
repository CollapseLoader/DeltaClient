package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.AmbienceEvent;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderSystem.class})
public class RenderSystemMixin {
    @Inject(method = {"clearColor"}, at = {@At("HEAD")}, cancellable = true)
    private static void onClearColor(float red, float green, float blue, float alpha, CallbackInfo ci) {
        AmbienceEvent.a event = new AmbienceEvent.a(red, green, blue, alpha);
        EventManager.a(event);
        if (event.a()) {
            GlStateManager._clearColor(event.getRed(), event.getGreen(), event.getBlue(), event.getAlpha());
            ci.cancel();
        }
    }
}
