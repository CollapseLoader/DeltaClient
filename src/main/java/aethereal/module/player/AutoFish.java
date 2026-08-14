package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HotbarEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

@ModuleRegister(a = "Auto Fish", b = "Автоматически ловит рыбу в AFK-режиме", c = Category.Player)
public class AutoFish extends Module implements Interface {
    private final CounterUtil b = new CounterUtil();
    private boolean c;

    @Generated
    public CounterUtil q() {
        return this.b;
    }

    @Generated
    public boolean r() {
        return this.c;
    }

    @Override
    public void b() {
        super.b();
        if (aM_.player != null && aM_.player.getInventory().getStack(aM_.player.getInventory().selectedSlot).getItem() == Items.FISHING_ROD) {
            if (aM_.player.fishHook == null) {
                d(false);
            }
            ChatUtil.sendMessage(j() + " активирован, удачной рыбалки!");
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.d() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().value().id().equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.id()) && aM_.player.fishHook.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) <= 0.48999979194765847d && aM_.player.fishHook != null) {
                d(true);
                this.b.b();
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.b.a(450L) && this.c) {
            d(false);
        }
    }

    @EventTarget
    public void a(HotbarEvent event) {
        if (this.c) {
            event.a(true);
        }
    }

    public void d(boolean cast) {
        aM_.interactionManager.interactItem(aM_.player, Hand.MAIN_HAND);
        aM_.player.swingHand(Hand.MAIN_HAND);
        this.c = cast;
    }
}
