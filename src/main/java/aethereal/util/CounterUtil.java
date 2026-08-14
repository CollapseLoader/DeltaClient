package aethereal.util;


public class CounterUtil {
    private long lastMillis;
    private long tickCount;
    private long randomSeed;

    public CounterUtil() {
        b();
    }

    public void c(long millis) {
        this.lastMillis = millis;
    }

    public void d(long ticks) {
        this.tickCount = ticks;
    }

    public boolean a(long delay) {
        return System.currentTimeMillis() - delay >= this.lastMillis;
    }

    public boolean a(long delay, long jitter) {
        return System.currentTimeMillis() - (delay + (this.randomSeed % (jitter + 1))) >= this.lastMillis;
    }

    public boolean b(long delay) {
        return this.tickCount >= delay;
    }

    public boolean b(long delay, long jitter) {
        return this.tickCount >= delay + (this.randomSeed % (jitter + 1));
    }

    public void a() {
        this.tickCount++;
    }

    public void b() {
        this.lastMillis = System.currentTimeMillis();
        this.randomSeed = (long) (Math.random() * 9.223372036854776E18d);
        this.tickCount = 0L;
    }

    public long c() {
        return System.currentTimeMillis() - this.lastMillis;
    }

    public long d() {
        return this.tickCount;
    }
}
