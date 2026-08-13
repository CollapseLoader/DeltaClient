package aethereal.notification;


import aethereal.render.AnimationUtil;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.item.ItemStack;

public class Notification {
    private final AnimationUtil a;
    private final CounterUtil b;
    private final Object c;
    private final Object d;
    private final int e;
    private int f;

    public Notification(Object symbol, int color, Object message, int time) {
        this.a = new AnimationUtil();
        this.b = new CounterUtil();
        if (!(symbol instanceof String) && !(symbol instanceof ItemStack)) {
            throw new IllegalArgumentException("Icon must be either String or ItemStack");
        }
        this.d = symbol;
        this.e = color;
        this.c = message;
        this.f = time;
    }

    public Notification(Object symbol, Object message, int time) {
        this(symbol, -1, message, time);
    }

    @Generated
    public void a(int time) {
        this.f = time;
    }

    @Generated
    public AnimationUtil a() {
        return this.a;
    }

    @Generated
    public CounterUtil b() {
        return this.b;
    }

    @Generated
    public Object c() {
        return this.c;
    }

    @Generated
    public Object d() {
        return this.d;
    }

    @Generated
    public int e() {
        return this.e;
    }

    @Generated
    public int f() {
        return this.f;
    }
}
