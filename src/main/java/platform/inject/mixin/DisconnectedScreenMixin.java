package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DisconnectedScreen.class})
public abstract class DisconnectedScreenMixin extends Screen {

    @Unique
    private ButtonWidget buttonWidget;

    private DisconnectedScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = {"init"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/DisconnectedScreen;refreshWidgetPositions()V", shift = At.Shift.BEFORE)})
    private void init(CallbackInfo ci) {
        ServerInfo server = Delta.h().d().v().h().a();
        if (server != null) {
            this.buttonWidget = addDrawableChild(ButtonWidget.builder(Text.literal("Переподключиться"), btn -> {
                ConnectScreen.connect(new MultiplayerScreen(null), Interface.mc, ServerAddress.parse(server.address), server, false, null);
            }).dimensions(0, 0, InterfaceC0020Opcode.aN, 20).build());
        }
    }

    @Inject(method = {"refreshWidgetPositions"}, at = {@At("TAIL")})
    private void refreshWidgetPositions(CallbackInfo ci) {
        if (this.buttonWidget != null) {
            int x = (this.width / 2) - 100;
            int maxY = children().stream().filter(child -> {
                return (child instanceof ButtonWidget) && child != this.buttonWidget;
            }).map(child2 -> {
                return Integer.valueOf(((ButtonWidget) child2).getY());
            }).max((v0, v1) -> {
                return v0.compareTo(v1);
            }).orElse(Integer.valueOf(this.height / 2)).intValue();
            this.buttonWidget.setPosition(x, maxY + 24);
        }
    }
}
