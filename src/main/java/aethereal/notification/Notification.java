package aethereal.notification;


import aethereal.render.AnimationUtil;
import aethereal.util.CounterUtil;
import net.minecraft.item.ItemStack;

public class Notification {
    private final AnimationUtil animation;
    private final CounterUtil counter;
    private final Object message;
    private final Object symbol;
    private final int color;
    private int time;

    public Notification(Object symbol, int color, Object message, int time) {
        this.animation = new AnimationUtil();
        this.counter = new CounterUtil();
        if (!(symbol instanceof String) && !(symbol instanceof ItemStack)) {
            throw new IllegalArgumentException("Icon must be either String or ItemStack");
        }
        this.symbol = symbol;
        this.color = color;
        this.message = message;
        this.time = time;
    }

    public Notification(Object symbol, Object message, int time) {
        this(symbol, -1, message, time);
    }

    public void a(int time) {
        this.time = time;
    }

    public AnimationUtil a() {
        return this.animation;
    }

    public CounterUtil b() {
        return this.counter;
    }

    public Object c() {
        return this.message;
    }

    public Object d() {
        return this.symbol;
    }

    public int e() {
        return this.color;
    }

    public int f() {
        return this.time;
    }
}
