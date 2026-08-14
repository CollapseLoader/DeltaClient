package aethereal.handler;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.*;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import java.util.ArrayDeque;
import java.util.Deque;


public class PvEHandler extends BaseHandler implements Interface {
    private final Deque<b> b = new ArrayDeque<>();

    static double getDurabilityPercentage(ItemStack stack) {
        if (!stack.isDamageable() || stack.getMaxDamage() <= 0) {
            return 100.0d;
        }
        return (1.0d - (((double) stack.getDamage()) / ((double) stack.getMaxDamage()))) * 100.0d;
    }

    public Deque<b> getTaskQueue() {
        return this.b;
    }

    @EventTarget
    public void onTickEvent(TickEvent event) {
        if (!this.b.isEmpty() && this.b.peek().isComplete()) {
            this.b.poll();
        }
    }

    @EventTarget(a = 4)
    public void onInputEvent(InputEvent event) {
        if (!this.b.isEmpty()) {
            MoveUtil.b(event);
        }
    }

    @EventTarget
    public void onPacketEvent(PacketEvent event) {
        if (this.b.isEmpty() || !event.isReceive()) {
            return;
        }
        GameMessageS2CPacket class_7439VarD = (GameMessageS2CPacket) event.getPacket();
        if (class_7439VarD instanceof GameMessageS2CPacket) {
            GameMessageS2CPacket gameMsg = class_7439VarD;
            if (gameMsg.content().getString().equals("Данная команда недоступна в режиме AFK")) {
                Delta.getInstance().getModuleProcessor().v().getAFKHandler().a(7);
            }
        }
    }

    public boolean startMendingTask(ItemStack tool, double startPct, double endPct) {
        ItemStack class_1799VarMethod_7972;
        for (b task : this.b) {
            if (task instanceof c) {
                return false;
            }
        }
        if (getDurabilityPercentage(tool) > startPct) {
            return true;
        }
        if (!InventoryUtil.a(tool, Enchantments.MENDING, 1)) {
            ChatUtil.sendMessage("На предмете нету починки, отмена.");
            return true;
        }
        ItemStack main = mc.player.getMainHandStack();
        ItemStack off = mc.player.getOffHandStack();
        if (main.isEmpty() || ItemStack.areItemsAndComponentsEqual(main, tool)) {
            class_1799VarMethod_7972 = (off.isEmpty() || off.getItem() == tool.getItem()) ? ItemStack.EMPTY : off.copy();
        } else {
            class_1799VarMethod_7972 = main.copy();
        }
        ItemStack restore = class_1799VarMethod_7972;
        this.b.add(new c(tool.getItem(), endPct, mc.player.getInventory().selectedSlot, restore));
        return false;
    }

    interface b {
        boolean isComplete();
    }

    static final class c implements Interface, b {
        private final Item tool;
        private final double endPct;
        private final int toolBarSlot;
        private final ItemStack restore;
        private int phase;
        private int ticks;

        c(Item tool, double endPct, int toolBarSlot, ItemStack restore) {
            this.tool = tool;
            this.endPct = endPct;
            this.toolBarSlot = toolBarSlot;
            this.restore = restore;
        }

        @Override
        public boolean isComplete() {
            InventoryHandler handler = Delta.getInstance().getModuleProcessor().v().getInventoryHandler();
            ItemStack offHand = mc.player.getOffHandStack();
            ItemStack mainHand = mc.player.getMainHandStack();
            switch (this.phase) {
                case 0:
                    if (mc.currentScreen != null) {
                        mc.currentScreen.close();
                    }
                    int i = this.ticks + 1;
                    this.ticks = i;
                    if (i >= 3 && handler.a().isEmpty()) {
                        handler.moveItem(this.toolBarSlot, 40, 1);
                        this.phase = 1;
                        return false;
                    }
                    return false;
                case 1:
                    if (!handler.a().isEmpty() || offHand.getItem() != this.tool) {
                        return false;
                    }
                    if (PvEHandler.getDurabilityPercentage(offHand) >= this.endPct) {
                        this.phase = 3;
                        return false;
                    }
                    int onBar = InventoryUtil.a(Items.EXPERIENCE_BOTTLE, true);
                    if (onBar != -1) {
                        mc.player.getInventory().selectedSlot = onBar;
                        this.phase = 2;
                        return false;
                    }
                    int inStorage = InventoryUtil.b(Items.EXPERIENCE_BOTTLE);
                    if (inStorage != -1) {
                        handler.moveItem(inStorage, this.toolBarSlot, 1);
                        this.phase = 2;
                        return false;
                    }
                    Delta.getInstance().getModuleProcessor().v().getPvEHandler().getTaskQueue().addFirst(new a(Items.EXPERIENCE_BOTTLE, 128, 2000));
                    return false;
                case 2:
                    if (!handler.a().isEmpty() || offHand.getItem() != this.tool) {
                        return false;
                    }
                    if (PvEHandler.getDurabilityPercentage(offHand) >= this.endPct) {
                        this.ticks = 0;
                        this.phase = 3;
                        return false;
                    }
                    if (mainHand.isEmpty()) {
                        this.phase = 1;
                        return false;
                    }
                    if (mainHand.getItem() != Items.EXPERIENCE_BOTTLE) {
                        int bar = InventoryUtil.a(Items.EXPERIENCE_BOTTLE, true);
                        mc.player.getInventory().selectedSlot = bar != -1 ? bar : this.toolBarSlot;
                        return false;
                    }
                    Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(mc.player.getYaw(), 90.0f), 360.0f, 1, 1);
                    mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    return false;
                case 3:
                    int i2 = this.ticks + 1;
                    this.ticks = i2;
                    if (i2 >= 3) {
                        handler.moveItem(this.toolBarSlot, 40, 1);
                        this.ticks = 0;
                        this.phase = 4;
                        return false;
                    }
                    return false;
                case 4:
                    int i3 = this.ticks + 1;
                    this.ticks = i3;
                    if (i3 >= 3) {
                        if (this.restore.isEmpty() || InventoryUtil.a(this.restore, false) == -1) {
                            return true;
                        }
                        handler.moveItem(InventoryUtil.a(this.restore, false), 40, 1);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        }
    }

    static final class a implements Interface, b {
        final Item b;
        final int c;
        final int d;
        int e;
        int f;

        a(Item targetItem, int need, int priceLimit) {
            this.b = targetItem;
            this.c = need;
            this.d = priceLimit;
        }

        @Override
        public boolean isComplete() {
            switch (this.e) {
                case 0:
                    if (mc.currentScreen != null) {
                        mc.currentScreen.close();
                    }
                    int i = this.f + 1;
                    this.f = i;
                    if (i >= 3) {
                        mc.player.networkHandler.sendChatMessage("/ah search " + (this.b == Items.EXPERIENCE_BOTTLE ? "Опыт" : new ItemStack(this.b).getName().getString()));
                        this.e = 1;
                        this.f = 0;
                        return false;
                    }
                    return false;
                case 1:
                    GenericContainerScreen class_476Var = (GenericContainerScreen) mc.currentScreen;
                    if (!(class_476Var instanceof GenericContainerScreen)) {
                        int i2 = this.f + 1;
                        this.f = i2;
                        if (i2 > 20) {
                            ChatUtil.sendMessage("Аукцион не открылся, повторяю.");
                            this.f = 0;
                            this.e = 0;
                            return false;
                        }
                        return false;
                    }
                    GenericContainerScreen screen = class_476Var;
                    if (screen.getTitle().getString().startsWith("☃") || screen.getTitle().getString().startsWith("0A2z")) {
                        this.e = 2;
                        this.f = 0;
                        return false;
                    }
                    return false;
                case 2:
                    int i3 = this.f + 1;
                    this.f = i3;
                    if (i3 >= 15) {
                        this.e = 3;
                        this.f = 0;
                        return false;
                    }
                    return false;
                case 3:
                    GenericContainerScreen class_476Var2 = (GenericContainerScreen) mc.currentScreen;
                    if (!(class_476Var2 instanceof GenericContainerScreen)) {
                        this.f = 0;
                        this.e = 0;
                        return false;
                    }
                    GenericContainerScreen screen2 = class_476Var2;
                    if (InventoryUtil.a(this.b) >= this.c) {
                        int i4 = this.f + 1;
                        this.f = i4;
                        if (i4 >= 6) {
                            mc.currentScreen.close();
                            return true;
                        }
                        return false;
                    }
                    this.f = 0;
                    if (mc.player.age % 7 == 0) {
                        Slot offer = null;
                        for (int i5 = 0; i5 < screen2.getScreenHandler().slots.size() - 36; i5++) {
                            Slot slot = screen2.getScreenHandler().slots.get(i5);
                            if (!slot.getStack().isEmpty() && slot.getStack().getItem() == this.b && ServerUtil.a.a$(slot.getStack()) > 0 && ServerUtil.a.a$(slot.getStack()) <= this.d && (offer == null || ServerUtil.a.a$(slot.getStack()) < ServerUtil.a.a$(offer.getStack()))) {
                                offer = slot;
                            }
                        }
                        if (offer == null) {
                            mc.interactionManager.clickSlot(screen2.getScreenHandler().syncId, 50, 0, SlotActionType.QUICK_MOVE, mc.player);
                            return false;
                        }
                        mc.interactionManager.clickSlot(screen2.getScreenHandler().syncId, offer.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                        return false;
                    }
                    return false;
                default:
                    return false;
            }
        }
    }
}
