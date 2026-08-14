package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.event.RemovalsEvent;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameOverlayRenderer.class})
public class InGameOverlayRendererMixin {
    @Inject(method = {"renderFireOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private static void renderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.type.FIRE);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Inject(method = {"renderInWallOverlay"}, at = {@At("HEAD")}, cancellable = true)
    private static void renderInWallOverlay(Sprite sprite, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        RemovalsEvent event = new RemovalsEvent(RemovalsEvent.type.CLIP);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }
}
