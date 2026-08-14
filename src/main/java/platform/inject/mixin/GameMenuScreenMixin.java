package platform.inject.mixin;


import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.util.ServerUtil;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import platform.inject.accessors.ButtonWidgetAccessor;

@Mixin({GameMenuScreen.class})
public abstract class GameMenuScreenMixin extends Screen {

    @Unique
    private boolean reconnect;

    @Unique
    private boolean disconnect;

    private GameMenuScreenMixin(Text title) {
        super(title);
        this.reconnect = false;
        this.disconnect = false;
    }

    @Inject(method = {"init"}, at = {@At("TAIL")})
    private void init(CallbackInfo ci) {
        ServerInfo server = Interface.mc.getCurrentServerEntry();
        children().stream().filter(child -> {
            return child instanceof ButtonWidget;
        }).map(child2 -> {
            return (ButtonWidget) child2;
        }).filter(button -> {
            TranslatableTextContent class_2588VarMethod_10851 = (TranslatableTextContent) button.getMessage().getContent();
            if (class_2588VarMethod_10851 instanceof TranslatableTextContent) {
                TranslatableTextContent content = class_2588VarMethod_10851;
                return content.getKey().equals("menu.disconnect");
            }
            return false;
        }).findFirst().ifPresent(button2 -> {
            ButtonWidgetAccessor accessor = (ButtonWidgetAccessor) button2;
            ButtonWidget.PressAction original = accessor.getOnPress();
            accessor.setOnPress(widget -> {
                if (ServerUtil.e() && !this.disconnect) {
                    widget.setMessage(button2.getMessage().copy().setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    this.disconnect = true;
                } else {
                    original.onPress(widget);
                }
            });
        });
        if (server != null) {
            int maxY = children().stream().filter(child3 -> {
                return child3 instanceof ButtonWidget;
            }).map(child4 -> {
                return Integer.valueOf(((ButtonWidget) child4).getY());
            }).max((v0, v1) -> {
                return v0.compareTo(v1);
            }).orElse(Integer.valueOf(this.height / 2)).intValue();
            addDrawableChild(ButtonWidget.builder(Text.literal("Переподключиться"), btn -> {
                if (ServerUtil.e() && !this.reconnect) {
                    btn.setMessage(Text.literal("Переподключиться").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    this.reconnect = true;
                } else {
                    try {
                        Interface.mc.world.disconnect();
                        ConnectScreen.connect(new TitleScreen(), Interface.mc, ServerAddress.parse(server.address), server, false, null);
                    } catch (Exception e) {
                    }
                }
            }).dimensions((this.width / 2) - 100, maxY + 24, InterfaceC0020Opcode.aN, 20).build());
        }
    }
}
