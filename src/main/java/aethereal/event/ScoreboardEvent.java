package aethereal.event;

import aethereal.core.Event;


import net.minecraft.text.Text;

public class ScoreboardEvent extends Event {
    private Text a;

    public ScoreboardEvent(Text title) {
        this.a = title;
    }

    public void a(Text title) {
        this.a = title;
    }

    public Text b() {
        return this.a;
    }
}
