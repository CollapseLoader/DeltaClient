package aethereal.event;

import aethereal.core.Event;


public class TextVisitEvent extends Event {
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
