package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class InputEvent extends Event implements IEvent {
    private float forward;
    private float strafe;
    private boolean jump;
    private boolean sneak;

    public InputEvent(float forward, float strafe, boolean jump, boolean sneak) {
        this.forward = forward;
        this.strafe = strafe;
        this.jump = jump;
        this.sneak = sneak;
    }

    /**
     * @deprecated Use {@link #setForward(float)}
     */
    @Deprecated
    public void a(float forward) {
        setForward(forward);
    }

    /**
     * @deprecated Use {@link #setStrafe(float)}
     */
    @Deprecated
    public void b(float strafe) {
        setStrafe(strafe);
    }

    /**
     * @deprecated Use {@link #setJump(boolean)}
     */
    @Deprecated
    public void b(boolean jump) {
        setJump(jump);
    }

    /**
     * @deprecated Use {@link #setSneak(boolean)}
     */
    @Deprecated
    public void c(boolean sneak) {
        setSneak(sneak);
    }

    @Generated
    public float getForward() {
        return this.forward;
    }

    @Generated
    public void setForward(float forward) {
        this.forward = forward;
    }

    /**
     * @deprecated Use {@link #getForward()}
     */
    @Deprecated
    public float b() {
        return getForward();
    }

    @Generated
    public float getStrafe() {
        return this.strafe;
    }

    @Generated
    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    /**
     * @deprecated Use {@link #getStrafe()}
     */
    @Deprecated
    public float c() {
        return getStrafe();
    }

    @Generated
    public boolean isJump() {
        return this.jump;
    }

    @Generated
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    /**
     * @deprecated Use {@link #isJump()}
     */
    @Deprecated
    public boolean d() {
        return isJump();
    }

    @Generated
    public boolean isSneak() {
        return this.sneak;
    }

    @Generated
    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }

    /**
     * @deprecated Use {@link #isSneak()}
     */
    @Deprecated
    public boolean e() {
        return isSneak();
    }
}
