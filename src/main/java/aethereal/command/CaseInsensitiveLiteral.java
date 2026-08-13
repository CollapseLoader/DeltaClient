package aethereal.command;


import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CaseInsensitiveLiteral extends LiteralCommandNode<CommandSource> {
    public CaseInsensitiveLiteral(LiteralCommandNode<CommandSource> node) {
        super(node.getLiteral(), node.getCommand(), node.getRequirement(), node.getRedirect(), node.getRedirectModifier(), node.isFork());
        node.getChildren().forEach(this::addChild);
    }

    static Collection<? extends CommandNode<CommandSource>> a(CommandNode<CommandSource> parent, StringReader input) {
        int start = input.getCursor();
        while (input.canRead() && input.peek() != ' ') {
            input.skip();
        }
        String word = input.getString().substring(start, input.getCursor());
        input.setCursor(start);
        for (CommandNode<CommandSource> child : parent.getChildren()) {
            if ((child instanceof LiteralCommandNode) && child.getName().equalsIgnoreCase(word)) {
                return Collections.singleton(child);
            }
        }
        return parent.getChildren();
    }

    public static LiteralArgumentBuilder<CommandSource> a(String name) {
        return new LiteralArgumentBuilder<CommandSource>(name) {
            public LiteralCommandNode<CommandSource> b() {
                return new CaseInsensitiveLiteral(super.build());
            }
        };
    }

    public void a(StringReader reader, CommandContextBuilder<CommandSource> ctx) throws CommandSyntaxException {
        int start = reader.getCursor();
        String literal = getLiteral();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        String word = reader.getString().substring(start, reader.getCursor());
        boolean full = word.equalsIgnoreCase(literal);
        boolean typingPrefix = !reader.canRead() && literal.toLowerCase(Locale.ROOT).startsWith(word.toLowerCase(Locale.ROOT));
        if (!full && !typingPrefix) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, literal);
        }
        ctx.withNode(this, StringRange.between(start, reader.getCursor()));
    }

    public CompletableFuture<Suggestions> a(CommandContext<CommandSource> context, SuggestionsBuilder builder) {
        String literal = getLiteral();
        String typed = builder.getRemaining();
        if (literal.toLowerCase(Locale.ROOT).startsWith(typed.toLowerCase(Locale.ROOT))) {
            String suggestion = typed + literal.substring(typed.length());
            return builder.suggest(suggestion).buildFuture();
        }
        return Suggestions.empty();
    }

    public Collection<? extends CommandNode<CommandSource>> a(StringReader input) {
        return a(this, input);
    }

    public static final class a extends RootCommandNode<CommandSource> {
        public Collection<? extends CommandNode<CommandSource>> a(StringReader input) {
            return CaseInsensitiveLiteral.a(this, input);
        }
    }
}
