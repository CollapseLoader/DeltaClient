package aethereal.util;


import lombok.Generated;

public class CounterUtil {
    private long a;
    private long b;
    private long c;

    public CounterUtil() {
        b();
    }

    @Generated
    public void c(long millis) {
        this.a = millis;
    }

    @Generated
    public void d(long ticks) {
        this.b = ticks;
    }

    public boolean a(long delay) {
        return System.currentTimeMillis() - delay >= this.a;
    }

    public boolean a(long delay, long jitter) {
        return System.currentTimeMillis() - (delay + (this.c % (jitter + 1))) >= this.a;
    }

    public boolean b(long delay) {
        return this.b >= delay;
    }

    public boolean b(long delay, long jitter) {
        return this.b >= delay + (this.c % (jitter + 1));
    }

    public void a() {
        this.b++;
    }

    public void b() {
        this.a = System.currentTimeMillis();
        this.c = (long) (Math.random() * 9.223372036854776E18d);
        this.b = 0L;
    }

    public long c() {
        return System.currentTimeMillis() - this.a;
    }

    public long d() {
        return this.b;
    }
}
