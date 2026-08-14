package aethereal.event;

import aethereal.core.Event;


public class TextVisitEvent extends Event {
    private String text;

    public TextVisitEvent(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String b() {
        return this.text;
    }
}
