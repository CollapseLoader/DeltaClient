package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.TickEvent;
import aethereal.util.InventoryUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.List;


public class InventoryHandler extends BaseHandler implements Interface {
    private final List<a> b = new ArrayList<>();

    public List<a> a() {
        return this.b;
    }

    @EventTarget
    public void onTickEvent(TickEvent event) {
        if (!this.b.isEmpty()) {
            a task = this.b.getFirst();
            StopHandler stopHandler = Delta.getInstance().getModuleProcessor().v().getStopHandler();
            if (stopHandler.c() < task.getBypass()) {
                int from = normalizeSlot(task.getFromSlot());
                int to = task.isArmorMove() ? task.getToSlot() : normalizeSlot(task.getToSlot());
                if (mc.player.playerScreenHandler.getSlot(from).getStack()
                        .contains(DataComponentTypes.BUNDLE_CONTENTS)) {
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 1,
                            SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, 0, SlotActionType.PICKUP,
                            mc.player);
                    if (!mc.player.playerScreenHandler.getCursorStack().isEmpty()) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0,
                                SlotActionType.PICKUP, mc.player);
                    }
                } else {
                    int swapButton = findSwapButton(task.getToSlot(), to);
                    if (swapButton != -1) {
                        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, swapButton,
                                SlotActionType.SWAP, mc.player);
                    } else {
                        int swapButton2 = findSwapButton(task.getFromSlot(), from);
                        if (swapButton2 != -1) {
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, swapButton2,
                                    SlotActionType.SWAP, mc.player);
                        } else if (from != to) {
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0,
                                    SlotActionType.SWAP, mc.player);
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, to, 0,
                                    SlotActionType.SWAP, mc.player);
                            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, from, 0,
                                    SlotActionType.SWAP, mc.player);
                        }
                    }
                }
                mc.player.networkHandler
                        .sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
                this.b.remove(task);
                if (!this.b.isEmpty()) {
                    stopHandler.a(this.b.getFirst().getBypass());
                }
            }
        }
    }

    public void moveItem(int fromSlot, int toSlot, int bypass) {
        enqueueTask(new a(fromSlot, toSlot, bypass, false));
    }

    public void moveToArmor(int fromSlot, int armorSlot, int bypass) {
        enqueueTask(new a(fromSlot, 5 + armorSlot, bypass, true));
    }

    public void moveItemByType(Item item, int toSlot, int bypass) {
        int slot = InventoryUtil.b(item);
        if (slot != -1) {
            enqueueTask(new a(slot, toSlot, bypass, false));
        }
    }

    public void moveStack(ItemStack stack, int toSlot, int bypass) {
        int slot = InventoryUtil.a(stack, false);
        if (slot != -1) {
            enqueueTask(new a(slot, toSlot, bypass, false));
        }
    }

    private void enqueueTask(a task) {
        if (task.getFromSlot() != -1 && task.getToSlot() != -1) {
            if (this.b.isEmpty() && task.bypass > 0) {
                Delta.getInstance().getModuleProcessor().v().getStopHandler().a(task.bypass);
            }
            this.b.add(task);
        }
    }

    private int normalizeSlot(int slot) {
        return (slot < 0 || slot > 8) ? slot : slot + 36;
    }

    private int findSwapButton(int original, int normalized) {
        if (original == 40 || original == 45 || normalized == 45) {
            return 40;
        }
        if (normalized < 36 || normalized > 44) {
            return -1;
        }
        return normalized - 36;
    }

    record a(int fromSlot, int toSlot, int bypass, boolean armorMove) {

        public int getFromSlot() {
            return this.fromSlot;
        }

        public int getToSlot() {
            return this.toSlot;
        }

        public int getBypass() {
            return this.bypass;
        }

        public boolean isArmorMove() {
            return this.armorMove;
        }
    }
}
