package aethereal.ui.element;


import aethereal.setting.StringSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;

public class StringElement extends Element<StringSetting> {
    private TextField textField;

    public StringElement(StringSetting setting) {
        super(setting);
        this.a.w = 12.0f;
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        TextField textFieldG = g();
        if (textFieldG != null) {
            textFieldG.onMouseClick(mouseX, mouseY, button);
            TextField textFieldG2 = g();
            if (textFieldG2 != null) {
                return textFieldG2.isFocused();
            }
        }
        throw new NullPointerException();
    }

    @Override

    public boolean onMouseDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.textField == null) {
            return false;
        }
        this.textField.onMouseDrag(mouseX, mouseY, button);
        return this.textField.isFocused();
    }

    @Override

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (this.textField == null || !this.textField.isFocused()) {
            return false;
        }
        this.textField.a(keyCode, scanCode, modifiers);
        return true;
    }

    @Override

    public boolean onCharTyped(char chr, int modifiers) {
        if (this.textField == null) {
            return false;
        }
        TextField textField = this.textField;
        if (textField == null) {
            throw new NullPointerException();
        }
        if (!textField.isFocused()) {
            return false;
        }
        TextField textField2 = this.textField;
        if (textField2 == null) {
            throw new NullPointerException();
        }
        textField2.a(chr, modifiers);
        return true;
    }

    private TextField g() {
        if (this.textField == null) {
            this.textField = new TextField(TextField.type.GUI_SETTING, this.b.k());
            this.textField.setPlaceholder(this.b.i());
            this.textField.getTextBuffer().append(this.b.c());
        }
        return this.textField;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        TextField field = g();
        field.setSize(new Vector2f(this.a.z, this.a.w));
        field.setPosition(new Vector2f(this.a.x, this.a.y));
        field.render(context, mouseX, mouseY, delta, extend);
        this.b.a(field.getTextBuffer().toString());
    }
}
