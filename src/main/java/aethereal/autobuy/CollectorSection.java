package aethereal.autobuy;

import aethereal.config.Condition;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.module.misc.Collector_2;
import aethereal.render.*;
import aethereal.ui.element.Section;
import aethereal.util.Marker_2;
import aethereal.util.MathUtil;
import aethereal.util.ProcessIdUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

import java.util.List;
import java.util.stream.Stream;

public class CollectorSection extends Section {
    private final Vector4f a;
    private final Vector4f b;
    private Collector_2.b c;
    private Collector_2.b d;
    private Condition e;
    private int f;
    private float g;

    public CollectorSection() {
        super("S", "collector section");
        this.a = new Vector4f();
        this.b = new Vector4f();
    }

    @Override
    public void a(DrawContext context, Vector4f frame, int mouseX, int mouseY, float scroll, float delta) {
        float startX = frame.x + 8.0f;
        float top = frame.y;
        float settingsX = ((startX + 240.0f) - 4.0f) + 8.0f;
        a(context, frame, 20.0f, 4.0f, 10, top, scroll, mouseX, mouseY, delta);
        a(context, frame, settingsX, top, mouseX, mouseY, delta);
    }

    private void a(DrawContext context, Vector4f frame, float slot, float gap, int columns, float top, float scroll, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        Draw3DProcessor draw3d = Delta.getInstance().getModuleProcessor().j();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float startX = frame.x + 8.0f;
        float bottom = frame.y + frame.w;
        float scale = (slot / 16.0f) * 0.75f;
        float offset = (slot - (16.0f * scale)) / 2.0f;
        List<Collector_2.b> items = Delta.getInstance().getModuleProcessor().p().e();
        this.g = 0.0f;
        this.d = null;
        if (this.c == null && !items.isEmpty()) {
            this.c = items.getFirst();
        }
        ScissorUtil.a(matrices, frame.x, top, frame.z, bottom - top);
        for (int i = 0; i < items.size(); i++) {
            Collector_2.b item = items.get(i);
            float slotX = startX + ((i % columns) * (slot + gap));
            float slotY = top + ((i / columns) * (slot + gap)) + scroll;
            this.g = Math.max(this.g, (((i / columns) + 1) * (slot + gap)) - gap);
            if (slotY + slot >= top && slotY <= bottom) {
                if (MathUtil.a(mouseX, mouseY, slotX, slotY, slot, slot)) {
                    this.d = item;
                }
                item.h().a(item.k());
                item.h().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
                float on = item.h().c();
                draw.a(matrices, slotX, slotY, slot, slot, 4.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(255, 255, 255, 255), 0.023529412f * (item == this.c ? 1.0f : this.d == item ? 0.5f : 0.0f)));
                draw.a(matrices, slotX, slotY, slot, slot, 4.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).b()));
                draw3d.a(context, item.c(), slotX + offset, slotY + offset, 0, 0.5f + (0.5f * on), scale, false);
                if (item.m() > 1) {
                    String count = String.valueOf(item.m());
                    Fonts.c.a(matrices, count, ((slotX + slot) - 2.0f) - Fonts.c.a(count, 6.5f), (slotY + slot) - 8.0f, 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), 0.25f + (0.75f * item.h().c())));
                }
            }
        }
        ScissorUtil.a(matrices);
    }

    private void a(DrawContext context, Vector4f frame, float settingsX, float y, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        Draw3DProcessor draw3d = Delta.getInstance().getModuleProcessor().j();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float width = ((frame.x + frame.z) - 8.0f) - settingsX;
        this.e = null;
        if (this.c == null) {
            return;
        }
        float rowX = settingsX + 8.0f;
        float rowW = width - 16.0f;
        float center = y + 10.0f;
        this.c.h().a(this.c.k());
        this.c.h().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
        float active = this.c.h().c();
        float fade = 0.35f + (0.65f * active);
        this.a.set(((rowX + rowW) - 6.0f) - 14.0f, center - 4.25f, 14.0f, 8.5f);
        float nameX = rowX + 20.0f;
        float nameLimit = (this.c.a() ? (this.a.x - 6.0f) - 40.0f : this.a.x) - 6.0f;
        draw3d.a(context, this.c.c(), rowX, center - 6.0f, 0, fade, 0.75f, false);
        Fonts.c.c(matrices, this.c.j(), nameX, (center - (Fonts.c.a(7.5f) / 2.0f)) - 0.75f, 7.5f, ColorUtil.lerpColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), theme.a(ThemeInfo.TEXT).toIntColor(), active), nameLimit - nameX);
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 3.25f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), active));
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 3.25f, 0.3f, theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor());
        draw.a(matrices, this.a.x + 1.5f + (5.5f * active), this.a.y + 1.5f, 5.5f, 5.5f, 1.75f, ColorUtil.lerpColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.ap, InterfaceC0020Opcode.ap, InterfaceC0020Opcode.bk, 255), ColorUtil.convertToARGB(255, 255, 255, 255), active));
        this.b.set(0.0f, 0.0f, 0.0f, 0.0f);
        if (this.c.a()) {
            this.b.set((this.a.x - 6.0f) - 32.0f, center - 5.0f, 32.0f, 10.0f);
            Fonts.c.b(matrices, ProcessIdUtil.a, this.b.x + 6.0f, (center - (Fonts.c.a(11.0f) / 2.0f)) - 2.0f, 11.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), fade));
            Fonts.c.b(matrices, String.valueOf(this.c.m()), this.b.x + (this.b.z / 2.0f), (center - (Fonts.c.a(6.75f) / 2.0f)) - 0.75f, 6.75f, ColorUtil.lerpColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), theme.a(ThemeInfo.TEXT).toIntColor(), active));
            Fonts.c.b(matrices, Marker_2.b, (this.b.x + this.b.z) - 6.0f, (center - (Fonts.c.a(11.0f) / 2.0f)) - 1.25f, 11.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), fade));
        }
        List<Condition> conditions = Stream.concat(this.c.e() == null ? Stream.empty() : this.c.e().b().stream(), this.c.f() == null ? Stream.empty() : this.c.f().b().stream()).toList();
        float enchantY = y + 20.0f + 8.0f;
        for (Condition condition : conditions) {
            if (!condition.d()) {
                condition.a().a(condition.b());
                condition.a().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
                float value = condition.a().c() * active;
                float centerY = enchantY + 5.0f;
                float switchX = ((rowX + rowW) - 6.0f) - 13.0f;
                float switchY = centerY - 3.8475f;
                float levelX = (switchX - 6.0f) - 28.0f;
                Fonts.c.a(matrices, condition.e(), rowX, (centerY - (Fonts.c.a(6.75f) / 2.0f)) - 0.75f, 6.75f, ColorUtil.lerpColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), theme.a(ThemeInfo.TEXT).toIntColor(), value));
                if (condition.c()) {
                    draw.a(context, levelX + 13.625f, centerY - 2.5f, 0.75f, 5.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * fade));
                    Fonts.c.b(matrices, Marker_2.b, levelX + 7.0f, (centerY - (Fonts.c.a(11.0f) / 2.0f)) - 1.25f, 11.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), fade));
                    Fonts.c.b(matrices, ProcessIdUtil.a, levelX + 21.0f, (centerY - (Fonts.c.a(11.0f) / 2.0f)) - 2.0f, 11.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), fade));
                }
                draw.a(matrices, switchX, switchY, 13.0f, 7.695f, 3.078f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), value));
                draw.a(matrices, switchX, switchY, 13.0f, 7.695f, 3.078f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * fade));
                draw.a(matrices, switchX + 1.496f + (5.216f * value), switchY + 1.496f, 4.703f, 4.703f, 1.539f, ColorUtil.lerpColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.ap, InterfaceC0020Opcode.ap, InterfaceC0020Opcode.bk, 255), ColorUtil.convertToARGB(255, 255, 255, 255), value));
                if (MathUtil.a(mouseX, mouseY, switchX, switchY, 13.0f, 7.695f)) {
                    this.e = condition;
                    this.f = 0;
                } else if (condition.c() && MathUtil.a(mouseX, mouseY, levelX, centerY - 5.0f, 14.0f, 10.0f)) {
                    this.e = condition;
                    this.f = 1;
                } else if (condition.c() && MathUtil.a(mouseX, mouseY, levelX + 14.0f, centerY - 5.0f, 14.0f, 10.0f)) {
                    this.e = condition;
                    this.f = 2;
                }
                enchantY += 14.0f;
            }
        }
    }

    @Override
    public boolean a(double mouseX, double mouseY, int button) {
        if (this.c != null && MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w)) {
            this.c.a(!this.c.k());
            return true;
        }
        if (this.c != null && MathUtil.a(mouseX, mouseY, this.b.x, this.b.y, this.b.z / 2.0f, this.b.w)) {
            this.c.b(Math.max(1, this.c.m() - 1));
            return true;
        }
        if (this.c != null && MathUtil.a(mouseX, mouseY, this.b.x + (this.b.z / 2.0f), this.b.y, this.b.z / 2.0f, this.b.w)) {
            this.c.b(Math.min(this.c.b(), this.c.m() + 1));
            return true;
        }
        if (this.e != null) {
            switch (this.f) {
                case 0:
                    this.e.a(!this.e.b());
                    return true;
                case 1:
                    this.e.b(1);
                    return true;
                case 2:
                    this.e.b(-1);
                    return true;
                default:
                    return true;
            }
        }
        if (this.d != null) {
            this.c = this.d;
            return true;
        }
        return false;
    }

    @Override
    public float a(Vector4f frame) {
        return this.g;
    }
}
