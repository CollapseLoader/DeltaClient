package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.client.sound.SoundInstance;

public class SoundEvent extends Event implements IEvent {
    private final SoundInstance a;
    private float b;

    public SoundEvent(SoundInstance sound, float volume) {
        this.a = sound;
        this.b = volume;
    }

    @Generated
    public SoundInstance b() {
        return this.a;
    }

    @Generated
    public void a(float volume) {
        this.b = volume;
    }

    @Generated
    public float c() {
        return this.b;
    }
}
