package aethereal.setting;

import aethereal.core.Action;
import aethereal.ui.element.BindElement;
import aethereal.ui.element.Element;

public class BindSetting extends Setting<Integer> {
    private final int bindType;
    private Action pressAction;
    private Action releaseAction;

    public BindSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
        this.bindType = 1;
    }

    public BindSetting(String name, Integer defaultVal, int type) {
        super(name, defaultVal);
        this.bindType = type;
    }

    public Action k() {
        return this.pressAction;
    }

    public Action l() {
        return this.releaseAction;
    }

    public int m() {
        return this.bindType;
    }

    public BindSetting a(Action action) {
        this.pressAction = action;
        return this;
    }

    public BindSetting b(Action release) {
        this.releaseAction = release;
        return this;
    }

    @Override
    public Element<?> createBooleanElement() {
        return new BindElement(this);
    }
}
