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

    public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean isCrouching,
                       boolean isSprinting) {
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
        return other.isMotionEvent(this) && super.equals(o) && Double.compare(getX(), other.getX()) == 0
                && Double.compare(getY(), other.getY()) == 0 && Double.compare(getZ(), other.getZ()) == 0
                && Float.compare(getYaw(), other.getYaw()) == 0 && Float.compare(getPitch(), other.getPitch()) == 0
                && isOnGround() == other.isOnGround() && isCrouching() == other.isCrouching()
                && isSprinting() == other.isSprinting();
    }

    protected boolean isMotionEvent(Object other) {
        return other instanceof MotionEvent;
    }

    public int hashCode() {
        int result = super.hashCode();
        long $x = Double.doubleToLongBits(getX());
        int result2 = (result * 59) + ((int) (($x >>> 32) ^ $x));
        long $y = Double.doubleToLongBits(getY());
        int result3 = (result2 * 59) + ((int) (($y >>> 32) ^ $y));
        long $z = Double.doubleToLongBits(getZ());
        return (((((((((((result3 * 59) + ((int) (($z >>> 32) ^ $z))) * 59) + Float.floatToIntBits(getYaw())) * 59)
                + Float.floatToIntBits(getPitch())) * 59) + (isOnGround() ? 79 : 97)) * 59) + (isCrouching() ? 79 : 97))
                * 59) + (isSprinting() ? 79 : 97);
    }

    public String toString() {
        double dB = getX();
        double dC = getY();
        double d = getZ();
        float fE = getYaw();
        float f = getPitch();
        isOnGround();
        isCrouching();
        isSprinting();
        return "MotionEvent(x=" + dB + ", y=" + dB + ", z=" + dC + ", yaw=" + dB + ", pitch=" + d + ", onGround=" + dB
                + ", isCrouching=" + fE + ", isSprinting=" + f + ")";
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isCrouching() {
        return this.isCrouching;
    }

    public void setCrouching(boolean isCrouching) {
        this.isCrouching = isCrouching;
    }

    public boolean isSprinting() {
        return this.isSprinting;
    }

    public void setSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
    }
}
