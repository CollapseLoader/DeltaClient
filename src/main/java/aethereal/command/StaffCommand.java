package aethereal.command;

import aethereal.core.Delta;
import aethereal.staff.StaffConstructor;
import aethereal.staff.StaffProcessor;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.Objects;

@Command(name = "staff")
public class StaffCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        StaffProcessor processor = Delta.h().d().f();
        LiteralArgumentBuilder literalArgumentBuilderThen = builder.then(a("add").executes(context -> {
            ChatUtil.sendMessage("Использование: .staff add <ник>");
            return 1;
        }).then(b("ник").suggests(a()).executes(context2 -> {
            String name = a(context2, "ник");
            if (processor.d(name)) {
                ChatUtil.sendMessage("Стафф " + name + " уже находится в списке стаффа.");
                return 1;
            }
            processor.b(name);
            processor.unSetup();
            ChatUtil.sendMessage("Стафф " + name + " был успешно добавлен в список стаффа.");
            return 1;
        })));
        LiteralArgumentBuilder literalArgumentBuilderExecutes = a("remove").executes(context3 -> {
            ChatUtil.sendMessage("Использование: .staff remove <ник>");
            return 1;
        });
        RequiredArgumentBuilder<CommandSource, String> requiredArgumentBuilderB = b("ник");
        Objects.requireNonNull(processor);
        literalArgumentBuilderThen.then(literalArgumentBuilderExecutes.then(requiredArgumentBuilderB.suggests(a(processor::a, (v0) -> {
            return v0.a();
        })).executes(context4 -> {
            String name = a(context4, "ник");
            if (!processor.d(name)) {
                ChatUtil.sendMessage("Стафф " + name + " не найден в списке стаффа.");
                return 1;
            }
            processor.c(name);
            processor.unSetup();
            ChatUtil.sendMessage("Стафф " + name + " был успешно удален из списка стаффа.");
            return 1;
        }))).then(a("list").executes(context5 -> {
            if (processor.a().isEmpty()) {
                ChatUtil.sendMessage("Список стаффа пуст.");
                return 1;
            }
            ChatUtil.sendMessage("Список стаффа (" + processor.a().size() + "):");
            for (StaffConstructor staffConstructor : processor.a()) {
                ChatUtil.sendMessage("  - " + staffConstructor.a());
            }
            return 1;
        })).then(a("clear").executes(context6 -> {
            ChatUtil.sendMessage("Было успешно удалено стаффа из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
        })).executes(context7 -> {
            ChatUtil.sendMessage("Использование: .staff <add|remove|list|clear>");
            return 1;
        });
    }
}
