package aethereal.util;


import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;

public class Timer implements Serializable {
    @Serial
    private static final long serialVersionUID = 9175191792439630013L;
    private static final long e = 1000000000;
    private static final long f = e * 60;
    private static final long g = f * 60;
    private final String a;
    private final int d;
    private State b;
    private long c;
    private final ThreadLocal<Long> h;

    public Timer(final String name) {
        this(name, 0);
    }

    public Timer(final String name, final int iterations) {
        this.h = new ThreadLocal<Long>() {
            @Override
            public Long initialValue() {
                return 0L;
            }
        };
        this.a = name;
        this.b = State.Stopped;
        this.d = Math.max(iterations, 0);
    }

    public synchronized void a() {
        this.h.set(Long.valueOf(System.nanoTime()));
        this.c = 0L;
        this.b = State.Started;
    }

    public synchronized void b() {
        if (this.b == State.Stopped) {
            a();
        } else {
            e();
        }
    }

    public synchronized String c() {
        this.c += System.nanoTime() - this.h.get().longValue();
        this.h.set(0L);
        this.b = State.Stopped;
        return toString();
    }

    public synchronized void d() {
        this.c += System.nanoTime() - this.h.get().longValue();
        this.h.set(0L);
        this.b = State.Paused;
    }

    public synchronized void e() {
        this.h.set(Long.valueOf(System.nanoTime()));
        this.b = State.Started;
    }

    public String f() {
        return this.a;
    }

    public long g() {
        return this.c / 1000000;
    }

    public long h() {
        return this.c;
    }

    public State i() {
        return this.b;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        a(result);
        return result.toString();
    }

    public void a(final StringBuilder buffer) {
        buffer.append("Timer ").append(this.a);
        switch (this.b) {
            case State.Started:
                buffer.append(" started");
                break;
            case State.Paused:
                buffer.append(" paused");
                break;
            case State.Stopped:
                long nanoseconds = this.c;
                long hours = nanoseconds / g;
                long nanoseconds2 = nanoseconds % g;
                long minutes = nanoseconds2 / f;
                long nanoseconds3 = nanoseconds2 % f;
                long seconds = nanoseconds3 / e;
                long nanoseconds4 = nanoseconds3 % e;
                String elapsed = "";
                if (hours > 0) {
                    elapsed = elapsed + hours + " hours ";
                }
                if (minutes > 0 || hours > 0) {
                    elapsed = elapsed + minutes + " minutes ";
                }
                DecimalFormat numFormat = new DecimalFormat("#0");
                String elapsed2 = elapsed + numFormat.format(seconds) + '.';
                DecimalFormat numFormat2 = new DecimalFormat("000000000");
                buffer.append(" stopped. Elapsed time: ").append(elapsed2 + numFormat2.format(nanoseconds4) + " seconds");
                if (this.d > 0) {
                    long nanoseconds5 = this.c / ((long) this.d);
                    long hours2 = nanoseconds5 / g;
                    long nanoseconds6 = nanoseconds5 % g;
                    long minutes2 = nanoseconds6 / f;
                    long nanoseconds7 = nanoseconds6 % f;
                    long seconds2 = nanoseconds7 / e;
                    long nanoseconds8 = nanoseconds7 % e;
                    String elapsed3 = "";
                    if (hours2 > 0) {
                        elapsed3 = elapsed3 + hours2 + " hours ";
                    }
                    if (minutes2 > 0 || hours2 > 0) {
                        elapsed3 = elapsed3 + minutes2 + " minutes ";
                    }
                    DecimalFormat numFormat3 = new DecimalFormat("#0");
                    String elapsed4 = elapsed3 + numFormat3.format(seconds2) + '.';
                    DecimalFormat numFormat4 = new DecimalFormat("000000000");
                    buffer.append(" Average per iteration: ").append(elapsed4 + numFormat4.format(nanoseconds8) + " seconds");
                }
                break;
            default:
                buffer.append(' ').append(this.b);
                break;
        }
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Timer timer)) {
            return false;
        }
        if (this.c != timer.c || this.h != timer.h) {
            return false;
        }
        if (this.a != null) {
            if (!this.a.equals(timer.a)) {
                return false;
            }
        } else if (timer.a != null) {
            return false;
        }
        if (this.b != null) {
            return this.b.equals(timer.b);
        }
        return timer.b == null;
    }

    public int hashCode() {
        int result = this.a != null ? this.a.hashCode() : 0;
        int result2 = (29 * result) + (this.b != null ? this.b.hashCode() : 0);
        long time = this.h.get().longValue();
        return (29 * ((29 * result2) + ((int) (time ^ (time >>> 32))))) + ((int) (this.c ^ (this.c >>> 32)));
    }

    public enum State {
        Started,
        Stopped,
        Paused
    }
}
