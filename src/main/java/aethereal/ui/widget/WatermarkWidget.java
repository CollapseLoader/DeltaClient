package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.ui.element.DragInfo;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WatermarkWidget extends Widget implements Interface {
    private final BooleanSetting g;
    private final BooleanSetting h;
    private final BooleanSetting i;
    private final BooleanSetting j;
    private final BooleanSetting k;
    private final BooleanSetting l;
    private final BooleanSetting m;
    private final BooleanSetting n;
    private final BooleanSetting o;
    private float f;

    public WatermarkWidget() {
        super(new DragInfo("Инфо-панель", 0.0f, 0.0f, 0.0f, 0.0f));
        this.g = new BooleanSetting("Боковое отображение", false);
        this.h = new BooleanSetting("Разделять элементы", false);
        this.i = new BooleanSetting("Частота кадров", true);
        this.j = new BooleanSetting("Задержка игрока", true);
        this.k = new BooleanSetting("Текущее время", true);
        this.l = new BooleanSetting("Логин в клиенте", true);
        this.m = new BooleanSetting("Координаты", true);
        this.n = new BooleanSetting("Задержка сервера", true);
        this.o = new BooleanSetting("Скорость игрока", true);
        j().setWidget(this);
        j().setDragStatus(2);
        a(this.g, this.h, this.i, this.j, this.k, this.l, this.m, this.n, this.o);
    }

    @Override
    public void a(DrawEvent event) {
        float fMethod_4476;
        d().a(true);
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        this.f = MathUtil.c(this.f, mc.getCurrentFps(), 0.1f);
        float iconSize = this.e - 0.5f;
        float logoSize = this.e + 1.0f;
        float sectionGap = !this.h.c().booleanValue() ? 5.0f : 2.0f;
        String[][] topSections = k();
        String[][] bottomSections = l();
        float topWidth = a(topSections, true, iconSize, logoSize, 5.0f, sectionGap, 3.0f, 4.0f);
        float bottomWidth = a(bottomSections, false, iconSize, logoSize, 5.0f, sectionGap, 3.0f, 4.0f);
        float x = this.g.c().booleanValue() ? 5.0f : (mc.getWindow().getScaledWidth() - topWidth) / 2.0f;
        if (this.g.c().booleanValue() || ((platform.inject.accessors.BossBarHudAccessor) mc.inGameHud.getBossBarHud()).getBossBars().isEmpty()) {
            fMethod_4476 = 0.0f;
        } else {
            int size = ((platform.inject.accessors.BossBarHudAccessor) mc.inGameHud.getBossBarHud()).getBossBars().size() - 1;
            Objects.requireNonNull(mc.textRenderer);
            fMethod_4476 = (((12 + (size * (10 + 9))) + 5) * mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue().intValue(), mc.forcesUnicodeFont())) / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont());
        }
        float y = 5.0f + fMethod_4476;
        float bottomX = this.g.c().booleanValue() ? x : x + ((topWidth - bottomWidth) / 2.0f);
        j().setX(x);
        j().setY(y);
        j().setWidth(topWidth);
        j().setHeight(bottomSections.length > 0 ? this.d + 3.0f + this.d : this.d);
        int primaryColor = ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), 1.0f);
        a(event, x, y, topWidth, topSections, true, primaryColor, iconSize, logoSize, 5.0f, sectionGap, 3.0f, 4.0f, -0.5f);
        if (bottomSections.length > 0) {
            a(event, bottomX, y + this.d + 3.0f, bottomWidth, bottomSections, false, primaryColor, iconSize, logoSize, 5.0f, sectionGap, 3.0f, 4.0f, -0.5f);
        }
        super.a(event);
    }

    private void a(DrawEvent event, float x, float y, float width, String[][] sections, boolean logo, int primaryColor, float iconSize, float logoSize, float startPadding, float sectionGap, float iconTextGap, float logoGap, float textYOffset) {
        if (this.h.c().booleanValue()) {
            a(event, x, y, sections, logo, primaryColor, iconSize, logoSize, startPadding, sectionGap, iconTextGap, textYOffset);
            return;
        }
        a(event, x, y, width, this.d, true, 1.0f);
        float cursor = x + startPadding;
        float textY = y + ((this.d - Fonts.e.a(this.e)) / 2.0f) + textYOffset;
        if (logo) {
            Fonts.a.a(event.h(), "a", cursor, y + ((this.d - Fonts.a.a(logoSize)) / 2.0f), logoSize, primaryColor);
            float cursor2 = cursor + Fonts.a.a("a", logoSize) + logoGap;
            a(event, cursor2, y, this.d, 1.0f);
            cursor = cursor2 + 1.0f + sectionGap;
        }
        for (int i = 0; i < sections.length; i++) {
            if (i > 0) {
                a(event, cursor, y, this.d, 1.0f);
                cursor += 1.0f + sectionGap;
            }
            Fonts.a.a(event.h(), sections[i][0], cursor, y + ((this.d - Fonts.a.a(iconSize)) / 2.0f), iconSize, primaryColor);
            float cursor3 = cursor + Fonts.a.a(sections[i][0], iconSize) + iconTextGap;
            Fonts.e.a(event.h(), sections[i][1], cursor3, textY, this.e, -1);
            cursor = cursor3 + Fonts.e.a(sections[i][1], this.e) + sectionGap;
        }
    }

    private void a(DrawEvent event, float x, float y, String[][] sections, boolean logo, int primaryColor, float iconSize, float logoSize, float startPadding, float sectionGap, float iconTextGap, float textYOffset) {
        float cursor = x;
        float textY = y + ((this.d - Fonts.e.a(this.e)) / 2.0f) + textYOffset;
        if (logo) {
            float logoWidth = (startPadding * 2.0f) + Fonts.a.a("a", logoSize);
            a(event, cursor, y, logoWidth, this.d, true, 1.0f);
            Fonts.a.a(event.h(), "a", cursor + startPadding, y + ((this.d - Fonts.a.a(logoSize)) / 2.0f), logoSize, primaryColor);
            cursor += logoWidth + sectionGap;
        }
        for (String[] section : sections) {
            float sectionWidth = (startPadding * 2.0f) + Fonts.a.a(section[0], iconSize) + iconTextGap + Fonts.e.a(section[1], this.e);
            a(event, cursor, y, sectionWidth, this.d, true, 1.0f);
            float inner = cursor + startPadding;
            Fonts.a.a(event.h(), section[0], inner, y + ((this.d - Fonts.a.a(iconSize)) / 2.0f), iconSize, primaryColor);
            Fonts.e.a(event.h(), section[1], inner + Fonts.a.a(section[0], iconSize) + iconTextGap, textY, this.e, -1);
            cursor += sectionWidth + sectionGap;
        }
    }

    private float a(String[][] sections, boolean logo, float iconSize, float logoSize, float startPadding, float sectionGap, float iconTextGap, float logoGap) {
        if (this.h.c().booleanValue()) {
            float width = 0.0f;
            if (logo) {
                width = 0.0f + (startPadding * 2.0f) + Fonts.a.a("a", logoSize) + sectionGap;
            }
            for (String[] section : sections) {
                width += (startPadding * 2.0f) + Fonts.a.a(section[0], iconSize) + iconTextGap + Fonts.e.a(section[1], this.e) + sectionGap;
            }
            return Math.max(0.0f, width - sectionGap);
        }
        float width2 = startPadding;
        if (logo) {
            width2 = width2 + Fonts.a.a("a", logoSize) + logoGap + 1.0f + sectionGap;
        }
        for (int i = 0; i < sections.length; i++) {
            if (i > 0) {
                width2 += 1.0f + sectionGap;
            }
            width2 = width2 + Fonts.a.a(sections[i][0], iconSize) + iconTextGap + Fonts.e.a(sections[i][1], this.e) + sectionGap;
        }
        return width2;
    }

    private String[][] k() {
        List<String[]> sections = new ArrayList<>();
        if (this.l.c().booleanValue()) {
            sections.add(new String[]{"L", Delta.getInstance().g().username()});
        }
        if (this.i.c().booleanValue()) {
            sections.add(new String[]{"q", ((int) this.f) + " FPS"});
        }
        if (this.j.c().booleanValue()) {
            sections.add(new String[]{"P", ServerUtil.d() + " ms"});
        }
        if (this.k.c().booleanValue()) {
            sections.add(new String[]{"T", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))});
        }
        return sections.toArray(new String[0][]);
    }

    private String[][] l() {
        List<String[]> sections = new ArrayList<>();
        if (this.m.c().booleanValue()) {
            sections.add(new String[]{"b", "x " + ((int) mc.player.getX()) + " y " + ((int) mc.player.getY()) + " z " + ((int) mc.player.getZ())});
        }
        if (this.n.c().booleanValue()) {
            sections.add(new String[]{"g", String.format("%.1f TPS", Float.valueOf(Delta.getInstance().getModuleProcessor().v().getTPSHandler().a()))});
        }
        if (this.o.c().booleanValue()) {
            sections.add(new String[]{"e", String.format("%.2f BPS", Double.valueOf(ServerUtil.c()))});
        }
        return sections.toArray(new String[0][]);
    }
}
