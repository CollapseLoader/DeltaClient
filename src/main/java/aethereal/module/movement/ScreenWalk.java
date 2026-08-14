package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.handler.StopHandler;
import aethereal.setting.ModeSetting;
import aethereal.util.MoveUtil;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import platform.inject.accessors.ClientConnectionAccessor;

import java.util.ArrayList;
import java.util.List;

@ModuleRegister(a = "Screen Walk", b = "Позволяет двигаться с открытым контейнером, задерживая пакеты инвентаря", c = Category.Movement)
public class ScreenWalk extends Module {
    private final ModeSetting b = new ModeSetting("Обход перемещения предметов", "Ускоренный", "Ускоренный", "Медленный");
    private final List<a> c = new ArrayList();
    private boolean d = false;

    public ScreenWalk() {
        a(this.b);
    }

    @EventTarget(a = 0)
    public void a(PacketEvent event) {
        boolean isShulker;
        StopHandler stopHandler = Delta.h().d().v().c();
        if (event.b()) {
            if (mc.currentScreen instanceof InventoryScreen) {
                ClickSlotC2SPacket click = (ClickSlotC2SPacket) event.d();
                if (click instanceof ClickSlotC2SPacket) {
                    if (MoveUtil.a()) {
                        if (click.getButton() == 1) {
                            if (mc.player.currentScreenHandler.getCursorStack().getItem() instanceof BlockItem blockItem) {
                                isShulker = blockItem.getBlock() instanceof ShulkerBoxBlock;
                            } else {
                                isShulker = false;
                            }
                        } else {
                            isShulker = false;
                        }
                        boolean shulker = isShulker;
                        if (shulker) {
                            stopHandler.a(2);
                        }
                        this.c.add(new a(event.d(), this.b.l("Медленный") ? this.c.isEmpty() ? 1 : this.c.size() + 1 : 2, shulker));
                        event.a(true);
                    }
                }
            }
            if (event.d() instanceof CloseHandledScreenC2SPacket) {
                if (MoveUtil.a() && (mc.currentScreen instanceof InventoryScreen)) {
                    event.a(true);
                    for (a packet : this.c) {
                        stopHandler.a(packet.b());
                    }
                }
                this.d = false;
            }
        }
        if (event.c() && this.b.l("Медленный")) {
            if (event.d() instanceof OpenScreenS2CPacket) {
                this.d = true;
            }
            if (event.d() instanceof CloseScreenS2CPacket) {
                this.d = false;
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!this.d && mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof SignEditScreen) && !(mc.currentScreen instanceof AnvilScreen) && !(mc.currentScreen instanceof CreativeInventoryScreen)) {
            for (KeyBinding keyBinding : new KeyBinding[]{mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey}) {
                keyBinding.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), keyBinding.getDefaultKey().getCode()));
            }
        }
        if (!MoveUtil.a() && !this.c.isEmpty()) {
            ClientConnectionAccessor connection = (ClientConnectionAccessor) mc.player.networkHandler.getConnection();
            if (this.b.l("Медленный")) {
                connection.sendWithoutEvent(this.c.removeFirst().a(), null, true);
            } else {
                this.c.forEach(packetInfo -> {
                    connection.sendWithoutEvent(packetInfo.a(), null, true);
                });
                this.c.clear();
            }
            if (this.c.isEmpty() && mc.currentScreen == null) {
                connection.sendWithoutEvent(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId), null, true);
            }
        }
    }

    record a(Packet<?> a, int b, boolean c) {
    }
}
