package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.*;
import aethereal.setting.BooleanSetting;
import aethereal.ui.element.DragInfo;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;

public class TargetWidget extends Widget {
    private final BooleanSetting f;
    private final BooleanSetting g;
    private final AnimationUtil h;
    private final AnimationUtil i;
    private LivingEntity j;
    private String k;

    public TargetWidget() {
        super(new DragInfo("Таргет-худ", 0.0f, 0.0f, 0.0f, 0.0f));
        this.f = new BooleanSetting("Визуализация предметов", true);
        this.g = new BooleanSetting("Отображать при наводке", false);
        this.h = new AnimationUtil();
        this.i = new AnimationUtil();
        this.k = "";
        j().setWidget(this);
        a(this.g, this.f);
    }

    @Override
    public void a(DrawEvent event) {
        String string;
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        if (a() > 0.0f && this.j != null) {
            j().setWidth(100.0f);
            j().setHeight(24.0f);
            float x = j().getClampedX();
            float y = j().getClampedY();
            a(event, x, y, j().getWidth(), j().getHeight(), true, a());
            float headSize = j().getHeight() / 1.35f;
            float headY = y + ((j().getHeight() - headSize) / 2.0f);
            if (this.j instanceof AbstractClientPlayerEntity player) {
                event.getDraw2DProcessor().a(event.h(), x + 5.0f, headY, headSize, headSize, 2.0f, ColorUtil.applyAlphaToColor(-1, a()), 0.125f, 0.125f, 0.125f, 0.125f, Interface.mc.getTextureManager().getTexture(player.getSkinTextures().texture()).getGlId());
            } else if (this.j != null) {
                Fonts.a.a(event.h(), "B", x + 6.5f + ((headSize - 24.0f) / 2.0f), headY + ((headSize - 24.0f) / 2.0f), 24.0f, ColorUtil.applyAlphaToColor(-1, a()));
            }
            float textX = x + 5.0f + headSize + 5.0f;
            StreamerMode streamerMode = Delta.getInstance().getModuleProcessor().t().aE();
            if (streamerMode.m() && streamerMode.r().c().booleanValue()) {
                string = streamerMode.a(this.j.getName().getString());
            } else {
                string = this.j.getName().getString();
            }
            String name = string;
            if (name.length() > 12) {
                Fonts.e.c(event.h(), name, textX, headY, 7.5f, ColorUtil.applyAlphaToColor(-1, a()), Fonts.e.a(name.substring(0, 12), 7.5f));
            } else {
                Fonts.e.a(event.h(), name, textX, headY, 7.5f, ColorUtil.applyAlphaToColor(-1, a()));
            }
            if (this.f.c().booleanValue()) {
                int i = 0;
                for (ItemStack stack : new ItemStack[]{this.j.getEquippedStack(EquipmentSlot.FEET), this.j.getEquippedStack(EquipmentSlot.LEGS), this.j.getEquippedStack(EquipmentSlot.CHEST), this.j.getEquippedStack(EquipmentSlot.HEAD), this.j.getOffHandStack(), this.j.getMainHandStack()}) {
                    if (!stack.isEmpty()) {
                        event.getDraw3DProcessor().a(event.i(), InventoryUtil.a(stack), ((x + j().getWidth()) - 10.0f) - (i * 9), y + j().getHeight(), 0, a(), 0.55f, true);
                        i++;
                    }
                }
            }
            int primary = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor();
            String hpValue = String.valueOf((int) ServerUtil.a.a(this.j));
            if (this.k.isEmpty()) {
                this.k = hpValue;
            }
            float progress = hpValue.equals(this.k) ? 1.0f : this.i.a(0.0f, 1.0f, 0.75f);
            a(event, hpValue, this.k, ((x + j().getWidth()) - 5.0f) - Fonts.e.a(hpValue, 7.0f), headY + 0.5f, 7.0f, primary, progress);
            if (progress >= 0.99f) {
                this.k = hpValue;
                this.i.c(0.0f);
            }
            float targetHP = MathUtil.b(MathUtil.b(ServerUtil.a.a(this.j), 0.0f, this.j.getMaxHealth()) / this.j.getMaxHealth(), 0.0f, 1.0f);
            float lineHP = this.h.a(targetHP, targetHP, 0.5f);
            float alpha = Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.BACKGROUND_HUD).b() * a();
            event.getDraw2DProcessor().a(event.h(), textX, headY + 12.5f, 54.0f, 3.0f, 0.5f, ColorUtil.applyAlphaToColor(ColorUtil.b(primary, 0.3f), a()));
            event.getDraw2DProcessor().a(event.h(), textX, headY + 12.5f, 54.0f * lineHP, 3.0f, 0.5f, ColorUtil.applyAlphaToColor(primary, alpha));
        }
        super.a(event);
    }

    private void a(DrawEvent event, String current, String previous, float right, float y, float size, int color, float progress) {
        float height = Fonts.e.a(size);
        float cursor = right + Fonts.e.a(current, size);
        int i = 0;
        while (i < current.length()) {
            char digit = current.charAt((current.length() - 1) - i);
            char old = i < previous.length() ? previous.charAt((previous.length() - 1) - i) : ' ';
            String value = String.valueOf(digit);
            cursor -= Fonts.e.a(value, size);
            if (digit == old || progress >= 1.0f) {
                Fonts.e.a(event.h(), value, cursor, y, size, ColorUtil.applyAlphaToColor(color, a()));
            } else {
                ScissorUtil.a(event.h(), cursor - 0.5f, y, Fonts.e.a(value, size) + 1.0f, height + 1.0f);
                Fonts.e.a(event.h(), String.valueOf(old), cursor, y - (height * progress), size, ColorUtil.applyAlphaToColor(color, (1.0f - progress) * a()));
                Fonts.e.a(event.h(), value, cursor, y + (height * (1.0f - progress)), size, ColorUtil.applyAlphaToColor(color, progress * a()));
                ScissorUtil.a(event.h());
            }
            i++;
        }
    }

    @Override
    public void a(GlobalEvent event) {
        LivingEntity class_1309Var;
        LivingEntity class_1309Var2;
        LivingEntity targets = Delta.getInstance().getModuleProcessor().t().B().getTarget() != null ? Delta.getInstance().getModuleProcessor().t().B().getTarget() : Delta.getInstance().getModuleProcessor().t().X().getTarget();
        if (this.g.c().booleanValue()) {
            EntityHitResult class_3966Var = Interface.mc.crosshairTarget instanceof EntityHitResult ? (EntityHitResult) Interface.mc.crosshairTarget : null;
            if (class_3966Var instanceof EntityHitResult) {
                EntityHitResult hit = class_3966Var;
                LivingEntity class_1309VarMethod_17782 = hit.getEntity() instanceof LivingEntity ? (LivingEntity) hit.getEntity() : null;
                if (class_1309VarMethod_17782 instanceof PlayerEntity) {
                    LivingEntity class_1309Var3 = class_1309VarMethod_17782;
                    if (class_1309Var3 != Interface.mc.player) {
                        class_1309Var = class_1309Var3;
                    } else {
                        class_1309Var = null;
                    }
                } else {
                    class_1309Var = null;
                }
            } else {
                class_1309Var = null;
            }
        } else {
            class_1309Var = null;
        }
        LivingEntity crosshair = class_1309Var;
        if (targets != null) {
            class_1309Var2 = targets;
        } else if (crosshair != null) {
            class_1309Var2 = crosshair;
        } else {
            class_1309Var2 = Interface.mc.currentScreen instanceof ChatScreen ? Interface.mc.player : null;
        }
        LivingEntity target = class_1309Var2;
        boolean visible = target != null;
        if (target != null) {
            this.j = target;
        }
        d().a(visible);
        if (!visible && d().a() <= 0.0f) {
            this.j = null;
        }
        super.a(event);
    }
}
