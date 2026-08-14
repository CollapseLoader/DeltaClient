package aethereal.setting;

import aethereal.core.Action;
import aethereal.ui.element.ButtonElement;
import aethereal.ui.element.Element;

public class ButtonSetting extends Setting<Boolean> {
    private final Action action;

    public ButtonSetting(String name, Action action) {
        super(name, false);
        this.action = action;
    }

    public void k() {
        if (this.action != null) {
            this.action.execute();
        }
    }

    @Override
    public Element<?> createBooleanElement() {
        return new ButtonElement(this);
    }
}
