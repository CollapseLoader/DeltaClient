package aethereal.module.misc;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.network.PacketSecurity;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.StringUtils;
import com.google.gson.JsonObject;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ModuleRegister(name = "Communication", description = "Связывает вас с другими игроками через групповые и глобальные сообщения (party, IRC и др.)", category = Category.Misc)
public class Communication extends Module {
    private final BooleanSetting c = new BooleanSetting("Клиентский чат", false);
    private final List<a> d = new ArrayList<>();

    public Communication() {
        BindSetting b = new BindSetting("Отправление метки друзьям", -1).a(() -> {
            JsonObject posObject = new JsonObject();
            posObject.addProperty("x", Double.valueOf(mc.player.getPos().x));
            posObject.addProperty("y", Double.valueOf(mc.player.getPos().y));
            posObject.addProperty("z", Double.valueOf(mc.player.getPos().z));
            Delta.getInstance().f().a(false, "friend", "type", "mark", "pos", posObject);
        });
        a(b, this.c);
    }

    @EventTarget
    public void a(TickEvent event) {
        this.d.removeIf(mark -> {
            return mark.a().a(5000L);
        });
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (this.c.c().booleanValue() && event.isSend()) {
            ChatMessageC2SPacket packet = (ChatMessageC2SPacket) event.getPacket();
            if (packet instanceof ChatMessageC2SPacket) {
                String content = packet.chatMessage();
                if (content.startsWith("@")) {
                    Delta.getInstance().f().a(false, "irc", "message", content.substring(1));
                    event.a(true);
                }
            }
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.b()) {
            for (a mark : this.d) {
                a(event, mark, mc.player.getEyePos());
            }
        }
    }

    private void a(DrawEvent event, a mark, Vec3d eyes) {
        String[] parts = mark.c().split(",\\s*");
        if (parts.length < 3) {
            return;
        }
        Vec3d position = new Vec3d(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
        Vector2f screen = ProjectUtil.project(position.x, position.y, position.z);
        if (ProjectUtil.isOnScreen(screen)) {
            ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
            int primary = theme.a(ThemeInfo.PRIMARY).toIntColor();
            int background = ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.BACKGROUND_HUD).toIntColor(), theme.a(ThemeInfo.BACKGROUND_HUD).b());
            Text text = Text.literal(mark.b().toUpperCase(Locale.ROOT)).append(Text.literal(" /  ").setStyle(Style.EMPTY.withColor(primary))).append(Text.literal(String.format(Locale.US, "%.1fм", Double.valueOf(eyes.distanceTo(position)))));
            float width = (3.0f * 2.0f) + 8.0f + 2.5f + Fonts.e.a(text, 6.25f);
            float height = (8.0f + (3.0f * 2.0f)) - 2.0f;
            float x = screen.x() - (width / 2.0f);
            float y = screen.y() - (height / 2.0f);
            Draw2DProcessor draw = event.getDraw2DProcessor();
            draw.a(event.h(), x, y, width - 0.5f, height, 3.5f, background, 1.0f, background, 6.0f);
            draw.a(event.h(), draw.c().b(mark.b()), null, (x + 3.0f) - 0.5f, (y + 3.0f) - 1.0f, 8.0f, 8.0f, 1.0f, 1.0f);
            Fonts.e.a(event.h(), text, x + 3.0f + 8.0f + 2.0f, (y + ((height - Fonts.e.a(6.25f)) / 2.0f)) - 0.5f, 6.25f);
        }
    }

    @EventTarget
    public void onBackend(BackendEvent event) {
        Packet packet = event.getPacket();
        PacketSecurity security = packet.getSecurity();
        String payload = packet.getPayload();
        String type = security.extractString(payload, "type");
        String user = security.extractString(payload, "user");
        String message = security.extractString(payload, "message");
        String priority = security.extractString(payload, "priority");
        if ("irc".equals(packet.getId()) && this.c.c().booleanValue()) {
            Prefix prefix = Prefix.a(priority);
            MutableText line = Text.empty();
            if (prefix != null) {
                line.append(Text.literal(prefix.a()).setStyle(Style.EMPTY.withFont(Identifier.of("delta", "prefixes")))).append(Text.literal(StringUtils.a));
            }
            line.append(ChatUtil.b("[" + user + "] → " + message));
            ChatUtil.sendMessage((Object) "[IRC]", line);
        }
        if ("friend".equals(packet.getId()) && "mark".equals(type)) {
            JsonObject pos = security.extractElement(payload, "pos").getAsJsonObject();
            String position = String.format("%.0f, %.0f, %.0f", Double.valueOf(pos.get("x").getAsDouble()), Double.valueOf(pos.get("y").getAsDouble()), Double.valueOf(pos.get("z").getAsDouble()));
            String login = security.extractString(payload, "minecraft");
            this.d.removeIf(mark -> {
                return mark.b().equalsIgnoreCase(login);
            });
            this.d.add(new a(new CounterUtil(), login, position));
        }
    }

    public enum Prefix {
        ADMIN("Администратор", "\ue100"),
        STAFF("Сотрудник", "\ue101"),
        YOUTUBER("Ютубер", "\ue102"),
        SHADE("shade", "\ue103"),
        DANGEROUS("dangerous", "\ue104"),
        DEVSTVENIK("девственник", "\ue105"),
        DRUN("друн", "\ue106"),
        QCOLD("qcold", "\ue107"),
        WIN("win", "\ue108"),
        BURMALDA("бурмалда", "\ue109"),
        VOZDUXAN("воздухан", "\ue110");

        private final String l;
        private final String m;

        Prefix(String role, String glyph) {
            this.l = role;
            this.m = glyph;
        }

        public static Prefix a(String role) {
            return Arrays.stream(values()).filter(prefix -> {
                return prefix.l.equalsIgnoreCase(role);
            }).findFirst().orElse(null);
        }

        public String a() {
            return this.m;
        }
    }

    public record a(CounterUtil a, String b, String c) {
    }
}
