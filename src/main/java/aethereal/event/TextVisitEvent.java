package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class TextVisitEvent extends Event implements IEvent {
    private String a;

    public TextVisitEvent(String text) {
        this.a = text;
    }

    public void a(String text) {
        this.a = text;
    }

    public String b() {
        return this.a;
    }
}
