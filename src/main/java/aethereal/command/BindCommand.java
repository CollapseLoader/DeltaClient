package aethereal.command;

import aethereal.config.ModuleProcessor;
import aethereal.core.Delta;
import aethereal.core.Module;
import aethereal.setting.BindSetting;
import aethereal.util.ChatUtil;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Command(a = "bind")
public class BindCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        ModuleProcessor processor = Delta.h().d().t();
        LiteralArgumentBuilder literalArgumentBuilderExecutes = a("add").executes(context -> {
            ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
            return 1;
        });
        RequiredArgumentBuilder<CommandSource, String> requiredArgumentBuilderC = c("аргументы");
        Objects.requireNonNull(processor);
        builder.then(literalArgumentBuilderExecutes.then(requiredArgumentBuilderC.suggests(a(processor::e, (v0) -> {
            return v0.j();
        })).executes(context2 -> {
            String args = a(context2, "аргументы");
            if (args == null || args.trim().isEmpty()) {
                ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
                return 1;
            }
            String[] parts = args.trim().split("\\s+");
            if (parts.length < 2) {
                ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
                return 1;
            }
            String keyName = parts[parts.length - 1];
            String moduleName = args.substring(0, args.length() - keyName.length()).trim();
            KeyUtil key = KeyUtil.a(keyName);
            if (key == KeyUtil.UNKNOWN) {
                ChatUtil.a("Клавиша " + keyName + " не найдена.");
                return 1;
            }
            Module module = processor.e().stream().filter(m -> {
                return m.j().equalsIgnoreCase(moduleName);
            }).findFirst().orElse(null);
            if (module == null) {
                ChatUtil.a("Модуль " + moduleName + " не найден.");
                return 1;
            }
            module.a(key.a());
            processor.unSetup();
            ChatUtil.a("Модуль " + moduleName + " был успешно привязан к клавише " + key.b() + ".");
            return 1;
        }))).then(a("list").executes(context3 -> {
            List<Module> modules = processor.e().stream().filter(module -> {
                return module.p() != -1 || module.e().stream().anyMatch(setting -> {
                    if (setting instanceof BindSetting bind) {
                        return bind.c().intValue() != -1;
                    }
                    return false;
                });
            }).toList();
            if (modules.isEmpty()) {
                ChatUtil.a("Список модулей с привязанными клавишами пуст.");
                return 1;
            }
            ChatUtil.a("Список модулей с привязанными клавишами (" + modules.size() + "):");
            modules.forEach(module2 -> {
                String binds = module2.e().stream().filter(setting -> {
                    if (setting instanceof BindSetting bind) {
                        return bind.c().intValue() != -1;
                    }
                    return false;
                }).map(setting2 -> {
                    return setting2.i() + " &8→&7 " + KeyUtil.a(((BindSetting) setting2).c().intValue()).b();
                }).collect(Collectors.joining(", "));
                ChatUtil.a(" &c" + module2.j() + (module2.p() != -1 ? " &8→&7 " + KeyUtil.a(module2.p()).b() : "") + (binds.isEmpty() ? "" : " &8[&7" + binds + "&8]"));
            });
            return 1;
        })).then(a("clear").executes(context4 -> {
            for (Module module : processor.e()) {
                module.a(-1);
            }
            processor.unSetup();
            ChatUtil.a("Привязанные клавиши были успешно очищены у всех модулей.");
            return 1;
        })).then(a("remove").executes(context5 -> {
            ChatUtil.a("Использование: .bind remove <клавиша>");
            return 1;
        }).then(d("клавиша").suggests(b()).executes(context6 -> {
            String name = a(context6, "клавиша");
            KeyUtil key = KeyUtil.a(name);
            if (key == KeyUtil.UNKNOWN) {
                ChatUtil.a("Клавиша " + name + " не найдена.");
                return 1;
            }
            List<Module> modules = processor.e().stream().filter(module -> {
                return module.p() == key.a();
            }).toList();
            if (modules.isEmpty()) {
                ChatUtil.a("Нет модулей, привязанных к клавише " + key.b() + ".");
                return 1;
            }
            modules.forEach(module2 -> {
                module2.a(-1);
            });
            processor.unSetup();
            ChatUtil.a("Клавиша " + key.b() + " была успешно удалена из " + modules.size() + " модулей.");
            return 1;
        }))).executes(context7 -> {
            ChatUtil.a("Использование: .bind <add|list|clear|remove>");
            return 1;
        });
    }
}
