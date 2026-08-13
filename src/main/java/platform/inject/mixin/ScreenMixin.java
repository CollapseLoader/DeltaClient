package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.event.TooltipEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({Screen.class})
public class ScreenMixin {
    @Inject(method = {"getTooltipFromItem"}, at = {@At("RETURN")})
    private static void getTooltipFromItem(MinecraftClient client, ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        EventManager.a(new TooltipEvent(stack, cir.getReturnValue()));
    }

    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        if (((self instanceof ReconfiguringScreen) || (self instanceof DownloadingTerrainScreen)) && Delta.h().d().t().aN().m()) {
            if (self instanceof DownloadingTerrainScreen) {
                Interface.aM_.setScreen(null);
            }
            ci.cancel();
        }
    }
}
