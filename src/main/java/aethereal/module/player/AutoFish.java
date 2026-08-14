package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.HotbarEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.CounterUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

@ModuleRegister(name = "Auto Fish", description = "Автоматически ловит рыбу в AFK-режиме", category = Category.Player)
public class AutoFish extends Module {
    private final CounterUtil b = new CounterUtil();
    private boolean isFishing;

    public CounterUtil q() {
        return this.b;
    }

    public boolean r() {
        return this.isFishing;
    }

    @Override
    public void b() {
        super.b();
        if (mc.player != null && mc.player.getInventory().getStack(mc.player.getInventory().selectedSlot).getItem() == Items.FISHING_ROD) {
            if (mc.player.fishHook == null) {
                d(false);
            }
            ChatUtil.sendMessage(j() + " активирован, удачной рыбалки!");
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.getPacket() instanceof PlaySoundS2CPacket packet) {
            if (packet.getSound().value().id().equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH.id()) && mc.player.fishHook.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) <= 0.48999979194765847d && mc.player.fishHook != null) {
                d(true);
                this.b.b();
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.b.a(450L) && this.isFishing) {
            d(false);
        }
    }

    @EventTarget
    public void a(HotbarEvent event) {
        if (this.isFishing) {
            event.a(true);
        }
    }

    public void d(boolean cast) {
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        mc.player.swingHand(Hand.MAIN_HAND);
        this.isFishing = cast;
    }
}
