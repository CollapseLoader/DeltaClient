package platform.inject.mixin;


import aethereal.command.CommandProcessor;
import aethereal.core.Delta;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin({ChatInputSuggestor.class})
public abstract class ChatInputSuggestorMixin {

    @Shadow
    @Final
    TextFieldWidget textField;

    @Shadow
    boolean completingSuggestions;

    @Shadow
    private ParseResults<CommandSource> parse;

    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow
    private ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    protected abstract void showCommandSuggestions();

    @Inject(method = {"refresh"}, at = {@At("HEAD")}, cancellable = true)
    private void onRefresh(CallbackInfo ci) {
        try {
            String text = this.textField.getText();
            CommandProcessor commandProcessor = Delta.h().d().u();
            String prefix = commandProcessor.i();
            if (text.startsWith(prefix)) {
                int cursor = this.textField.getCursor();
                StringReader reading = new StringReader(text);
                reading.setCursor(prefix.length());
                this.parse = commandProcessor.a().parse(reading, commandProcessor.h());
                if (cursor >= prefix.length() && (this.window == null || !this.completingSuggestions)) {
                    this.pendingSuggestions = commandProcessor.a().getCompletionSuggestions(this.parse, cursor);
                    this.pendingSuggestions.thenRun(() -> {
                        if (this.pendingSuggestions.isDone()) {
                            showCommandSuggestions();
                        }
                    });
                }
                ci.cancel();
            }
        } catch (Throwable th) {
        }
    }
}
