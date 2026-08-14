package aethereal.event;

import aethereal.core.Event;

public class InputEvent extends Event {
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

    public float getForward() {
        return this.forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public boolean isJump() {
        return this.jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isSneak() {
        return this.sneak;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }
}
