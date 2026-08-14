package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ClickEvent;
import aethereal.event.HotbarEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;


public class InteractHandler extends BaseHandler implements Interface {
    private final List<a> b = new ArrayList<>();

    public List<a> b() {
        return this.b;
    }

    public void a(int slot) {
        if (this.b.isEmpty() && Delta.getInstance().getModuleProcessor().v().a().a().isEmpty()) {
            this.b.add(new a(slot));
        }
    }

    public boolean a() {
        return !this.b.isEmpty();
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!this.b.isEmpty() && mc.player.age > 40) {
            InventoryHandler inventoryHandler = Delta.getInstance().getModuleProcessor().v().a();
            a task = this.b.getFirst();
            boolean inventory = task.b() > 8;
            task.a(task.d() + 1);
            if (task.d() == 1) {
                if (inventory) {
                    inventoryHandler.a(task.b(), task.a(), 2);
                } else {
                    mc.player.getInventory().selectedSlot = task.b();
                }
            } else if (!task.c() && task.d() > 0 && inventoryHandler.a().isEmpty()) {
                if (mc.player.isUsingItem()) {
                    task.a(true);
                } else {
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                }
            } else if (task.c() && !mc.player.isUsingItem() && inventoryHandler.a().isEmpty()) {
                if (inventory) {
                    inventoryHandler.a(task.a(), task.b(), 2);
                } else {
                    mc.player.getInventory().selectedSlot = task.a();
                }
                this.b.remove(task);
            }
            if (task.d() >= 60) {
                ChatUtil.sendMessage("Использование предмета не удалось по неизвестной причине");
                this.b.remove(task);
            }
        }
    }

    @EventTarget
    public void a(HotbarEvent event) {
        if (a()) {
            event.a(true);
        }
    }

    @EventTarget
    public void a(ClickEvent event) {
        if (a() && event.h() == 1) {
            event.a(true);
        }
    }

    public static final class a {
        private final int a = Interface.mc.player.getInventory().selectedSlot;
        private final int b;
        private boolean c;
        private int d;

        public a(int eatSlot) {
            this.b = eatSlot;
        }

        public void a(boolean returned) {
            this.c = returned;
        }

        public void a(int ticks) {
            this.d = ticks;
        }

        public int a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public boolean c() {
            return this.c;
        }

        public int d() {
            return this.d;
        }
    }
}
