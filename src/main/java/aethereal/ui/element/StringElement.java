package aethereal.ui.element;


import aethereal.setting.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;

public class StringElement extends Element_2<StringSetting> {
    private TextField d;

    public StringElement(StringSetting setting) {
        super(setting);
        this.a.w = 12.0f;
    }

    @Override

    public boolean a(double mouseX, double mouseY, int button) {
        TextField textFieldG = g();
        if (textFieldG != null) {
            textFieldG.a(mouseX, mouseY, button);
            TextField textFieldG2 = g();
            if (textFieldG2 != null) {
                return textFieldG2.j();
            }
        }
        throw new NullPointerException();
    }

    @Override

    public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.d == null) {
            return false;
        }
        this.d.b(mouseX, mouseY, button);
        return this.d.j();
    }

    @Override

    public boolean a(int keyCode, int scanCode, int modifiers) {
        if (this.d == null || !this.d.j()) {
            return false;
        }
        this.d.a(keyCode, scanCode, modifiers);
        return true;
    }

    @Override

    public boolean a(char chr, int modifiers) {
        if (this.d == null) {
            return false;
        }
        TextField textField = this.d;
        if (textField == null) {
            throw new NullPointerException();
        }
        if (!textField.j()) {
            return false;
        }
        TextField textField2 = this.d;
        if (textField2 == null) {
            throw new NullPointerException();
        }
        textField2.a(chr, modifiers);
        return true;
    }

    private TextField g() {
        if (this.d == null) {
            this.d = new TextField(TextField.a.GUI_SETTING, this.b.k());
            this.d.a(this.b.i());
            this.d.g().append(this.b.c());
        }
        return this.d;
    }

    @Override
    public void a(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        TextField field = g();
        field.b(new Vector2f(this.a.z, this.a.w));
        field.a(new Vector2f(this.a.x, this.a.y));
        field.a(context, mouseX, mouseY, delta, extend);
        this.b.a(field.g().toString());
    }
}
