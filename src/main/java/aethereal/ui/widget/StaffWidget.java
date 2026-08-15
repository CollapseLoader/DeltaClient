package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.staff.StaffConstructor;
import aethereal.ui.element.DragInfo;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.PlayerListEntry;

public class StaffWidget extends Widget implements Interface {
    public StaffWidget() {
        super(new DragInfo("Стафф", 0.0f, 0.0f, 0.0f, 0.0f));
        j().setWidget(this);
    }

    @Override
    public void a(DrawEvent event) {
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        float x = j().getClampedX();
        float y = j().getClampedY();
        float targetWidth = 14.5f + Fonts.e.a("Staff-list", this.e) + 5.0f + 2.0f;
        float contentY = y + this.d + 3.0f;
        boolean active = false;
        for (StaffConstructor staff : Delta.getInstance().getModuleProcessor().f().a()) {
            if (staff.b().c() > 0.0f) {
                targetWidth = Math.max(targetWidth, 19.0f + Fonts.e.a(staff.a(), 6.5f) + 8.0f + Fonts.e.a(a(staff.a()) ? "Near" : "Online", 6.5f) + 5.0f + 2.0f);
                active = true;
            }
        }
        float width = MathUtil.c(j().getWidth(), targetWidth, 0.5f);
        j().setWidth(width);
        if (a() > 0.0f) {
            a(event, "i", "Staff-list", width, a());
        }
        for (StaffConstructor staff2 : Delta.getInstance().getModuleProcessor().f().a()) {
            AnimationUtil animationUtil = staff2.b();
            animationUtil.a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
            float animation = animationUtil.c() * a();
            if (animation > 0.0f) {
                float offsetX = (-8.0f) * (1.0f - animation);
                float offsetY = -(1.0f - animation);
                float drawY = contentY + offsetY;
                float textY = (drawY + ((11.5f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f;
                a(event, x + offsetX, drawY, width, 11.5f, false, animation);
                a(event, x + offsetX + 15.0f, drawY, 11.5f, animation);
                PlayerListEntry entry = mc.getNetworkHandler() == null ? null : mc.getNetworkHandler().getPlayerList().stream().filter(e -> {
                    return e.getProfile().getName().equalsIgnoreCase(staff2.a());
                }).findFirst().orElse(null);
                if (entry != null) {
                    event.getDraw2DProcessor().a(event.h(), x + offsetX + 5.0f, drawY + 2.0f, 7.5f, 7.5f, 2.0f, ColorUtil.applyAlphaToColor(-1, animation), 0.125f, 0.125f, 0.125f, 0.125f, mc.getTextureManager().getTexture(entry.getSkinTextures().texture()).getGlId());
                } else {
                    Fonts.a.a(event.h(), "y", x + offsetX + 5.0f, drawY + ((11.5f - Fonts.a.a(8.0f)) / 2.0f), 8.0f, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), animation));
                }
                Fonts.e.a(event.h(), staff2.a(), x + offsetX + 19.0f, textY, 6.5f, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.TEXT).toIntColor(), animation));
                boolean near = a(staff2.a());
                Fonts.e.a(event.h(), near ? "Near" : "Online", ((((x + offsetX) + width) - 5.0f) - Fonts.e.a(near ? "Near" : "Online", 6.5f)) - 1.0f, textY, 6.5f, ColorUtil.applyAlphaToColor(near ? -1529792 : -9711765, animation));
                contentY += 13.5f * animation;
            }
        }
        j().setHeight(active ? (contentY - y) - 2.0f : this.d);
        super.a(event);
    }

    @Override
    public void a(GlobalEvent event) {
        boolean visible = mc.currentScreen instanceof ChatScreen;
        for (StaffConstructor staff : Delta.getInstance().getModuleProcessor().f().a()) {
            staff.b().a((mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerList().stream().anyMatch(e -> {
                return e.getProfile().getName().equalsIgnoreCase(staff.a());
            })) || a(staff.a()));
            if (staff.b().c() > 0.0f) {
                visible = true;
            }
        }
        d().a(visible);
        super.a(event);
    }

    private boolean a(String name) {
        return mc.world != null && mc.world.getPlayers().stream().anyMatch(playerEntity -> {
            return playerEntity.getName().getString().equalsIgnoreCase(name);
        });
    }
}
