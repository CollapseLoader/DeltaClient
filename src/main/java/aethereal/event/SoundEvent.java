package aethereal.event;

import aethereal.core.Event;


import net.minecraft.client.sound.SoundInstance;

public class SoundEvent extends Event {
    private final SoundInstance sound;
    private float volume;

    public SoundEvent(SoundInstance sound, float volume) {
        this.sound = sound;
        this.volume = volume;
    }

    public SoundInstance b() {
        return this.sound;
    }

    public void a(float volume) {
        this.volume = volume;
    }

    public float c() {
        return this.volume;
    }
}
