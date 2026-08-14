package aethereal.autobuy;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.render.*;
import aethereal.ui.element.Section;
import aethereal.ui.element.TextField;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import aethereal.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Locale;

public class AutoBuySection extends Section implements Interface {
    private final TextField b;
    private final AnimationUtil c;
    private final Vector4f d;
    private final Vector4f e;
    private final Vector4f f;
    private final Vector4f g;
    private AutoBuyEntry h;
    private AutoBuyEntry i;
    private float j;

    public AutoBuySection() {
        super("h", "auto-buy section");
        this.b = new TextField(TextField.a.GUI_SETTING, true);
        this.c = new AnimationUtil();
        this.d = new Vector4f();
        this.e = new Vector4f();
        this.f = new Vector4f();
        this.g = new Vector4f();
        this.b.a("Цена предмета");
    }

    @Override
    public void a(DrawContext context, Vector4f frame, int mouseX, int mouseY, float scroll, float delta) {
        float startX = frame.x + 8.0f;
        float top = frame.y;
        float settingsX = ((startX + 240.0f) - 4.0f) + 8.0f;
        a(context, frame, 20.0f, 4.0f, 10, top, scroll, mouseX, mouseY, delta);
        a(context, frame, settingsX, top, mouseX, mouseY, delta);
    }

    private void a(AutoBuyEntry item) {
        this.h = item;
        this.b.g().setLength(0);
        this.b.a(false);
        if (item != null && item.k() > 0.0d) {
            this.b.g().append((long) item.k());
        }
    }

    private void a(DrawContext context, Vector4f frame, float slot, float gap, int columns, float top, float scroll, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.h().d().i();
        Draw3DProcessor draw3d = Delta.h().d().j();
        ThemeProcessor theme = Delta.h().d().o();
        float startX = frame.x + 8.0f;
        float bottom = frame.y + frame.w;
        float scale = (slot / 16.0f) * 0.75f;
        float offset = (slot - (16.0f * scale)) / 2.0f;
        List<AutoBuyEntry> items = Delta.h().d().q().e();
        this.j = 0.0f;
        this.i = null;
        if (this.h == null && !items.isEmpty()) {
            a(items.getFirst());
        }
        ScissorUtil.a(matrices, frame.x, top, frame.z, bottom - top);
        for (int i = 0; i < items.size(); i++) {
            AutoBuyEntry item = items.get(i);
            float slotX = startX + ((i % columns) * (slot + gap));
            float slotY = top + ((i / columns) * (slot + gap)) + scroll;
            this.j = Math.max(this.j, (((i / columns) + 1) * (slot + gap)) - gap);
            if (slotY + slot >= top && slotY <= bottom) {
                if (MathUtil.a(mouseX, mouseY, slotX, slotY, slot, slot)) {
                    this.i = item;
                }
                item.j().a(item.l());
                item.j().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
                draw.a(matrices, slotX, slotY, slot, slot, 4.0f, ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.023529412f * (item == this.h ? 1.0f : this.i == item ? 0.5f : 0.0f)));
                draw.a(matrices, slotX, slotY, slot, slot, 4.0f, 0.5f, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b()));
                float on = item.j().c();
                draw3d.a(context, item.a(), slotX + offset, slotY + offset, 0, 0.5f + (0.5f * on), scale, false);
            }
        }
        ScissorUtil.a(matrices);
    }

    private void a(DrawContext context, Vector4f frame, float settingsX, float y, int mouseX, int mouseY, float delta) {
        String str;
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.h().d().i();
        Draw3DProcessor draw3d = Delta.h().d().j();
        ThemeProcessor theme = Delta.h().d().o();
        float width = ((frame.x + frame.z) - 8.0f) - settingsX;
        if (this.h == null) {
            return;
        }
        float rowX = settingsX + 8.0f;
        float rowW = width - 16.0f;
        float center = y + 10.0f;
        this.h.j().a(this.h.l());
        this.h.j().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
        float active = this.h.j().c();
        this.g.set(((rowX + rowW) - 6.0f) - 14.0f, center - 4.25f, 14.0f, 8.5f);
        this.d.set((this.g.x - 4.0f) - 10.0f, center - 5.0f, 10.0f, 10.0f);
        float nameX = rowX + 20.0f;
        draw3d.a(context, this.h.a(), rowX, center - 6.0f, 0, 0.35f + (0.65f * active), 0.75f, false);
        Fonts.c.c(matrices, this.h.b(), nameX, (center - (Fonts.c.a(7.5f) / 2.0f)) - 0.75f, 7.5f, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active), (this.d.x - 6.0f) - nameX);
        this.c.a(MathUtil.a(mouseX, mouseY, this.d.x, this.d.y, this.d.z, this.d.w));
        this.c.a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
        float searchValue = this.c.c();
        Fonts.a.a(matrices, "G", this.d.x + ((this.d.z - Fonts.a.b("G", 8.0f + searchValue)) / 2.0f), this.d.y + ((this.d.w - Fonts.a.a(8.0f + searchValue)) / 2.0f), 8.0f + searchValue, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active));
        draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, 3.25f, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), active));
        draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, 3.25f, 0.3f, theme.a(ThemeInfo.OUTLINE_SMALL).a());
        draw.a(matrices, this.g.x + 1.5f + (5.5f * active), this.g.y + 1.5f, 5.5f, 5.5f, 1.75f, ColorUtil.a(ColorUtil.a(InterfaceC0020Opcode.ap, InterfaceC0020Opcode.ap, InterfaceC0020Opcode.bk, 255), ColorUtil.a(255, 255, 255, 255), active));
        this.f.set((rowX + rowW) - 14.0f, y + 20.0f + 8.0f, 14.0f, 14.0f);
        this.e.set((this.f.x - 4.0f) - 14.0f, y + 20.0f + 8.0f, 14.0f, 14.0f);
        String digits = this.b.g().toString().replace(",", "");
        if (digits.isEmpty()) {
            str = "";
        } else {
            Locale locale = Locale.US;
            Object[] objArr = new Object[1];
            objArr[0] = Long.valueOf(Long.parseLong(digits.length() > 15 ? digits.substring(0, 15) : digits));
            str = String.format(locale, "%,d", objArr);
        }
        String formatted = str;
        if (!formatted.contentEquals(this.b.g())) {
            this.b.g().setLength(0);
            this.b.g().append(formatted);
            this.b.a(this.b.j());
        }
        float fieldWidth = (this.e.x - 4.0f) - rowX;
        this.b.b(new Vector2f(fieldWidth, 14.0f));
        this.b.a(new Vector2f(rowX, y + 20.0f + 8.0f));
        this.b.a(context, mouseX, mouseY, delta, 1.0f);
        this.h.a(digits.isEmpty() ? 0.0d : Double.parseDouble(digits));
        boolean saveHover = MathUtil.a(mouseX, mouseY, this.e.x, this.e.y, this.e.z, this.e.w);
        boolean loadHover = MathUtil.a(mouseX, mouseY, this.f.x, this.f.y, this.f.z, this.f.w);
        draw.a(matrices, this.e.x, this.e.y, this.e.z, this.e.w, 4.0f, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), (10.0f + (20.0f * (saveHover ? 1.0f : 0.0f))) / 255.0f));
        draw.a(matrices, this.e.x, this.e.y, this.e.z, this.e.w, 4.0f, 0.5f, theme.a(ThemeInfo.OUTLINE_SMALL).a());
        Fonts.a.a(matrices, "N", this.e.x + ((14.0f - Fonts.a.b("N", 8.0f)) / 2.0f), this.e.y + ((14.0f - Fonts.a.a(8.0f)) / 2.0f), 8.0f, theme.a(ThemeInfo.TEXT).a());
        draw.a(matrices, this.f.x, this.f.y, this.f.z, this.f.w, 4.0f, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), (10.0f + (20.0f * (loadHover ? 1.0f : 0.0f))) / 255.0f));
        draw.a(matrices, this.f.x, this.f.y, this.f.z, this.f.w, 4.0f, 0.5f, theme.a(ThemeInfo.OUTLINE_SMALL).a());
        Fonts.a.a(matrices, "A", this.f.x + ((14.0f - Fonts.a.b("A", 8.0f)) / 2.0f), this.f.y + ((14.0f - Fonts.a.a(8.0f)) / 2.0f), 8.0f, theme.a(ThemeInfo.TEXT).a());
    }

    @Override
    public boolean a(double mouseX, double mouseY, int button) {
        if (button == 0 && MathUtil.a(mouseX, mouseY, this.e.x, this.e.y, this.e.z, this.e.w)) {
            Delta.h().d().q().unSetup();
            ChatUtil.sendMessage("Конфигурация авто-закупки успешно сохранена");
            return true;
        }
        if (button == 0 && MathUtil.a(mouseX, mouseY, this.f.x, this.f.y, this.f.z, this.f.w)) {
            Delta.h().d().q().setup();
            ChatUtil.sendMessage("Конфигурация авто-закупки успешно загружена");
            a(this.h);
            return true;
        }
        if (this.h != null && MathUtil.a(mouseX, mouseY, this.d.x, this.d.y, this.d.z, this.d.w)) {
            mc.player.networkHandler.sendChatCommand("ah search " + this.h.b().replaceAll("\\[\\d+x\\d+]", "").replace("⚡", "").replace("xxx", "").replace("[", "").replace("]", "").replace("★", "").trim().replaceAll("\\s+", StringUtils.a));
            return true;
        }
        if (this.h != null && MathUtil.a(mouseX, mouseY, this.g.x, this.g.y, this.g.z, this.g.w)) {
            this.h.a(!this.h.l());
            return true;
        }
        if (this.h != null) {
            this.b.a(mouseX, mouseY, button);
            if (this.b.j()) {
                return true;
            }
        }
        if (this.i != null && this.i != this.h) {
            a(this.i);
            return true;
        }
        return false;
    }

    @Override
    public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.b.b(mouseX, mouseY, button);
        return this.b.j();
    }

    @Override
    public boolean a(int keyCode, int scanCode, int modifiers) {
        if (this.b.j()) {
            this.b.a(keyCode, scanCode, modifiers);
            return true;
        }
        return false;
    }

    @Override
    public boolean a(char chr, int modifiers) {
        if (this.b.j()) {
            this.b.a(chr, modifiers);
            return true;
        }
        return false;
    }

    @Override
    public float a(Vector4f frame) {
        return this.j;
    }
}
