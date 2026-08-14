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

    public SoundInstance getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
