package aethereal.event;

import aethereal.core.Event;


import net.minecraft.entity.LivingEntity;

public class JumpEvent extends Event {
    private final LivingEntity livingEntity;

    public JumpEvent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    public LivingEntity b() {
        return this.livingEntity;
    }
}
