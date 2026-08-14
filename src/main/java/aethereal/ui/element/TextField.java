package aethereal.ui.element;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Font;
import aethereal.render.Fonts;
import aethereal.util.CursorUtil;
import aethereal.util.MathUtil;
import lombok.Generated;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class TextField {
    private final a a;
    private final boolean b;
    private final StringBuilder f;
    private Vector2f c;
    private Vector2f d;
    private String e;
    private Vector2f g;
    private int h;
    private boolean i;
    private boolean j;
    private float k;

    public TextField(a type) {
        this(type, false);
    }

    public TextField(a type, boolean numbers) {
        this.c = new Vector2f(0.0f, 0.0f);
        this.d = new Vector2f(0.0f, 0.0f);
        this.e = "";
        this.f = new StringBuilder();
        this.g = new Vector2f(0.0f, 0.0f);
        this.a = type;
        this.b = numbers;
    }

    @Generated
    public a b() {
        return this.a;
    }

    @Generated
    public boolean c() {
        return this.b;
    }

    @Generated
    public void a(Vector2f position) {
        this.c = position;
    }

    @Generated
    public Vector2f d() {
        return this.c;
    }

    @Generated
    public void b(Vector2f size) {
        this.d = size;
    }

    @Generated
    public Vector2f e() {
        return this.d;
    }

    @Generated
    public void a(String placeholder) {
        this.e = placeholder;
    }

    @Generated
    public String f() {
        return this.e;
    }

    @Generated
    public StringBuilder g() {
        return this.f;
    }

    @Generated
    public Vector2f h() {
        return this.g;
    }

    @Generated
    public int i() {
        return this.h;
    }

    @Generated
    public boolean j() {
        return this.i;
    }

    @Generated
    public boolean k() {
        return this.j;
    }

    @Generated
    public float l() {
        return this.k;
    }

    public void a(DrawContext context, double mouseX, double mouseY, float delta, float alpha) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.h().d().i();
        float lineHeight = this.a.d.d().lineHeight() * this.a.e;
        float textX = this.c.getX() + this.a.f;
        float textY = this.c.getY() + ((this.d.getY() - lineHeight) / 2.0f) + this.a.g;
        float visibleWidth = this.d.getX() - (this.a.f * 2.0f);
        a(visibleWidth);
        boolean hover = MathUtil.a(mouseX, mouseY, this.c.getX(), this.c.getY(), this.d.getX(), this.d.getY());
        if (hover != this.j) {
            this.j = hover;
            CursorUtil.a(hover ? CursorUtil.a.TEXT : CursorUtil.a.DEFAULT);
        }
        boolean placeholding = !this.i && this.f.isEmpty() && this.a.h;
        String content = placeholding ? this.e : this.f.toString();
        int color = ColorUtil.a(Delta.h().d().o().a(placeholding ? ThemeInfo.TEXT_DISABLED : ThemeInfo.TEXT).a(), alpha);
        this.a.a(draw, matrices, this.c.getX(), this.c.getY(), this.d.getX(), this.d.getY(), alpha);
        a(context, textX, textY, lineHeight, visibleWidth, alpha);
        b(context, textX, textY, lineHeight, visibleWidth, alpha);
        this.a.d.c(matrices, b(content), textX, textY, this.a.e, color, visibleWidth);
    }

    private void a(DrawContext context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
        if (this.i && this.g.getX() != this.g.getY()) {
            int from = (int) Math.min(this.g.getX(), this.g.getY());
            int to = (int) Math.max(this.g.getX(), this.g.getY());
            float start = Math.max((textX + a(from)) - this.k, textX);
            float end = Math.min((textX + a(to)) - this.k, textX + visibleWidth);
            if (start < end) {
                Draw2DProcessor draw = Delta.h().d().i();
                draw.a(context, start, textY, end - start, lineHeight, ColorUtil.a(Delta.h().d().o().a(ThemeInfo.PRIMARY).a(), 0.47f * alpha));
            }
        }
    }

    private void b(DrawContext context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
        if (this.i) {
            float caretX = (textX + a((int) this.g.getY())) - this.k;
            if (caretX >= textX && caretX <= textX + visibleWidth) {
                float blink = (float) ((Math.sin(System.currentTimeMillis() / 150.0d) * 0.5d) + 0.5d);
                float caretHeight = this.a.e / 1.01f;
                float caretY = textY + ((lineHeight - caretHeight) / 2.0f);
                Draw2DProcessor draw = Delta.h().d().i();
                draw.a(context, caretX, caretY, 0.5f, caretHeight, ColorUtil.a(Delta.h().d().o().a(ThemeInfo.TEXT).a(), blink * alpha));
            }
        }
    }

    private float a(int index) {
        return this.a.d.a(this.f.substring(0, Math.min(index, this.f.length())), this.a.e) + 0.5f;
    }

    private String b(String content) {
        if (content == null || content.isEmpty()) {
            return content == null ? "" : content;
        }
        for (int i = 0; i < content.length(); i++) {
            if (this.a.d.a(content.substring(0, i), this.a.e) >= this.k) {
                return content.substring(i);
            }
        }
        return "";
    }

    private void a(float visibleWidth) {
        float cursor = a((int) this.g.getY());
        if (cursor - this.k > visibleWidth) {
            this.k = cursor - visibleWidth;
        } else if (cursor < this.k) {
            this.k = cursor;
        }
        if (this.a.d.a(this.f.toString(), this.a.e) < visibleWidth) {
            this.k = 0.0f;
        }
    }

    public void a(double mouseX, double mouseY, int button) {
        if (MathUtil.a(mouseX, mouseY, this.c.getX(), this.c.getY(), this.d.getX(), this.d.getY())) {
            this.i = true;
            if (button == 0) {
                int cursor = b((float) mouseX);
                this.h = cursor;
                this.g = new Vector2f(cursor, cursor);
                return;
            }
            return;
        }
        if (this.i) {
            a(false);
        }
    }

    public void b(double mouseX, double mouseY, int button) {
        if (this.i && button == 0) {
            int cursor = b((float) mouseX);
            this.g = new Vector2f(Math.min(this.h, cursor), Math.max(this.h, cursor));
        }
    }

    private int b(float mouseX) {
        float textX = this.c.getX() + this.a.f;
        float adjusted = mouseX + this.k;
        for (int i = 0; i <= this.f.length(); i++) {
            if (textX + a(i) > adjusted) {
                return i;
            }
        }
        return this.f.length();
    }

    public void a(int keyCode, int scanCode, int modifiers) {
        if (this.i) {
            boolean ctrl = (modifiers & 2) != 0;
            boolean hasSelection = this.g.getX() != this.g.getY();
            if (ctrl && keyCode == 65) {
                this.g = new Vector2f(0.0f, this.f.length());
                return;
            }
            if (ctrl && keyCode == 67) {
                m();
                return;
            }
            if (ctrl && keyCode == 86) {
                n();
                return;
            }
            if (keyCode == 259) {
                b(hasSelection);
                return;
            }
            if (keyCode == 261) {
                c(hasSelection);
                return;
            }
            if (keyCode == 263) {
                b(-1);
                return;
            }
            if (keyCode == 262) {
                b(1);
            } else if (keyCode == 257 || keyCode == 256) {
                a(false);
            }
        }
    }

    public void a(char chr, int modifiers) {
        if (this.i) {
            if (!this.b || (chr >= '0' && chr <= '9')) {
                c(String.valueOf(chr));
            }
        }
    }

    private void c(String string) {
        if (this.g.getX() != this.g.getY()) {
            o();
        }
        int pos = (int) this.g.getY();
        this.f.insert(pos, string);
        this.g = new Vector2f(pos + string.length(), pos + string.length());
    }

    private void m() {
        int from = (int) Math.min(this.g.getX(), this.g.getY());
        int to = (int) Math.max(this.g.getX(), this.g.getY());
        if (from < to) {
            GLFW.glfwSetClipboardString(Interface.mc.getWindow().getHandle(), this.f.substring(from, to));
        }
    }

    private void n() {
        String clip = GLFW.glfwGetClipboardString(Interface.mc.getWindow().getHandle());
        if (clip != null && !clip.isEmpty()) {
            c(this.b ? clip.replaceAll("[^0-9]", "") : clip);
        }
    }

    private void b(boolean hasSelection) {
        if (hasSelection) {
            o();
        } else if (this.g.getY() > 0.0f) {
            int pos = (int) this.g.getY();
            this.f.deleteCharAt(pos - 1);
            this.g = new Vector2f(pos - 1, pos - 1);
        }
    }

    private void c(boolean hasSelection) {
        if (hasSelection) {
            o();
        } else if (this.g.getY() < this.f.length()) {
            this.f.deleteCharAt((int) this.g.getY());
        }
    }

    private void b(int direction) {
        int pos = ((int) this.g.getY()) + direction;
        if (pos >= 0 && pos <= this.f.length()) {
            this.g = new Vector2f(pos, pos);
        }
    }

    private void o() {
        int from = (int) Math.min(this.g.getX(), this.g.getY());
        int to = (int) Math.max(this.g.getX(), this.g.getY());
        this.f.delete(from, to);
        this.g = new Vector2f(from, from);
    }

    public void a() {
        this.f.setLength(0);
        this.g = new Vector2f(0.0f, 0.0f);
        this.i = false;
    }

    public void a(boolean status) {
        this.i = status;
        this.g = new Vector2f(status ? this.f.length() : 0.0f, status ? this.f.length() : 0.0f);
    }

    public enum a {
        ALT_MANAGER(Fonts.b, 7.0f, 6.0f, -0.5f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                draw.a(matrices, x, y, width + 2.0f, height, new Vector4f(5.0f, 1.0f, 5.0f, 1.0f), ColorUtil.a(16777215, 0.039215688f * alpha));
            }
        },
        GUI(Fonts.c, 7.0f, 6.0f, 0.0f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                ThemeProcessor theme = Delta.h().d().o();
                int background = ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), theme.a(ThemeInfo.PRIMARY).a(), 0.05f), 0.78431374f * alpha);
                draw.a(matrices, x, y, width, height, 6.0f, background, alpha, background, 2.0f);
                draw.a(matrices, x, y, width, height, 6.0f, 0.5f, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * alpha));
            }
        },
        GUI_SETTING(Fonts.c, 6.5f, 4.0f, 0.0f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                ThemeProcessor theme = Delta.h().d().o();
                draw.a(matrices, x, y, width, height, 2.0f, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), 0.011764706f * alpha));
                draw.a(matrices, x, y, width, height, 2.0f, 0.5f, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b() * alpha));
            }
        };

        final Font d;
        final float e;
        final float f;
        final float g;
        final boolean h;

        @Generated
        a(final Font font, final float fontSize, final float paddingX, final float textOffset, final boolean placeholder) {
            this.d = font;
            this.e = fontSize;
            this.f = paddingX;
            this.g = textOffset;
            this.h = placeholder;
        }

        public abstract void a(Draw2DProcessor draw2DProcessor, MatrixStack class_4587Var, float f, float f2, float f3, float f4, float f5);

        @Generated
        public Font a() {
            return this.d;
        }

        @Generated
        public float b() {
            return this.e;
        }

        @Generated
        public float c() {
            return this.f;
        }

        @Generated
        public float d() {
            return this.g;
        }

        @Generated
        public boolean e() {
            return this.h;
        }
    }
}
