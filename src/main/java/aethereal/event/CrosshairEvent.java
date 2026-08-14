package aethereal.event;

import aethereal.core.Event;


import net.minecraft.client.gui.DrawContext;

public class CrosshairEvent extends Event {
    private final DrawContext context;
    private final float partialTicks;

    public CrosshairEvent(DrawContext context, float partialTicks) {
        this.context = context;
        this.partialTicks = partialTicks;
    }

    public DrawContext getContext() {
        return this.context;
    }

    public float c() {
        return this.partialTicks;
    }
}
