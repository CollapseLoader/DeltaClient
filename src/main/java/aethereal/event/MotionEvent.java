package aethereal.event;

import aethereal.core.Event;


public class MotionEvent extends Event {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean isCrouching;
    private boolean isSprinting;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean isCrouching, boolean isSprinting) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.isCrouching = isCrouching;
        this.isSprinting = isSprinting;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MotionEvent other)) {
            return false;
        }
        return other.a(this) && super.equals(o) && Double.compare(b(), other.b()) == 0 && Double.compare(c(), other.c()) == 0 && Double.compare(d(), other.d()) == 0 && Float.compare(e(), other.e()) == 0 && Float.compare(f(), other.f()) == 0 && g() == other.g() && h() == other.h() && i() == other.i();
    }

    protected boolean a(Object other) {
        return other instanceof MotionEvent;
    }

    public int hashCode() {
        int result = super.hashCode();
        long $x = Double.doubleToLongBits(b());
        int result2 = (result * 59) + ((int) (($x >>> 32) ^ $x));
        long $y = Double.doubleToLongBits(c());
        int result3 = (result2 * 59) + ((int) (($y >>> 32) ^ $y));
        long $z = Double.doubleToLongBits(d());
        return (((((((((((result3 * 59) + ((int) (($z >>> 32) ^ $z))) * 59) + Float.floatToIntBits(e())) * 59) + Float.floatToIntBits(f())) * 59) + (g() ? 79 : 97)) * 59) + (h() ? 79 : 97)) * 59) + (i() ? 79 : 97);
    }

    public void a(double x) {
        this.x = x;
    }

    public void b(double y) {
        this.y = y;
    }

    public void c(double z) {
        this.z = z;
    }

    public void a(float yaw) {
        this.yaw = yaw;
    }

    public void b(float pitch) {
        this.pitch = pitch;
    }

    public void b(boolean onGround) {
        this.onGround = onGround;
    }

    public void c(boolean isCrouching) {
        this.isCrouching = isCrouching;
    }

    public void d(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }

    public String toString() {
        double dB = b();
        double dC = c();
        double d = d();
        float fE = e();
        float f = f();
        g();
        h();
        i();
        return "MotionEvent(x=" + dB + ", y=" + dB + ", z=" + dC + ", yaw=" + dB + ", pitch=" + d + ", onGround=" + dB + ", isCrouching=" + fE + ", isSprinting=" + f + ")";
    }

    public double b() {
        return this.x;
    }

    public double c() {
        return this.y;
    }

    public double d() {
        return this.z;
    }

    public float e() {
        return this.yaw;
    }

    public float f() {
        return this.pitch;
    }

    public boolean g() {
        return this.onGround;
    }

    public boolean h() {
        return this.isCrouching;
    }

    public boolean i() {
        return this.isSprinting;
    }
}
