package aethereal.command;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.util.ChatUtil;
import aethereal.util.ProjectUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "way")
public class WayCommand extends BaseCommand {
    private final List<b> c = new ArrayList<>();
    private final List<String> d = new ArrayList<>();
    private a e = a.NONE;

    public List<b> c() {
        return this.c;
    }

    public void a(a eventMode) {
        this.e = eventMode;
    }

    @Override
    public void a(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(a("add").executes(context -> {
            ChatUtil.sendMessage("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
            return 1;
        }).then(d("название").executes(context2 -> {
            ChatUtil.sendMessage("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
            return 1;
        }).then(e("x").executes(context3 -> {
            ChatUtil.sendMessage("Использование: .way add <название> <x> <z> или .way add <название> <x> <y> <z>\"");
            return 1;
        }).then(e("y или z").executes(context4 -> {
            a(a(context4, "название"), new Vec3d(b(context4, "x"), mc.player.getY(), b(context4, "y или z")));
            return 1;
        }).then(e("z").executes(context5 -> {
            a(a(context5, "название"), new Vec3d(b(context5, "x"), b(context5, "y или z"), b(context5, "z")));
            return 1;
        })))))).then(a("me").executes(context6 -> {
            a("me", mc.player.getPos());
            return 1;
        })).then(a("remove").executes(context7 -> {
            ChatUtil.sendMessage("Использование: .way remove <название>");
            return 1;
        }).then(d("название").suggests(a(() -> {
            return this.c;
        }, (v0) -> {
            return v0.a();
        })).executes(context8 -> {
            String name = a(context8, "название");
            if (!g(name)) {
                ChatUtil.sendMessage("Метка с именем &c" + name + " &7отсутствует");
                return 1;
            }
            this.c.removeIf(way -> {
                return way.a().equalsIgnoreCase(name);
            });
            ChatUtil.sendMessage("Метка с именем &c" + name + " &7успешно удалена");
            return 1;
        }))).then(a("list").executes(context9 -> {
            if (this.c.isEmpty()) {
                ChatUtil.sendMessage("Список меток не содержит элементов");
                return 1;
            }
            ChatUtil.sendMessage("Список всех меток (" + this.c.size() + "):");
            for (b way : this.c) {
                ChatUtil.sendMessage("— &c" + way.a() + " &7[&f" + a(way.b()) + "&7]&f");
            }
            return 1;
        })).then(a("event").executes(context10 -> {
            this.e = a.WAY;
            mc.player.networkHandler.sendCommand("event delay");
            return 1;
        })).then(a("clear").executes(context11 -> {
            ChatUtil.sendMessage("Количество удалённых меток: " + this.c.size());
            this.c.clear();
            return 1;
        })).executes(context12 -> {
            ChatUtil.sendMessage("Использование: .way <add|me|remove|list|clear|event>");
            return 1;
        });
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (this.e == a.NONE || !event.isReceive()) {
            return;
        }
        GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) event.getPacket();
        if (class_7439VarD instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket s2CPacket = class_7439VarD;
            String message = s2CPacket.content().getString();
            if (message.contains("[Ивенты]") || message.contains("Аир-дроп:") || message.contains("|| /warp portal") || message.contains("|| Координаты:") || message.contains("Призван игроком:") || message.contains("Уровень лута:") || message.contains("Статус:") || message.contains("[1]") || message.contains("[2]")) {
                event.a(true);
                this.d.add(message);
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.e != a.NONE && !this.d.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < this.d.size(); i++) {
                String message = this.d.get(i);
                if (message.contains("[1] Маяк убийца") || message.contains("[1] Вулкан") || message.contains("[1] Метеоритный дождь") || message.contains("[1] Гейзер")) {
                    for (int j = i; j < this.d.size(); j++) {
                        String next = this.d.get(j);
                        if (next.contains("|| Координаты:")) {
                            Matcher matcher = Pattern.compile("\\[(-?\\d+) (-?\\d+) (-?\\d+)]").matcher(next);
                            if (!matcher.find()) {
                                break;
                            }
                            Vec3d pos = new Vec3d(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
                            if (this.e == a.GPS) {
                                Delta.getInstance().getModuleProcessor().u().d().a(pos);
                            } else {
                                a("Ивент", pos);
                            }
                            found = true;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!found) {
                ChatUtil.sendMessage("Нет активного события с координатами.");
            }
            this.d.clear();
            this.e = a.NONE;
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (event.b()) {
            for (b way : this.c) {
                a(event, way, mc.player.getEyePos());
            }
            Vec3d gps = Delta.getInstance().getModuleProcessor().u().d().c();
            if (gps != null) {
                a(event, gps, mc.player.getEyePos());
            }
        }
    }

    public void a(String name, Vec3d pos) {
        String trimmed = name.length() > 6 ? name.substring(0, 6) : name;
        boolean replaced = this.c.removeIf(way -> {
            return way.a().equalsIgnoreCase(trimmed) || way.b().distanceTo(pos) <= 5.0d;
        });
        this.c.add(new b(trimmed, pos));
        ChatUtil.sendMessage("Метка &c" + trimmed + (replaced ? " &7успешно переставлена: " : " &7успешно добавлена: ") + a(pos));
    }

    private boolean g(String name) {
        return this.c.stream().anyMatch(way -> {
            return way.a().equalsIgnoreCase(name);
        });
    }

    private String a(Vec3d pos) {
        return ((int) pos.getX()) + ", " + ((int) pos.getY()) + ", " + ((int) pos.getZ());
    }

    private void a(DrawEvent event, b way, Vec3d eyes) {
        Vector2f screen = ProjectUtil.project(way.b().x, way.b().y, way.b().z);
        if (ProjectUtil.isOnScreen(screen)) {
            int primary = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
            int background = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.BACKGROUND_HUD).toIntColor();
            Text text = Text.literal(way.a().toUpperCase(Locale.ROOT)).append(Text.literal("  /  ").setStyle(Style.EMPTY.withColor(primary))).append(Text.literal(String.format(Locale.US, "%.1fм", Double.valueOf(eyes.distanceTo(way.b())))));
            float width = 14.0f + Fonts.e.a(text, 6.25f);
            float x = screen.x() - (width / 2.0f);
            float y = screen.y() - 5.75f;
            event.getDraw2DProcessor().a(event.h(), x, y, width, 11.5f, 3.5f, ColorUtil.applyAlphaToColor(background, Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.BACKGROUND_HUD).getAlphaFloat()), 1.0f, ColorUtil.applyAlphaToColor(background, Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.BACKGROUND_HUD).getAlphaFloat()), 6.0f);
            Fonts.a.a(event.h(), "F", x + 3.0f, y + 3.0f, 5.5f, primary);
            Fonts.e.a(event.h(), text, x + 3.0f + 5.5f + 2.5f, (y + ((11.5f - Fonts.e.a(6.25f)) / 2.0f)) - 0.25f, 6.25f);
        }
    }

    private void a(DrawEvent event, Vec3d gps, Vec3d eyes) {
        MatrixStack matrices = event.h();
        int primary = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
        double dx = gps.x - mc.player.getX();
        double dz = gps.z - mc.player.getZ();
        float targetYaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float angle = MathHelper.wrapDegrees(targetYaw - mc.player.getYaw());
        float cx = mc.getWindow().getScaledWidth() / 2.0f;
        float cy = mc.getWindow().getScaledHeight() * 0.25f;
        matrices.push();
        matrices.translate(cx, cy, 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
        event.getDraw2DProcessor().a(matrices, Identifier.of("delta", "pictures/triangle.png"), -7.0f, -7.0f, 14.0f, 14.0f, 0.0f, primary);
        matrices.pop();
        Text text = Text.literal(String.format(Locale.US, "%.1fм", Double.valueOf(eyes.distanceTo(gps))));
        Fonts.d.a(matrices, text, cx - (Fonts.d.a(text, 7.0f) / 2.0f), cy + 7.0f + 2.0f, 7.0f);
    }

    public enum a {
        NONE,
        WAY,
        GPS
    }

    public record b(String a, Vec3d b) {
        public b {
        }
    }
}
