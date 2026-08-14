package platform.inject.mixin;


import aethereal.core.Interface;
import aethereal.ui.screen.MainScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public abstract class TitleScreenMixin {
    @Inject(method = {"init"}, at = {@At("HEAD")}, cancellable = true)
    private void init(CallbackInfo ci) {
        if (!(Interface.mc.currentScreen instanceof MainScreen)) {
            Interface.mc.setScreen(new MainScreen());
            ci.cancel();
        }
    }
}
