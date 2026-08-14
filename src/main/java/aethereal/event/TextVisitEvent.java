package aethereal.event;

import aethereal.core.Event;


public class TextVisitEvent extends Event {
    private String text;

    public TextVisitEvent(String text) {
        this.text = text;
    }

    public void a(String text) {
        this.text = text;
    }

    public String b() {
        return this.text;
    }
}
