package aethereal.command;

import aethereal.core.Delta;
import aethereal.friend.FriendConstructor;
import aethereal.friend.FriendProcessor;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.Objects;

@Command(a = "friend")
public class FriendCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        FriendProcessor processor = Delta.h().d().e();
        LiteralArgumentBuilder literalArgumentBuilderThen = builder.then(a("add").executes(context -> {
            ChatUtil.a("Использование: .friend add <ник>");
            return 1;
        }).then(b("ник").suggests(a()).executes(context2 -> {
            String name = a(context2, "ник");
            if (processor.d(name)) {
                ChatUtil.a("Друг " + name + " уже находится в списке друзей.");
                return 1;
            }
            processor.b(name);
            processor.unSetup();
            ChatUtil.a("Друг " + name + " был успешно добавлен в список друзей.");
            return 1;
        })));
        LiteralArgumentBuilder literalArgumentBuilderExecutes = a("remove").executes(context3 -> {
            ChatUtil.a("Использование: .friend remove <ник>");
            return 1;
        });
        RequiredArgumentBuilder<CommandSource, String> requiredArgumentBuilderB = b("ник");
        Objects.requireNonNull(processor);
        literalArgumentBuilderThen.then(literalArgumentBuilderExecutes.then(requiredArgumentBuilderB.suggests(a(processor::a, (v0) -> {
            return v0.a();
        })).executes(context4 -> {
            String name = a(context4, "ник");
            if (!processor.d(name)) {
                ChatUtil.a("Друг " + name + " не найден в списке друзей.");
                return 1;
            }
            processor.c(name);
            processor.unSetup();
            ChatUtil.a("Друг " + name + " был успешно удален из списка друзей.");
            return 1;
        }))).then(a("list").executes(context5 -> {
            if (processor.a().isEmpty()) {
                ChatUtil.a("Список друзей пуст.");
                return 1;
            }
            ChatUtil.a("Список друзей (" + processor.a().size() + "):");
            for (FriendConstructor friend : processor.a()) {
                ChatUtil.a("  - " + friend.a());
            }
            return 1;
        })).then(a("clear").executes(context6 -> {
            ChatUtil.a("Было успешно удалено друзей из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
        })).executes(context7 -> {
            ChatUtil.a("Использование: .friend <add|remove|list|clear>");
            return 1;
        });
    }
}
