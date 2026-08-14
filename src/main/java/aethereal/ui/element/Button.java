package aethereal.ui.element;

import aethereal.core.Delta;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.render.*;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Button {
    private final AnimationUtil a = new AnimationUtil();
    private final float b;
    private final float c;
    private final String d;
    private final Runnable e;
    private float f;
    private float g;

    public Button(float width, float height, String label, Runnable action) {
        this.b = width;
        this.c = height;
        this.d = label;
        this.e = action;
    }

    public AnimationUtil a() {
        return this.a;
    }

    public float b() {
        return this.b;
    }

    public float c() {
        return this.c;
    }

    public String d() {
        return this.d;
    }

    public Runnable e() {
        return this.e;
    }

    public float f() {
        return this.f;
    }

    public float g() {
        return this.g;
    }

    public void a(float x, float y) {
        this.f = x;
        this.g = y;
    }

    public void a(DrawContext context, int mouseX, int mouseY, float delta, float open) {
        this.a.a(this.e != null && MathUtil.a(mouseX, mouseY, this.f, this.g, this.b, this.c));
        this.a.a(0.0f, 1.0f, 0.35f, EasingList.i, delta);
        float hover = Math.min(1.0f, this.a.c() / 0.9f);
        float scale = (0.85f + (0.15f * EasingList.s.ease(open))) * (1.0f + (0.03f * hover));
        MatrixStack matrices = context.getMatrices();
        float cx = this.f + (this.b / 2.0f);
        float cy = this.g + (this.c / 2.0f);
        matrices.push();
        matrices.translate(cx, cy, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-cx, -cy, 0.0f);
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        draw.b(matrices, this.f, this.g, this.b, this.c, 8.0f, ColorUtil.convertToARGB(11, 11, 13, InterfaceC0020Opcode.bN), open);
        draw.a(matrices, this.f, this.g, this.b, this.c, 8.0f, 0.5f, ColorUtil.convertToARGB(255, 255, 255, (int) (hover * 20.0f * open)));
        if (this.d != null) {
            float time = (System.currentTimeMillis() % 3000) / 3000.0f;
            net.minecraft.text.MutableText class_2561VarMethod_43470 = Text.literal("");
            for (int i = 0; i < this.d.length(); i++) {
                float wave = (float) ((Math.sin(((double) (time + ((i * 0.5f) / this.d.length()))) * 3.141592654293742d * 2.0d) * 0.5d) + 0.5d);
                int c = (int) (180.0f + (65.0f * wave * hover));
                class_2561VarMethod_43470.append(Text.literal(String.valueOf(this.d.charAt(i))).setStyle(Style.EMPTY.withColor((c << 16) | (c << 8) | c)));
            }
            float labelW = Fonts.e.a(this.d, 8.0f);
            Fonts.e.a(matrices, class_2561VarMethod_43470, this.f + ((this.b - labelW) / 2.0f), this.g + ((this.c - 9.0f) / 2.0f), 8.0f, 0.0f, open);
        }
        matrices.pop();
    }
}
