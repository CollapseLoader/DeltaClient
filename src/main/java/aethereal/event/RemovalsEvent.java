package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class RemovalsEvent extends Event implements IEvent {
    private final a a;

    public RemovalsEvent(a type) {
        this.a = type;
    }

    @Generated
    public a b() {
        return this.a;
    }

    public enum a {
        HURT_CAM,
        SCOREBOARD,
        BOSS_BAR,
        PORTAL,
        FIRE,
        CLIP,
        BREAK_PARTICLES,
        WATER,
        NAUSEA,
        BLINDNESS,
        PUMPKIN,
        WEATHER,
        GLOW,
        DARKNESS,
        BLACK_HEARTS
    }
}
