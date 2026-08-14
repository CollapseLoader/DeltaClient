package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ScoreboardEvent;
import aethereal.event.TextVisitEvent;
import aethereal.friend.FriendConstructor;
import aethereal.setting.BooleanSetting;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.regex.Pattern;

@ModuleRegister(name = "Streamer Mode", description = "Скрывает личные данные при стриминге и записи", category = Category.Misc)
public class StreamerMode extends Module {
    private final BooleanSetting b = new BooleanSetting("Скрывать скины игроков", true);
    private final BooleanSetting c = new BooleanSetting("Скрывать имена друзей", true);
    private final BooleanSetting d = new BooleanSetting("Скрывать номер анархии", true);

    public StreamerMode() {
        a(this.b, this.c, this.d);
    }

    public BooleanSetting q() {
        return this.b;
    }

    public BooleanSetting r() {
        return this.c;
    }

    public BooleanSetting s() {
        return this.d;
    }

    @EventTarget
    public void a(TextVisitEvent event) {
        event.setText(a(event.b()));
    }

    @EventTarget
    public void a(ScoreboardEvent event) {
        if (this.d.c().booleanValue()) {
            MutableText text = Text.empty();
            event.b().visit((style, part) -> {
                text.append(Text.literal(part.replaceAll("Анархия-\\d+", "Анархия-???")).setStyle(style));
                return Optional.empty();
            }, Style.EMPTY);
            event.setTitle(text);
        }
    }

    public String a(String text) {
        String result = text.replaceAll("(?i)" + mc.getSession().getUsername(), "Protected");
        if (this.c.c().booleanValue()) {
            for (FriendConstructor friend : Delta.getInstance().getModuleProcessor().e().a()) {
                result = Pattern.compile(friend.a(), 82).matcher(result).replaceAll("Protected");
            }
        }
        if (this.d.c().booleanValue()) {
            result = result.replaceAll("Анархия-\\d+", "Анархия-???");
        }
        return result;
    }
}
