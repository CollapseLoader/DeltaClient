package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class KeyEvent extends Event implements IEvent {
    private final int key;
    private final int scanCode;
    private final int action;
    private final int modifiers;

    public KeyEvent(int key, int scanCode, int action, int modifiers) {
        this.key = key;
        this.scanCode = scanCode;
        this.action = action;
        this.modifiers = modifiers;
    }

    public int getKey() {
        return this.key;
    }

    /**
     * @deprecated Use {@link #getKey()}
     */
    @Deprecated
    public int b() {
        return getKey();
    }

    public int getScanCode() {
        return this.scanCode;
    }

    /**
     * @deprecated Use {@link #getScanCode()}
     */
    @Deprecated
    public int c() {
        return getScanCode();
    }

    public int getAction() {
        return this.action;
    }

    /**
     * @deprecated Use {@link #getAction()}
     */
    @Deprecated
    public int d() {
        return getAction();
    }

    public int getModifiers() {
        return this.modifiers;
    }

    /**
     * @deprecated Use {@link #getModifiers()}
     */
    @Deprecated
    public int e() {
        return getModifiers();
    }
}
