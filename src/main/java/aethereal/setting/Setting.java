package aethereal.setting;

import aethereal.ui.element.Element;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class Setting<Value> {
    private final Value defaultValue;
    private final String name;
    private Value currentValue;
    private java.util.function.Supplier<Boolean> visibleSupplier = () -> {
        return true;
    };
    private Consumer<Value> onChange = value -> {
    };
    private boolean enabled = true;

    public Setting(String name, Value defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
    }

    public abstract Element<?> createBooleanElement();

    public java.util.function.Supplier<Boolean> e() {
        return this.visibleSupplier;
    }

    public Consumer<Value> f() {
        return this.onChange;
    }

    public Value g() {
        return this.defaultValue;
    }

    public Value h() {
        return this.currentValue;
    }

    public String i() {
        return this.name;
    }

    public boolean j() {
        return this.enabled;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a() {
        this.enabled = false;
        return (T) this;
    }

    public Setting<Value> a(Value newValue) {
        if (Objects.equals(this.currentValue, newValue)) {
            return this;
        }
        this.currentValue = newValue;
        this.onChange.accept(newValue);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a(Consumer<Value> onChange) {
        this.onChange = onChange;
        return (T) this;
    }

    public void b() {
        this.currentValue = this.defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a(java.util.function.Supplier<Boolean> bool) {
        this.visibleSupplier = bool;
        return (T) this;
    }

    public Value c() {
        return this.currentValue;
    }
}
