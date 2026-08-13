package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class MotionEvent extends Event implements IEvent {
    private double a;
    private double b;
    private double c;
    private float d;
    private float e;
    private boolean f;
    private boolean g;
    private boolean h;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean isCrouching, boolean isSprinting) {
        this.a = x;
        this.b = y;
        this.c = z;
        this.d = yaw;
        this.e = pitch;
        this.f = onGround;
        this.g = isCrouching;
        this.h = isSprinting;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MotionEvent other)) {
            return false;
        }
        return other.a(this) && super.equals(o) && Double.compare(b(), other.b()) == 0 && Double.compare(c(), other.c()) == 0 && Double.compare(d(), other.d()) == 0 && Float.compare(e(), other.e()) == 0 && Float.compare(f(), other.f()) == 0 && g() == other.g() && h() == other.h() && i() == other.i();
    }

    @Generated
    protected boolean a(Object other) {
        return other instanceof MotionEvent;
    }

    @Generated
    public int hashCode() {
        int result = super.hashCode();
        long $x = Double.doubleToLongBits(b());
        int result2 = (result * 59) + ((int) (($x >>> 32) ^ $x));
        long $y = Double.doubleToLongBits(c());
        int result3 = (result2 * 59) + ((int) (($y >>> 32) ^ $y));
        long $z = Double.doubleToLongBits(d());
        return (((((((((((result3 * 59) + ((int) (($z >>> 32) ^ $z))) * 59) + Float.floatToIntBits(e())) * 59) + Float.floatToIntBits(f())) * 59) + (g() ? 79 : 97)) * 59) + (h() ? 79 : 97)) * 59) + (i() ? 79 : 97);
    }

    @Generated
    public void a(double x) {
        this.a = x;
    }

    @Generated
    public void b(double y) {
        this.b = y;
    }

    @Generated
    public void c(double z) {
        this.c = z;
    }

    @Generated
    public void a(float yaw) {
        this.d = yaw;
    }

    @Generated
    public void b(float pitch) {
        this.e = pitch;
    }

    @Generated
    public void b(boolean onGround) {
        this.f = onGround;
    }

    @Generated
    public void c(boolean isCrouching) {
        this.g = isCrouching;
    }

    @Generated
    public void d(boolean isSprinting) {
        this.h = isSprinting;
    }

    @Generated
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

    @Generated
    public double b() {
        return this.a;
    }

    @Generated
    public double c() {
        return this.b;
    }

    @Generated
    public double d() {
        return this.c;
    }

    @Generated
    public float e() {
        return this.d;
    }

    @Generated
    public float f() {
        return this.e;
    }

    @Generated
    public boolean g() {
        return this.f;
    }

    @Generated
    public boolean h() {
        return this.g;
    }

    @Generated
    public boolean i() {
        return this.h;
    }
}
