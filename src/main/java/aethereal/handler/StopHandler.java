package aethereal.handler;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;


public class StopHandler extends BaseHandler implements Interface {
    private int b = -1;

    public int c() {
        return this.b;
    }

    public void a(int ticks) {
        this.b = ticks;
    }

    public boolean a() {
        return this.b != -1;
    }

    public boolean b() {
        return this.b == 0;
    }

    @EventTarget
    public void a(TickEvent eventTick) {
        if (this.b > 0) {
            this.b--;
        } else if (this.b == 0) {
            this.b = -1;
        }
    }

    @EventTarget(a = 0)
    public void a(InputEvent eventInput) {
        if (this.b >= 0) {
            eventInput.setForward(0.0f);
            eventInput.setStrafe(0.0f);
            eventInput.setJump(false);
        }
    }
}
