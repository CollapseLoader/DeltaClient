package aethereal.core;

public class Event implements IEvent, Cancellable {
    private boolean cancelled;

    protected Event() {
    }

    @Override
    public boolean a() {
        return this.cancelled;
    }

    @Override
    public void a(boolean state) {
        this.cancelled = state;
    }
}
