package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.text.Text;

public class ScoreboardEvent extends Event implements IEvent {
    private Text a;

    public ScoreboardEvent(Text title) {
        this.a = title;
    }

    @Generated
    public void a(Text title) {
        this.a = title;
    }

    @Generated
    public Text b() {
        return this.a;
    }
}
