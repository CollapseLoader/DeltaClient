package aethereal.command;

import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BaseCommand implements Interface {
    protected final String b = getClass().getAnnotation(Command.class).name();

    public abstract void a(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder);

    public final void a(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = CaseInsensitiveLiteral.a(this.b);
        a(builder);
        dispatcher.register(builder);
        EventManager.a(this);
    }

    protected LiteralArgumentBuilder<CommandSource> a(String name) {
        return CaseInsensitiveLiteral.a(name);
    }

    protected RequiredArgumentBuilder<CommandSource, String> b(String name) {
        return RequiredArgumentBuilder.argument(name, StringArgumentType.string());
    }

    protected RequiredArgumentBuilder<CommandSource, String> c(String name) {
        return RequiredArgumentBuilder.argument(name, StringArgumentType.greedyString());
    }

    protected RequiredArgumentBuilder<CommandSource, String> d(String name) {
        return RequiredArgumentBuilder.argument(name, reader -> {
            int start = reader.getCursor();
            while (reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }
            return reader.getString().substring(start, reader.getCursor());
        });
    }

    protected RequiredArgumentBuilder<CommandSource, Integer> e(String name) {
        return RequiredArgumentBuilder.argument(name, IntegerArgumentType.integer());
    }

    protected RequiredArgumentBuilder<CommandSource, Float> f(String name) {
        return RequiredArgumentBuilder.argument(name, FloatArgumentType.floatArg());
    }

    protected String a(CommandContext<CommandSource> context, String name) {
        return StringArgumentType.getString(context, name);
    }

    protected int b(CommandContext<CommandSource> context, String name) {
        return IntegerArgumentType.getInteger(context, name);
    }

    protected float c(CommandContext<CommandSource> context, String name) {
        return FloatArgumentType.getFloat(context, name);
    }

    protected SuggestionProvider<CommandSource> a() {
        return (context, builder) -> {
            if (mc.player.networkHandler == null) {
                return builder.buildFuture();
            }
            Stream<String> streamFilter = mc.player.networkHandler.getPlayerList().stream().map(entry -> {
                return entry.getProfile().getName();
            }).filter(name -> {
                if (name != null) {
                    return name.toLowerCase().startsWith(builder.getRemainingLowerCase() == null ? "" : builder.getRemainingLowerCase());
                }
                return false;
            });
            Objects.requireNonNull(builder);
            streamFilter.forEach(s -> builder.suggest(s));
            return builder.buildFuture();
        };
    }

    protected SuggestionProvider<CommandSource> b() {
        return (context, builder) -> {
            for (KeyUtil key : KeyUtil.values()) {
                if (key != KeyUtil.UNKNOWN) {
                    builder.suggest(key.name());
                }
            }
            return builder.buildFuture();
        };
    }

    protected <T> SuggestionProvider<CommandSource> a(java.util.function.Supplier<Collection<T>> itemsSupplier, Function<T, String> mapper) {
        return (context, builder) -> {
            String remaining = builder.getRemainingLowerCase() == null ? "" : builder.getRemainingLowerCase();
            Iterator<T> it = itemsSupplier.get().iterator();
            while (it.hasNext()) {
                String name = mapper.apply(it.next());
                if (name != null && name.toLowerCase().startsWith(remaining)) {
                    builder.suggest(name);
                }
            }
            return builder.buildFuture();
        };
    }
}
