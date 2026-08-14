package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.client.sound.SoundInstance;

public class SoundEvent extends Event implements IEvent {
    private final SoundInstance a;
    private float b;

    public SoundEvent(SoundInstance sound, float volume) {
        this.a = sound;
        this.b = volume;
    }

    public SoundInstance b() {
        return this.a;
    }

    public void a(float volume) {
        this.b = volume;
    }

    public float c() {
        return this.b;
    }
}
