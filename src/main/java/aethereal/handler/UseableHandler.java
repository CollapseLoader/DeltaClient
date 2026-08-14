package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.TickEvent;
import aethereal.module.player.WindHop;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.Rotation;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BundleItemSelectedC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;


public class UseableHandler extends BaseHandler implements Interface {
    private final List<UseableTask> b = new ArrayList<>();

    public List<UseableTask> a() {
        return this.b;
    }

    @EventTarget
    public void onTickEvent(TickEvent event) {
        if (!this.b.isEmpty()) {
            UseableTask task = this.b.getFirst();
            WindHop windHop = Delta.getInstance().getModuleProcessor().t().aW();
            int hotbar = task.a().getItem() == Items.SPLASH_POTION ? InventoryUtil.b(task.a(), true) : InventoryUtil.a(task.a().getItem(), true);
            int inventory = task.a().getItem() == Items.SPLASH_POTION ? InventoryUtil.b(task.a(), false) : InventoryUtil.a(task.a().getItem(), false);
            if (task.d() == -1 && hotbar == -1 && inventory == -1) {
                this.b.remove(task);
                return;
            }
            task.c(task.d() + 1);
            if (task.d() == 0) {
                task.a(mc.player.getInventory().selectedSlot);
                if (hotbar != -1) {
                    task.b(hotbar);
                    if (hotbar != mc.player.getInventory().selectedSlot) {
                        setSelectedSlot(hotbar);
                        return;
                    }
                    return;
                }
                if (inventory != -1) {
                    int bundle = InventoryUtil.a(mc.player.getInventory().getStack(inventory), task.a());
                    if (bundle != -1) {
                        mc.player.networkHandler.sendPacket(new BundleItemSelectedC2SPacket(inventory < 9 ? 36 + inventory : inventory, bundle));
                    }
                    task.b((bundle == -1 || !mc.player.getMainHandStack().isEmpty()) ? inventory : task.b());
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(inventory, mc.player.getInventory().selectedSlot, 1);
                    return;
                }
                return;
            }
            if (task.d() == 1) {
                if (task.a().getItem() == Items.WIND_CHARGE && windHop.m() && windHop.q().c().booleanValue()) {
                    float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
                    float silent = (float) ((Math.sin(t * 0.31f) * 6.600001001477404d) + (Math.sin((t * 0.73f) + 1.1f) * 0.3000001491338646d));
                    Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(Look.b() + silent, 90.0f + (silent / 2.0f)), 180.0f, 1, 3);
                }
                a(task);
                if (mc.player.getInventory().getStack(task.c()).contains(DataComponentTypes.BUNDLE_CONTENTS)) {
                    mc.player.getInventory().setStack(task.b(), ItemStack.EMPTY);
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(task.c(), 36 + task.b(), 1);
                } else if (task.c() > 8) {
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(task.b(), task.c(), 1);
                } else if (task.b() != mc.player.getInventory().selectedSlot) {
                    setSelectedSlot(task.b());
                }
                this.b.remove(task);
            }
        }
    }

    public void setSelectedSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
    }

    public void a(UseableTask task) {
        ((platform.inject.invokers.ClientPlayerInteractionManagerInvoker) mc.interactionManager).invokeSendSequencedPacket(mc.world, sequence -> {
            return new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, sequence, mc.player.getYaw(), mc.player.getPitch());
        });
    }

    public void a(ItemStack itemStack) {
        this.b.add(new UseableTask(itemStack));
    }

    public static final class UseableTask {
        private final ItemStack itemStack;
        private int selectedSlot;
        private int itemSlot;
        private int ticks = -1;

        public UseableTask(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public void a(int selectedSlot) {
            this.selectedSlot = selectedSlot;
        }

        public void b(int itemSlot) {
            this.itemSlot = itemSlot;
        }

        public void c(int ticks) {
            this.ticks = ticks;
        }

        public ItemStack a() {
            return this.itemStack;
        }

        public int b() {
            return this.selectedSlot;
        }

        public int c() {
            return this.itemSlot;
        }

        public int d() {
            return this.ticks;
        }
    }
}
