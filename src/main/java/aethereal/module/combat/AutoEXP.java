package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HotbarEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BindSetting;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.Rotation;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@ModuleRegister(a = "Auto EXP", b = "Бросает бутылочки опыта под себя, пока удерживается заданная клавиша", c = Category.Combat)
public class AutoEXP extends Module {
    private final int[] d = {-1, -1};
    private boolean c;

    public AutoEXP() {
        BindSetting b = new BindSetting("Кнопка активации", -1, 0).a(() -> {
            d(true);
        }).b(() -> {
            d(false);
        });
        a(b);
    }

    @EventTarget
    public void a(HotbarEvent event) {
        if (this.c) {
            event.a(true);
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (aM_.player == null) {
            return;
        }
        if (aM_.currentScreen != null) {
            d(false);
        }
        if (this.c) {
            q();
        }
        if (!this.c && this.d[0] != -1) {
            aM_.player.getInventory().selectedSlot = this.d[0];
            if (this.d[1] != -1) {
                Delta.h().d().v().a().a(7, this.d[1], 1);
            }
            this.d[0] = -1;
            this.d[1] = -1;
        }
    }

    private void q() {
        int invSlot;
        boolean inHand = aM_.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE;
        if (!inHand) {
            int hotbarSlot = InventoryUtil.a(Items.EXPERIENCE_BOTTLE, true);
            if (hotbarSlot != -1) {
                if (aM_.player.getInventory().selectedSlot != hotbarSlot) {
                    aM_.player.getInventory().selectedSlot = hotbarSlot;
                    return;
                }
                return;
            } else {
                if (Delta.h().d().v().a().a().isEmpty() && this.c && (invSlot = InventoryUtil.b(Items.EXPERIENCE_BOTTLE)) != -1) {
                    if (this.d[1] == -1) {
                        this.d[1] = invSlot;
                    }
                    if (aM_.player.getInventory().selectedSlot != 7) {
                        aM_.player.getInventory().selectedSlot = 7;
                    }
                    Delta.h().d().v().a().a(invSlot, 7, 1);
                    return;
                }
                return;
            }
        }
        float t = aM_.player.age + aM_.getRenderTickCounter().getTickDelta(false);
        float smoothYaw = ((float) ((((Math.sin(t * 0.8f) * 11.0d) + (Math.sin((((double) t) * 0.04000000011823444d) + 17.20000385061287d) * 1.5d)) + (Math.sin((((double) t) * 0.10999997043280933d) + 5.800000963109878d) * 3.0d)) + (Math.sin((((double) t) * 0.07000004669766619d) + 12.300002384186381d)))) / 3.0f;
        float smoothPitch = (float) (Math.sin(((double) t) * 0.10000000392993033d) + (Math.sin((((double) t) * 0.029999989348000328d) + 54.099982886210135d) * 0.5d));
        Delta.h().d().k().a(new Rotation(Look.b() + smoothYaw, 86.0f + smoothPitch), 70.0f, 1, 3);
        if (Rotation.b().d() > 83.0f) {
            aM_.interactionManager.interactItem(aM_.player, Hand.MAIN_HAND);
            aM_.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private void d(boolean value) {
        this.c = value;
        if (!value || aM_.player == null) {
            return;
        }
        this.d[0] = aM_.player.getInventory().selectedSlot;
    }

    @Override
    public void c() {
        super.c();
        this.c = false;
        this.d[0] = -1;
        this.d[1] = -1;
    }
}
