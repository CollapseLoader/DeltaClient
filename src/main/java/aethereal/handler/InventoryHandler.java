package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.List;

@Handler_2
public class InventoryHandler extends BaseHandler implements Interface {
    private final List<a> b = new ArrayList();

    @Generated
    public List<a> a() {
        return this.b;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!this.b.isEmpty()) {
            a task = this.b.getFirst();
            StopHandler stopHandler = Delta.h().d().v().c();
            if (stopHandler.c() < task.c()) {
                int from = a(task.a());
                int to = task.d() ? task.b() : a(task.b());
                if (mc.player.playerScreenHandler.getSlot(from).getStack().contains(DataComponentTypes.BUNDLE_CONTENTS)) {
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 1, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, 0, SlotActionType.PICKUP, mc.player);
                    if (!mc.player.playerScreenHandler.getCursorStack().isEmpty()) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0, SlotActionType.PICKUP, mc.player);
                    }
                } else {
                    int swapButton = a(task.b(), to);
                    if (swapButton != -1) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, swapButton, SlotActionType.SWAP, mc.player);
                    } else {
                        int swapButton2 = a(task.a(), from);
                        if (swapButton2 != -1) {
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, swapButton2, SlotActionType.SWAP, mc.player);
                        } else if (from != to) {
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0, SlotActionType.SWAP, mc.player);
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, 0, SlotActionType.SWAP, mc.player);
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0, SlotActionType.SWAP, mc.player);
                        }
                    }
                }
                mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                this.b.remove(task);
                if (!this.b.isEmpty()) {
                    stopHandler.a(this.b.getFirst().c());
                }
            }
        }
    }

    public void a(int fromSlot, int toSlot, int bypass) {
        a(new a(fromSlot, toSlot, bypass, false));
    }

    public void b(int fromSlot, int armorSlot, int bypass) {
        a(new a(fromSlot, 5 + armorSlot, bypass, true));
    }

    public void a(Item item, int toSlot, int bypass) {
        int slot = InventoryUtil.b(item);
        if (slot != -1) {
            a(new a(slot, toSlot, bypass, false));
        }
    }

    public void a(ItemStack stack, int toSlot, int bypass) {
        int slot = InventoryUtil.a(stack, false);
        if (slot != -1) {
            a(new a(slot, toSlot, bypass, false));
        }
    }

    private void a(a task) {
        if (task.a() != -1 && task.b() != -1) {
            if (this.b.isEmpty() && task.c > 0) {
                Delta.h().d().v().c().a(task.c);
            }
            this.b.add(task);
        }
    }

    private int a(int slot) {
        return (slot < 0 || slot > 8) ? slot : slot + 36;
    }

    private int a(int original, int normalized) {
        if (original == 40 || original == 45 || normalized == 45) {
            return 40;
        }
        if (normalized < 36 || normalized > 44) {
            return -1;
        }
        return normalized - 36;
    }

    record a(int a, int b, int c, boolean d) {

        @Override
        @Generated
            public int a() {
                return this.a;
            }

            @Override
            @Generated
            public int b() {
                return this.b;
            }

            @Override
            @Generated
            public int c() {
                return this.c;
            }

            @Override
            @Generated
            public boolean d() {
                return this.d;
            }
        }
}
