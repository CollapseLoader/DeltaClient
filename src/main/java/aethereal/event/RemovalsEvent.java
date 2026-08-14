package aethereal.event;

import aethereal.core.Event;


public class RemovalsEvent extends Event {
    private final type removalType;

    public RemovalsEvent(type type) {
        this.removalType = type;
    }

    public type b() {
        return this.removalType;
    }

    public enum type {
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
