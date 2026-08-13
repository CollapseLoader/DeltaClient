package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.client.gui.DrawContext;

public class CrosshairEvent extends Event implements IEvent {
    private final DrawContext a;
    private final float b;

    public CrosshairEvent(DrawContext context, float partialTicks) {
        this.a = context;
        this.b = partialTicks;
    }

    @Generated
    public DrawContext b() {
        return this.a;
    }

    @Generated
    public float c() {
        return this.b;
    }
}
