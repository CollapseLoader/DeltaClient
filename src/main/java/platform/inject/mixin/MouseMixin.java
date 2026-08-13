package platform.inject.mixin;


import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.KeyEvent;
import aethereal.event.LookEvent;
import aethereal.event.ScrollEvent;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.SwapScreen;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Mouse.class})
public class MouseMixin {
    @Inject(method = {"onMouseButton"}, at = {@At("HEAD")}, cancellable = true)
    public void onMouseButton(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (Interface.aM_.currentScreen == null || (Interface.aM_.currentScreen instanceof SwapScreen) || (Interface.aM_.currentScreen instanceof AssistantScreen)) {
            EventManager.a(new KeyEvent((button < 0 || button > 7) ? button : (-100) + button, 0, action, modifiers));
        }
        if (action == 1) {
            ClickEvent event = new ClickEvent(Interface.aM_.mouse.getX() / 2.0d, Interface.aM_.mouse.getY() / 2.0d, button, ClickEvent.a.PRESS);
            EventManager.a(event);
            if (event.a()) {
                ci.cancel();
                return;
            }
            return;
        }
        if (action == 0) {
            ClickEvent event2 = new ClickEvent(Interface.aM_.mouse.getX() / 2.0d, Interface.aM_.mouse.getY() / 2.0d, button, ClickEvent.a.RELEASE);
            EventManager.a(event2);
            if (event2.a()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = {"onCursorPos"}, at = {@At("HEAD")}, cancellable = true)
    public void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        ClickEvent event = new ClickEvent(Interface.aM_.mouse.getX() / 2.0d, Interface.aM_.mouse.getY() / 2.0d, 0, ClickEvent.a.DRAG);
        EventManager.a(event);
        if (event.a()) {
            ci.cancel();
        }
    }

    @Inject(method = {"onMouseScroll"}, at = {@At("RETURN")})
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        EventManager.a(new ScrollEvent(horizontal, vertical));
    }

    @Redirect(method = {"updateMouse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void redirectChangeLookDirection(ClientPlayerEntity player, double yaw, double pitch) {
        LookEvent event = new LookEvent((float) yaw, (float) pitch);
        EventManager.a(event);
        if (!event.a()) {
            player.changeLookDirection(yaw, pitch);
        }
    }
}
