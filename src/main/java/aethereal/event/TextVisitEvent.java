package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class TextVisitEvent extends Event implements IEvent {
    private String a;

    @Generated
    public TextVisitEvent(String text) {
        this.a = text;
    }

    @Generated
    public void a(String text) {
        this.a = text;
    }

    @Generated
    public String b() {
        return this.a;
    }
}
