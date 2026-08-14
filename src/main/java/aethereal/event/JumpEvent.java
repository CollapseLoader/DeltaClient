package aethereal.event;

import aethereal.core.Event;


import net.minecraft.entity.LivingEntity;

public class JumpEvent extends Event {
    private final LivingEntity a;

    public JumpEvent(LivingEntity livingEntity) {
        this.a = livingEntity;
    }

    public LivingEntity b() {
        return this.a;
    }
}
