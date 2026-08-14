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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class TextField {
    private final type a;
    private final boolean numbersOnly;
    private final StringBuilder textBuffer;
    private Vector2f position;
    private Vector2f size;
    private String placeholder;
    private Vector2f selectionRange;
    private int cursorIndex;
    private boolean focused;
    private boolean hovering;
    private float scrollOffset;

    public TextField(type type) {
        this(type, false);
    }

    public TextField(type type, boolean numbers) {
        this.position = new Vector2f(0.0f, 0.0f);
        this.size = new Vector2f(0.0f, 0.0f);
        this.placeholder = "";
        this.textBuffer = new StringBuilder();
        this.selectionRange = new Vector2f(0.0f, 0.0f);
        this.a = type;
        this.numbersOnly = numbers;
    }

    public type getType() {
        return this.a;
    }

    public boolean isNumbersOnly() {
        return this.numbersOnly;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getSize() {
        return this.size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public StringBuilder getTextBuffer() {
        return this.textBuffer;
    }

    public Vector2f getSelectionRange() {
        return this.selectionRange;
    }

    public int getCursorIndex() {
        return this.cursorIndex;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public boolean isHovering() {
        return this.hovering;
    }

    public float getScrollOffset() {
        return this.scrollOffset;
    }

    public void render(DrawContext context, double mouseX, double mouseY, float delta, float alpha) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        float lineHeight = this.a.d.d().lineHeight() * this.a.e;
        float textX = this.position.getX() + this.a.f;
        float textY = this.position.getY() + ((this.size.getY() - lineHeight) / 2.0f) + this.a.g;
        float visibleWidth = this.size.getX() - (this.a.f * 2.0f);
        updateScrollOffset(visibleWidth);
        boolean hover = MathUtil.a(mouseX, mouseY, this.position.getX(), this.position.getY(), this.size.getX(), this.size.getY());
        if (hover != this.hovering) {
            this.hovering = hover;
            CursorUtil.a(hover ? CursorUtil.a.TEXT : CursorUtil.a.DEFAULT);
        }
        boolean placeholding = !this.focused && this.textBuffer.isEmpty() && this.a.h;
        String content = placeholding ? this.placeholder : this.textBuffer.toString();
        int color = ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(placeholding ? ThemeInfo.TEXT_DISABLED : ThemeInfo.TEXT).toIntColor(), alpha);
        this.a.a(draw, matrices, this.position.getX(), this.position.getY(), this.size.getX(), this.size.getY(), alpha);
        drawSelectionHighlight(context, textX, textY, lineHeight, visibleWidth, alpha);
        drawCaret(context, textX, textY, lineHeight, visibleWidth, alpha);
        this.a.d.c(matrices, trimToVisibleContent(content), textX, textY, this.a.e, color, visibleWidth);
    }

    private void drawSelectionHighlight(DrawContext context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
        if (this.focused && this.selectionRange.getX() != this.selectionRange.getY()) {
            int from = (int) Math.min(this.selectionRange.getX(), this.selectionRange.getY());
            int to = (int) Math.max(this.selectionRange.getX(), this.selectionRange.getY());
            float start = Math.max((textX + getCharWidth(from)) - this.scrollOffset, textX);
            float end = Math.min((textX + getCharWidth(to)) - this.scrollOffset, textX + visibleWidth);
            if (start < end) {
                Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
                draw.a(context, start, textY, end - start, lineHeight, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), 0.47f * alpha));
            }
        }
    }

    private void drawCaret(DrawContext context, float textX, float textY, float lineHeight, float visibleWidth, float alpha) {
        if (this.focused) {
            float caretX = (textX + getCharWidth((int) this.selectionRange.getY())) - this.scrollOffset;
            if (caretX >= textX && caretX <= textX + visibleWidth) {
                float blink = (float) ((Math.sin(System.currentTimeMillis() / 150.0d) * 0.5d) + 0.5d);
                float caretHeight = this.a.e / 1.01f;
                float caretY = textY + ((lineHeight - caretHeight) / 2.0f);
                Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
                draw.a(context, caretX, caretY, 0.5f, caretHeight, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.TEXT).toIntColor(), blink * alpha));
            }
        }
    }

    private float getCharWidth(int index) {
        return this.a.d.a(this.textBuffer.substring(0, Math.min(index, this.textBuffer.length())), this.a.e) + 0.5f;
    }

    private String trimToVisibleContent(String content) {
        if (content == null || content.isEmpty()) {
            return content == null ? "" : content;
        }
        for (int i = 0; i < content.length(); i++) {
            if (this.a.d.a(content.substring(0, i), this.a.e) >= this.scrollOffset) {
                return content.substring(i);
            }
        }
        return "";
    }

    private void updateScrollOffset(float visibleWidth) {
        float cursor = getCharWidth((int) this.selectionRange.getY());
        if (cursor - this.scrollOffset > visibleWidth) {
            this.scrollOffset = cursor - visibleWidth;
        } else if (cursor < this.scrollOffset) {
            this.scrollOffset = cursor;
        }
        if (this.a.d.a(this.textBuffer.toString(), this.a.e) < visibleWidth) {
            this.scrollOffset = 0.0f;
        }
    }

    public void onMouseClick(double mouseX, double mouseY, int button) {
        if (MathUtil.a(mouseX, mouseY, this.position.getX(), this.position.getY(), this.size.getX(), this.size.getY())) {
            this.focused = true;
            if (button == 0) {
                int cursor = getCharIndexAtPosition((float) mouseX);
                this.cursorIndex = cursor;
                this.selectionRange = new Vector2f(cursor, cursor);
                return;
            }
            return;
        }
        if (this.focused) {
            a(false);
        }
    }

    public void onMouseDrag(double mouseX, double mouseY, int button) {
        if (this.focused && button == 0) {
            int cursor = getCharIndexAtPosition((float) mouseX);
            this.selectionRange = new Vector2f(Math.min(this.cursorIndex, cursor), Math.max(this.cursorIndex, cursor));
        }
    }

    private int getCharIndexAtPosition(float mouseX) {
        float textX = this.position.getX() + this.a.f;
        float adjusted = mouseX + this.scrollOffset;
        for (int i = 0; i <= this.textBuffer.length(); i++) {
            if (textX + getCharWidth(i) > adjusted) {
                return i;
            }
        }
        return this.textBuffer.length();
    }

    public void a(int keyCode, int scanCode, int modifiers) {
        if (this.focused) {
            boolean ctrl = (modifiers & 2) != 0;
            boolean hasSelection = this.selectionRange.getX() != this.selectionRange.getY();
            if (ctrl && keyCode == 65) {
                this.selectionRange = new Vector2f(0.0f, this.textBuffer.length());
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
        if (this.focused) {
            if (!this.numbersOnly || (chr >= '0' && chr <= '9')) {
                c(String.valueOf(chr));
            }
        }
    }

    private void c(String string) {
        if (this.selectionRange.getX() != this.selectionRange.getY()) {
            o();
        }
        int pos = (int) this.selectionRange.getY();
        this.textBuffer.insert(pos, string);
        this.selectionRange = new Vector2f(pos + string.length(), pos + string.length());
    }

    private void m() {
        int from = (int) Math.min(this.selectionRange.getX(), this.selectionRange.getY());
        int to = (int) Math.max(this.selectionRange.getX(), this.selectionRange.getY());
        if (from < to) {
            GLFW.glfwSetClipboardString(Interface.mc.getWindow().getHandle(), this.textBuffer.substring(from, to));
        }
    }

    private void n() {
        String clip = GLFW.glfwGetClipboardString(Interface.mc.getWindow().getHandle());
        if (clip != null && !clip.isEmpty()) {
            c(this.numbersOnly ? clip.replaceAll("[^0-9]", "") : clip);
        }
    }

    private void b(boolean hasSelection) {
        if (hasSelection) {
            o();
        } else if (this.selectionRange.getY() > 0.0f) {
            int pos = (int) this.selectionRange.getY();
            this.textBuffer.deleteCharAt(pos - 1);
            this.selectionRange = new Vector2f(pos - 1, pos - 1);
        }
    }

    private void c(boolean hasSelection) {
        if (hasSelection) {
            o();
        } else if (this.selectionRange.getY() < this.textBuffer.length()) {
            this.textBuffer.deleteCharAt((int) this.selectionRange.getY());
        }
    }

    private void b(int direction) {
        int pos = ((int) this.selectionRange.getY()) + direction;
        if (pos >= 0 && pos <= this.textBuffer.length()) {
            this.selectionRange = new Vector2f(pos, pos);
        }
    }

    private void o() {
        int from = (int) Math.min(this.selectionRange.getX(), this.selectionRange.getY());
        int to = (int) Math.max(this.selectionRange.getX(), this.selectionRange.getY());
        this.textBuffer.delete(from, to);
        this.selectionRange = new Vector2f(from, from);
    }

    public void a() {
        this.textBuffer.setLength(0);
        this.selectionRange = new Vector2f(0.0f, 0.0f);
        this.focused = false;
    }

    public void a(boolean status) {
        this.focused = status;
        this.selectionRange = new Vector2f(status ? this.textBuffer.length() : 0.0f, status ? this.textBuffer.length() : 0.0f);
    }

    public enum type {
        ALT_MANAGER(Fonts.b, 7.0f, 6.0f, -0.5f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                draw.a(matrices, x, y, width + 2.0f, height, new Vector4f(5.0f, 1.0f, 5.0f, 1.0f), ColorUtil.applyAlphaToColor(16777215, 0.039215688f * alpha));
            }
        },
        GUI(Fonts.c, 7.0f, 6.0f, 0.0f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
                int background = ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.05f), 0.78431374f * alpha);
                draw.a(matrices, x, y, width, height, 6.0f, background, alpha, background, 2.0f);
                draw.a(matrices, x, y, width, height, 6.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * alpha));
            }
        },
        GUI_SETTING(Fonts.c, 6.5f, 4.0f, 0.0f, true) {
            @Override
            public void a(Draw2DProcessor draw, MatrixStack matrices, float x, float y, float width, float height, float alpha) {
                ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
                draw.a(matrices, x, y, width, height, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.011764706f * alpha));
                draw.a(matrices, x, y, width, height, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * alpha));
            }
        };

        final Font d;
        final float e;
        final float f;
        final float g;
        final boolean h;

        type(final Font font, final float fontSize, final float paddingX, final float textOffset, final boolean placeholder) {
            this.d = font;
            this.e = fontSize;
            this.f = paddingX;
            this.g = textOffset;
            this.h = placeholder;
        }

        public abstract void a(Draw2DProcessor draw2DProcessor, MatrixStack class_4587Var, float f, float f2, float f3, float f4, float f5);

        public Font a() {
            return this.d;
        }

        public float b() {
            return this.e;
        }

        public float c() {
            return this.f;
        }

        public float d() {
            return this.g;
        }

        public boolean e() {
            return this.h;
        }
    }
}
