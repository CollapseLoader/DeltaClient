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
    private final float width;
    private final float height;
    private final String label;
    private final Runnable action;
    private float x;
    private float y;

    public Button(float width, float height, String label, Runnable action) {
        this.width = width;
        this.height = height;
        this.label = label;
        this.action = action;
    }

    public AnimationUtil getAnimation() {
        return this.a;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public String getLabel() {
        return this.label;
    }

    public Runnable getAction() {
        return this.action;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta, float open) {
        this.a.a(this.action != null && MathUtil.a(mouseX, mouseY, this.x, this.y, this.width, this.height));
        this.a.a(0.0f, 1.0f, 0.35f, EasingList.i, delta);
        float hover = Math.min(1.0f, this.a.c() / 0.9f);
        float scale = (0.85f + (0.15f * EasingList.s.ease(open))) * (1.0f + (0.03f * hover));
        MatrixStack matrices = context.getMatrices();
        float cx = this.x + (this.width / 2.0f);
        float cy = this.y + (this.height / 2.0f);
        matrices.push();
        matrices.translate(cx, cy, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-cx, -cy, 0.0f);
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        draw.b(matrices, this.x, this.y, this.width, this.height, 8.0f, ColorUtil.convertToARGB(11, 11, 13, InterfaceC0020Opcode.bN), open);
        draw.a(matrices, this.x, this.y, this.width, this.height, 8.0f, 0.5f, ColorUtil.convertToARGB(255, 255, 255, (int) (hover * 20.0f * open)));
        if (this.label != null) {
            float time = (System.currentTimeMillis() % 3000) / 3000.0f;
            net.minecraft.text.MutableText class_2561VarMethod_43470 = Text.literal("");
            for (int i = 0; i < this.label.length(); i++) {
                float wave = (float) ((Math.sin(((double) (time + ((i * 0.5f) / this.label.length()))) * 3.141592654293742d * 2.0d) * 0.5d) + 0.5d);
                int c = (int) (180.0f + (65.0f * wave * hover));
                class_2561VarMethod_43470.append(Text.literal(String.valueOf(this.label.charAt(i))).setStyle(Style.EMPTY.withColor((c << 16) | (c << 8) | c)));
            }
            float labelW = Fonts.e.a(this.label, 8.0f);
            Fonts.e.a(matrices, class_2561VarMethod_43470, this.x + ((this.width - labelW) / 2.0f), this.y + ((this.height - 9.0f) / 2.0f), 8.0f, 0.0f, open);
        }
        matrices.pop();
    }
}
