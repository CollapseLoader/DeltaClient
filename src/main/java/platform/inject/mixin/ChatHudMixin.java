package platform.inject.mixin;


import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.Animations;
import aethereal.render.EasingList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin({ChatHud.class})
public abstract class ChatHudMixin {

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Shadow
    private void refresh() {
    }

    @ModifyArgs(method = {"render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), require = 0)
    private void onRenderLineBackground(Args args, @Local ChatHudLine.Visible line) {
        if (!Delta.h().d().t().as().m() || !Delta.h().d().t().as().q().c().booleanValue() || (((Integer) args.get(4)).intValue() & 16777215) != 0) {
            return;
        }
        args.set(2, Integer.valueOf(((Integer) args.get(0)).intValue() + Interface.mc.textRenderer.getWidth(line.content()) + 5));
    }

    @WrapOperation(method = {"render(Lnet/minecraft/client/gui/DrawContext;IIIZ)V"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I")})
    private int onRenderLineText(DrawContext context, TextRenderer renderer, OrderedText text, int x, int y, int color, Operation<Integer> original, @Local ChatHudLine.Visible line, @Local(argsOnly = true, ordinal = 0) int currentTick) {
        Animations animations = Delta.h().d().t().Q();
        if (!animations.m() || !animations.q().a("Появление сообщений").c().booleanValue()) {
            return original.call(context, renderer, text, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(color)).intValue();
        }
        double t = Math.max(0.0d, Math.min(1.0d, ((double) ((currentTick - line.addedTime()) + Interface.mc.getRenderTickCounter().getTickDelta(false))) / 9.0d));
        double progress = EasingList.p.ease((float) t);
        int alpha = (int) Math.round(((double) ((color >>> 24) & 255)) * progress);
        context.getMatrices().push();
        context.getMatrices().translate((float) ((-(1.0d - progress)) * 8.0d), 0.0f, 0.0f);
        int result = original.call(context, renderer, text, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf((color & 16777215) | (alpha << 24))).intValue();
        context.getMatrices().pop();
        return result;
    }

    @Inject(method = {"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at = {@At("TAIL")})
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (this.messages.size() < 2) {
            return;
        }
        ChatHudLine current = this.messages.get(0);
        ChatHudLine previous = this.messages.get(1);
        String currentText = current.content().getString();
        String previousText = previous.content().getString();
        int counterIndex = previousText.lastIndexOf(" [x");
        if (counterIndex != -1 && previousText.endsWith("]")) {
            String originalText = previousText.substring(0, counterIndex);
            if (originalText.equals(currentText)) {
                updateMessage(current, message, Integer.parseInt(previousText.substring(counterIndex + 3, previousText.length() - 1)) + 1);
                this.messages.remove(1);
                refresh();
                return;
            }
            return;
        }
        if (previousText.equals(currentText)) {
            updateMessage(current, message, 2);
            this.messages.remove(1);
            refresh();
        }
    }

    @Unique
    private void updateMessage(ChatHudLine lastMessage, Text message, int count) {
        ChatHudLine updatedLine = new ChatHudLine(lastMessage.creationTick(), Text.empty().append(message.copy()).append(Text.literal(" [x" + count + "]").formatted(Formatting.GRAY)), lastMessage.signature(), lastMessage.indicator());
        this.messages.set(0, updatedLine);
    }
}
