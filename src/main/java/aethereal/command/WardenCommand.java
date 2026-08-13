package aethereal.command;

import aethereal.core.Delta;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.List;

@Command(a = "warden")
public class WardenCommand extends BaseCommand {
    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        List<Integer> anarchies = Delta.h().d().t().aU().getAnarchyList();
        builder.then(a("add").executes(context -> {
            ChatUtil.a("Использование: .warden add <анархия>");
            return 1;
        }).then(e("анархия").executes(context2 -> {
            int anarchy = b(context2, "анархия");
            if (anarchy >= 1 && anarchy <= 999) {
                if (!anarchies.contains(Integer.valueOf(anarchy))) {
                    if (anarchies.size() < 10) {
                        anarchies.add(Integer.valueOf(anarchy));
                        ChatUtil.a("Анархия " + anarchy + " добавлена.");
                        return 1;
                    }
                    ChatUtil.a("Можно добавить максимум 10 анархий.");
                    return 1;
                }
                ChatUtil.a("Анархия " + anarchy + " уже в списке.");
                return 1;
            }
            ChatUtil.a("Анархия должна быть от 1 до 999.");
            return 1;
        }))).then(a("remove").executes(context3 -> {
            ChatUtil.a("Использование: .warden remove <анархия>");
            return 1;
        }).then(e("анархия").executes(context4 -> {
            int anarchy = b(context4, "анархия");
            if (!anarchies.remove(Integer.valueOf(anarchy))) {
                ChatUtil.a("Анархия " + anarchy + " не найдена.");
                return 1;
            }
            ChatUtil.a("Анархия " + anarchy + " удалена.");
            return 1;
        }))).then(a("list").executes(context5 -> {
            if (!anarchies.isEmpty()) {
                ChatUtil.a("Анархии (" + anarchies.size() + "): " + anarchies);
                return 1;
            }
            ChatUtil.a("Список анархий пуст.");
            return 1;
        })).then(a("clear").executes(context6 -> {
            anarchies.clear();
            ChatUtil.a("Список анархий очищен.");
            return 1;
        })).executes(context7 -> {
            ChatUtil.a("Использование: .warden <add|remove|list|clear>");
            return 1;
        });
    }
}
