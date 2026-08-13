package aethereal.setting;

import aethereal.ui.element.Element_2;
import lombok.Generated;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class Setting<Value> {
    private final Value c;
    private final String e;
    private Value d;
    private java.util.function.Supplier<Boolean> a = () -> {
        return true;
    };
    private Consumer<Value> b = value -> {
    };
    private boolean f = true;

    public Setting(String name, Value defaultValue) {
        this.e = name;
        this.c = defaultValue;
        this.d = defaultValue;
    }

    public abstract Element_2<?> d();

    @Generated
    public java.util.function.Supplier<Boolean> e() {
        return this.a;
    }

    @Generated
    public Consumer<Value> f() {
        return this.b;
    }

    @Generated
    public Value g() {
        return this.c;
    }

    @Generated
    public Value h() {
        return this.d;
    }

    @Generated
    public String i() {
        return this.e;
    }

    @Generated
    public boolean j() {
        return this.f;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a() {
        this.f = false;
        return (T) this;
    }

    public Setting<Value> a(Value newValue) {
        if (Objects.equals(this.d, newValue)) {
            return this;
        }
        this.d = newValue;
        this.b.accept(newValue);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a(Consumer<Value> onChange) {
        this.b = onChange;
        return (T) this;
    }

    public void b() {
        this.d = this.c;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<Value>> T a(java.util.function.Supplier<Boolean> bool) {
        this.a = bool;
        return (T) this;
    }

    public Value c() {
        return this.d;
    }
}
