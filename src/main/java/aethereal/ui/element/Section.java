package aethereal.ui.element;


import aethereal.render.AnimationUtil;
import net.minecraft.client.gui.DrawContext;
import org.joml.Vector4f;

public class Section {
    private final AnimationUtil a = new AnimationUtil();
    private final String icon;
    private final String name;

    protected Section(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public AnimationUtil a() {
        return this.a;
    }

    public String b() {
        return this.icon;
    }

    public String c() {
        return this.name;
    }

    public void a(DrawContext context, Vector4f content, int mouseX, int mouseY, float scroll, float delta) {
    }

    public float a(Vector4f content) {
        return 0.0f;
    }

    public boolean a(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean b(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public boolean a(double mouseX, double mouseY, double amount) {
        return false;
    }

    public boolean a(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean a(char chr, int modifiers) {
        return false;
    }
}
