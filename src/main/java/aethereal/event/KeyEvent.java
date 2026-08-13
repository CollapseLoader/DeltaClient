package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

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

    @Generated
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

    @Generated
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

    @Generated
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

    @Generated
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
