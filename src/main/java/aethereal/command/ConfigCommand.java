package aethereal.command;

import aethereal.config.ModuleProcessor;
import aethereal.core.Delta;
import aethereal.core.Module;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Util;

import java.io.File;

@Command(name = "cfg")
public class ConfigCommand extends BaseCommand {
    @SuppressWarnings("unchecked")
    private static <T> void resetSettingValue(Setting<?> setting) {
        ((Setting<T>) setting).a((T) setting.g());
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        ModuleProcessor processor = Delta.getInstance().getModuleProcessor().t();
        builder.then(a("save").executes(context -> {
            ChatUtil.sendMessage("Использование: .cfg save <имя>");
            return 1;
        }).then(b("имя").executes(context -> {
            String configName = a(context, "имя");
            processor.b(configName);
            ChatUtil.sendMessage("Конфиг " + configName + " был успешно сохранен.");
            return 1;
        }))).then(a("load").executes(context -> {
            ChatUtil.sendMessage("Использование: .cfg load <имя>");
            return 1;
        }).then(b("имя").suggests(c()).executes(context -> {
            String configName = a(context, "имя");
            if (processor.c(configName)) {
                ChatUtil.sendMessage("Конфиг " + configName + " был успешно загружен.");
                return 1;
            }
            ChatUtil.sendMessage("Конфиг " + configName + " не найден.");
            return 1;
        }))).then(a("list").executes(context -> {
            File configDir = processor.d();
            File[] configFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (configFiles == null || configFiles.length == 0) {
                ChatUtil.sendMessage("Список конфигов пуст.");
                return 1;
            }
            ChatUtil.sendMessage("Список конфигов (" + configFiles.length + "):");
            for (File configFile : configFiles) {
                ChatUtil.sendMessage("  - " + configFile.getName());
            }
            return 1;
        })).then(a("reset").executes(context -> {
            for (Module module : processor.e()) {
                module.a(false);
                module.a(-1);
                for (Setting<?> setting : module.e()) {
                    if (setting.g() != null) {
                        resetSettingValue(setting);
                    }
                }
            }
            ChatUtil.sendMessage("Все модули были сброшены в состояние по умолчанию.");
            return 1;
        })).then(a("remove").executes(context -> {
            ChatUtil.sendMessage("Использование: .cfg remove <имя>");
            return 1;
        }).then(b("имя").suggests(c()).executes(context -> {
            String configName = a(context, "имя");
            if (processor.d(configName)) {
                ChatUtil.sendMessage("Конфиг " + configName + " был успешно удален.");
                return 1;
            }
            ChatUtil.sendMessage("Конфиг " + configName + " не найден.");
            return 1;
        }))).then(a("dir").executes(context -> {
            File configDir = processor.d();
            if (!configDir.exists()) {
                configDir.mkdirs();
            }
            ChatUtil.sendMessage("Папка конфигов: " + configDir.getAbsolutePath());
            Util.getOperatingSystem().open(configDir);
            return 1;
        })).executes(context -> {
            ChatUtil.sendMessage("Использование: .cfg <load|save|list|reset|remove|dir>");
            return 1;
        });
    }

    private SuggestionProvider<CommandSource> c() {
        return (context, builder) -> {
            ModuleProcessor processor = Delta.getInstance().getModuleProcessor().t();
            File configDir = processor.d();
            File[] configFiles = configDir.exists() ? configDir.listFiles((dir, name) -> name.endsWith(".json")) : null;
            if (configFiles != null) {
                for (File configFile : configFiles) {
                    String name = configFile.getName();
                    builder.suggest(name.substring(0, name.length() - 5));
                }
            }
            return builder.buildFuture();
        };
    }
}
