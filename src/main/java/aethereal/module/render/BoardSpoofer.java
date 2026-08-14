package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.ScoreboardEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.StringSetting;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;
import java.util.Optional;

@ModuleRegister(name = "Board Spoofer", description = "Подменяет значения доната, монет и токенов в Scoreboard", category = Category.Render)
public class BoardSpoofer extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Элементы настройки", new BooleanSetting("Ранг", true), new BooleanSetting("Монеты", true), new BooleanSetting("Токены", true));
    private final ModeSetting c = new ModeSetting("Выберите привилегию", "Игрок", "Игрок", "Барон", "Страж", "Герой", "Аспид", "Сквид", "Глава", "Элита", "Титан", "Принц", "Князь", "Герцог").a(() -> {
        return this.b.a("Ранг").c();
    });
    private final StringSetting d = new StringSetting("Число монет", "", true).a(() -> {
        return this.b.a("Монеты").c();
    });
    private final StringSetting e = new StringSetting("Число токенов", "", true).a(() -> {
        return this.b.a("Токены").c();
    });

    public BoardSpoofer() {
        a(this.b, this.c, this.d, this.e);
    }

    @EventTarget
    public void a(ScoreboardEvent event) {
        Text title = event.b();
        if (this.b.a("Ранг").c().booleanValue()) {
            title = a(title, "Ранг: ", this.c.c());
        }
        if (this.b.a("Монеты").c().booleanValue()) {
            Text previousTitle = title;
            Locale locale = Locale.US;
            Object[] objArr = new Object[1];
            objArr[0] = Long.valueOf(Long.parseLong(this.d.c().isEmpty() ? "0" : this.d.c()));
            title = a(previousTitle, "Монет: ", String.format(locale, "%", objArr));
        }
        if (this.b.a("Токены").c().booleanValue()) {
            title = a(title, "Токенов: ", this.e.c().isEmpty() ? "0" : this.e.c());
        }
        event.setTitle(title);
    }

    private Text a(Text text, String label, String newValue) {
        MutableText rebuilt = Text.empty();
        StringBuilder seen = new StringBuilder();
        text.visit((style, part) -> {
            int previousLength = seen.length();
            seen.append(part);
            int labelIndex = seen.indexOf(label);
            if (labelIndex == -1 || labelIndex + label.length() <= previousLength) {
                rebuilt.append(Text.literal(part).setStyle(style));
                return Optional.empty();
            }
            rebuilt.append(Text.literal(part.substring(0, (labelIndex + label.length()) - previousLength)).setStyle(style));
            rebuilt.append(Text.literal(newValue).setStyle("Ранг: ".equals(label) ? a(newValue) : style));
            return Optional.of(true);
        }, Style.EMPTY);
        return rebuilt.getSiblings().isEmpty() ? text : rebuilt;
    }

    private Style a(String rank) {
        switch (rank) {
            case "Страж":
                return Style.EMPTY.withFormatting(Formatting.YELLOW);
            case "Барон":
            case "Сквид":
                return Style.EMPTY.withFormatting(Formatting.AQUA);
            case "Герой":
                return Style.EMPTY.withFormatting(Formatting.GREEN);
            case "Аспид":
                return Style.EMPTY.withFormatting(Formatting.DARK_AQUA);
            case "Глава":
            case "Титан":
                return Style.EMPTY.withFormatting(Formatting.GOLD);
            case "Элита":
                return Style.EMPTY.withFormatting(Formatting.DARK_PURPLE);
            case "Принц":
            case "Князь":
                return Style.EMPTY.withFormatting(Formatting.RED);
            case "Герцог":
                return Style.EMPTY.withFormatting(Formatting.DARK_RED);
            default:
                return Style.EMPTY.withFormatting(Formatting.WHITE);
        }
    }
}
