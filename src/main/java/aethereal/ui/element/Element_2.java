package aethereal.ui.element;


import aethereal.api.Compile;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Font;
import aethereal.setting.Setting;
import lombok.Generated;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class Element_2<SettingType extends Setting<?>> {
    protected final Vector4f a = new Vector4f();
    protected final SettingType b;
    private final AnimationUtil d = new AnimationUtil();
    private final AnimationUtil e = new AnimationUtil();
    protected float c;

    public Element_2(SettingType setting) {
        this.b = setting;
    }

    @Compile
    public boolean a(double mouseX, double mouseY, int button) {
        return false;
    }

    @Compile
    public boolean b(double mouseX, double mouseY, int button) {
        return false;
    }

    @Compile
    public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    @Compile
    public boolean a(double mouseX, double mouseY, double amount) {
        return false;
    }

    @Compile
    public boolean a(char chr, int modifiers) {
        return false;
    }

    @Compile
    public boolean a(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Generated
    public void a(float scroll) {
        this.c = scroll;
    }

    @Generated
    public AnimationUtil b() {
        return this.d;
    }

    @Generated
    public AnimationUtil c() {
        return this.e;
    }

    @Generated
    public Vector4f d() {
        return this.a;
    }

    @Generated
    public SettingType e() {
        return this.b;
    }

    @Generated
    public float f() {
        return this.c;
    }

    public boolean a() {
        return this.b.e().get().booleanValue();
    }

    public void a(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
    }

    protected void a(MatrixStack matrixStack, Font font, String text, float x, float y, float height, float size, int color, float maxWidth, boolean hovered, float extend, float delta) {
        this.c = font.a(matrixStack, text, x, (y + ((height - font.a(size)) / 2.0f)) - 0.5f, size, ColorUtil.a(color, extend), maxWidth, hovered, this.c, delta);
    }

    public void a(DrawContext context, double mouseX, double mouseY, float delta) {
    }

    public void a(DrawEvent event, float x, float y, float width, float animation) {
    }
}
