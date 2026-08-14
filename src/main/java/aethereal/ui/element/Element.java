package aethereal.ui.element;


import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Font;
import aethereal.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class Element<SettingType extends Setting<?>> {
    protected final Vector4f a = new Vector4f();
    protected final SettingType b;
    private final AnimationUtil d = new AnimationUtil();
    private final AnimationUtil e = new AnimationUtil();
    protected float c;

    public Element(SettingType setting) {
        this.b = setting;
    }


    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        return false;
    }


    public boolean onMouseRelease(double mouseX, double mouseY, int button) {
        return false;
    }


    public boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }


    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        return false;
    }


    public boolean onCharTyped(char chr, int modifiers) {
        return false;
    }


    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public AnimationUtil getActivationAnimation() {
        return this.d;
    }

    public AnimationUtil getVisibilityAnimation() {
        return this.e;
    }

    public Vector4f getBounds() {
        return this.a;
    }

    public SettingType getSetting() {
        return this.b;
    }

    public float getScroll() {
        return this.c;
    }

    public void setScroll(float scroll) {
        this.c = scroll;
    }

    public boolean isEnabled() {
        return this.b.e().get().booleanValue();
    }

    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
    }

    protected void drawLabel(MatrixStack matrixStack, Font font, String text, float x, float y, float height, float size, int color, float maxWidth, boolean hovered, float extend, float delta) {
        this.c = font.a(matrixStack, text, x, (y + ((height - font.a(size)) / 2.0f)) - 0.5f, size, ColorUtil.applyAlphaToColor(color, extend), maxWidth, hovered, this.c, delta);
    }

    public void renderColorPicker(DrawContext context, double mouseX, double mouseY, float delta) {
    }

    public void onDrawEvent(DrawEvent event, float x, float y, float width, float animation) {
    }
}
