package aethereal.command;

import aethereal.core.Delta;
import aethereal.macro.MacrosConstructor;
import aethereal.macro.MacrosProcessor;
import aethereal.util.ChatUtil;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.List;
import java.util.Objects;

@Command(name = "macros")
public class MacrosCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        MacrosProcessor processor = Delta.getInstance().getModuleProcessor().d();
        LiteralArgumentBuilder<CommandSource> literalArgumentBuilderThen = builder.then(a("add").executes(context -> {
            ChatUtil.sendMessage("Использование: .macros add <клавиша> <команда>");
            return 1;
        }).then(d("клавиша").suggests(b()).then(c("команда").executes(context2 -> {
            String key = a(context2, "клавиша");
            String command = a(context2, "команда");
            KeyUtil keyUtil = KeyUtil.a(key);
            if (keyUtil == KeyUtil.UNKNOWN) {
                ChatUtil.sendMessage("Клавиша " + key + " не найдена.");
                return 1;
            }
            processor.a(key, command);
            processor.unSetup();
            ChatUtil.sendMessage("Макрос " + command + " был успешно добавлен на клавишу " + keyUtil.b() + ".");
            return 1;
        }))));
        LiteralArgumentBuilder<CommandSource> literalArgumentBuilderExecutes = a("remove").executes(context3 -> {
            List<MacrosConstructor> macros = processor.a();
            if (macros.isEmpty()) {
                ChatUtil.sendMessage("Список макросов пуст.");
                return 1;
            }
            ChatUtil.sendMessage("Доступные команды для удаления (" + macros.size() + "):");
            for (MacrosConstructor macro : macros) {
                ChatUtil.sendMessage("  " + macro.b() + " (клавиша: " + KeyUtil.a(macro.a()).b() + ")");
            }
            ChatUtil.sendMessage("Использование: .macros remove <команда>");
            return 1;
        });
        RequiredArgumentBuilder<CommandSource, String> requiredArgumentBuilderC = c("команда");
        Objects.requireNonNull(processor);
        literalArgumentBuilderThen.then(literalArgumentBuilderExecutes.then(requiredArgumentBuilderC.suggests(a(processor::a, (v0) -> {
            return v0.b();
        })).executes(context4 -> {
            String command = a(context4, "команда");
            MacrosConstructor macro = processor.a().stream().filter(m -> {
                return m.b().equals(command);
            }).findFirst().orElse(null);
            if (macro == null) {
                ChatUtil.sendMessage("Макрос с командой " + command + " не найден в списке макросов.");
                return 1;
            }
            processor.b(macro.a());
            processor.unSetup();
            ChatUtil.sendMessage("Макрос " + command + " был успешно удален с клавиши " + KeyUtil.a(macro.a()).b() + ".");
            return 1;
        }))).then(a("list").executes(context5 -> {
            List<MacrosConstructor> macros = processor.a();
            if (macros.isEmpty()) {
                ChatUtil.sendMessage("Список макросов пуст.");
                return 1;
            }
            ChatUtil.sendMessage("Список макросов (" + macros.size() + "):");
            for (MacrosConstructor macro : macros) {
                ChatUtil.sendMessage("  " + KeyUtil.a(macro.a()).b() + ": " + macro.b());
            }
            return 1;
        })).then(a("clear").executes(context6 -> {
            ChatUtil.sendMessage("Было успешно удалено макросов из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
        })).executes(context7 -> {
            ChatUtil.sendMessage("Использование: .macros <add|remove|list|clear>");
            return 1;
        });
    }
}
