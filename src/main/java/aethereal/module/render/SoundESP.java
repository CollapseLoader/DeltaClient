package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.SoundEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.CounterUtil;
import aethereal.util.ProjectUtil;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(name = "Sound ESP", description = "Отображает место, где был воспроизведён выбранный звук", category = Category.Render)
public class SoundESP extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отслеживать звуки", new BooleanSetting("Трезубец", true), new BooleanSetting("Фейерверк", true), new BooleanSetting("Взрывы", true));
    private final List<a> c = new ArrayList<>();

    public SoundESP() {
        a(this.b);
    }

    @EventTarget
    public void a(SoundEvent e) {
        String path = e.getSound().getId().getPath();
        if ((path.contains("entity.firework_rocket.launch") && this.b.a("Фейерверк").c().booleanValue()) || ((path.contains("entity.generic.explode") && this.b.a("Взрывы").c().booleanValue()) || (path.contains("item.trident.return") && this.b.a("Трезубец").c().booleanValue()))) {
            boolean exists = false;
            for (a info : this.c) {
                if (Math.abs(info.b().getX() - e.getSound().getX()) <= 0.5d && Math.abs(info.b().getY() - e.getSound().getY()) <= 0.5d && Math.abs(info.b().getZ() - e.getSound().getZ()) <= 0.5d) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                this.c.add(new a(e.getSound()));
            }
        }
    }

    @EventTarget
    public void a(DrawEvent event) {
        String str;
        if (event.b()) {
            int color = ColorUtil.convertToARGB(0, 0, 0, 100);
            for (a soundInfo : this.c) {
                if (soundInfo.getCounter().a(5500L)) {
                    this.c.remove(soundInfo);
                } else {
                    Vector2f screenPos = ProjectUtil.project(soundInfo.b().getX(), soundInfo.b().getY(), soundInfo.b().getZ());
                    if (ProjectUtil.isOnScreen(screenPos)) {
                        String path = soundInfo.b().getId().getPath();
                        if (path.contains("firework_rocket")) {
                            str = "Фейерверк";
                        } else if (path.contains("explode")) {
                            str = "Взрывы";
                        } else {
                            str = path.contains("trident") ? "Трезубец" : "Звук";
                        }
                        String soundName = str;
                        int distance = (int) mc.player.getPos().distanceTo(new Vec3d(soundInfo.b().getX(), soundInfo.b().getY(), soundInfo.b().getZ()));
                        int timeAlive = (int) (soundInfo.getCounter().c() / 1000);
                        Text text = Text.literal(soundName + " [" + distance + "м/" + timeAlive + " сек]");
                        float textWidth = Fonts.e.a(text, 7.5f);
                        float textHeight = Fonts.e.d().lineHeight() * 7.5f;
                        float textX = screenPos.x() - (textWidth / 2.0f);
                        float textY = screenPos.y();
                        event.getDraw2DProcessor().a(event.i().getMatrices(), textX - 2.0f, textY, textWidth + 4.0f, textHeight, 0.0f, color);
                        Fonts.e.a(event.i().getMatrices(), text, textX, textY, 7.5f);
                    }
                }
            }
        }
    }

    public static class a {
        public final CounterUtil a = new CounterUtil();
        public final SoundInstance b;

        public a(SoundInstance sound) {
            this.b = sound;
            this.a.b();
        }

        public CounterUtil getCounter() {
            return this.a;
        }

        public SoundInstance b() {
            return this.b;
        }
    }
}
