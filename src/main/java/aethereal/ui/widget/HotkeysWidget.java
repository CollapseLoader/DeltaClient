package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.DragInfo;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;

public class HotkeysWidget extends Widget implements Interface {
    public HotkeysWidget() {
        super(new DragInfo("Клавиши", 0.0f, 0.0f, 0.0f, 0.0f));
        j().a(this);
    }

    @Override
    public void a(DrawEvent event) {
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        float x = j().a();
        float y = j().b();
        float targetWidth = 14.5f + Fonts.e.a("Hot-keys", this.e) + 5.0f + 2.0f;
        float contentY = y + this.d + 3.0f;
        float rightWidth = Fonts.a.a("Q", 6.5f);
        boolean active = false;
        for (Module module : Delta.getInstance().getModuleProcessor().t().e()) {
            if (module.p() != -1 && module.f().c() > 0.0f) {
                active = true;
                targetWidth = Math.max(targetWidth, 19.0f + Fonts.e.a(module.j(), 6.5f) + 8.0f + Fonts.e.a(KeyUtil.b(module.p()), 6.5f) + 4.0f + rightWidth + 5.0f + 2.0f);
            }
        }
        float width = MathUtil.c(j().f(), targetWidth, 0.5f);
        j().c(width);
        a(event, "Q", "Hot-keys", width, a());
        for (Module module2 : Delta.getInstance().getModuleProcessor().t().e()) {
            module2.f().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
            float animation = module2.p() != -1 ? module2.f().c() * a() : 0.0f;
            if (animation > 0.0f) {
                float offsetX = (-8.0f) * (1.0f - animation);
                float offsetY = -(1.0f - animation);
                float drawY = contentY + offsetY;
                float bindWidth = Fonts.e.a(KeyUtil.b(module2.p()), 6.5f);
                float rightIconX = ((((x + offsetX) + width) - 5.0f) - rightWidth) - 1.0f;
                float textY = (drawY + ((11.5f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f;
                a(event, x + offsetX, drawY, width, 11.5f, false, animation);
                a(event, x + offsetX + 15.0f, drawY, 11.5f, animation);
                Fonts.a.a(event.h(), module2.l().a(), x + offsetX + 5.0f, (drawY + ((11.5f - Fonts.a.a(6.5f)) / 2.0f)) - 0.15f, 6.5f, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), animation));
                Fonts.e.a(event.h(), module2.j(), x + offsetX + 19.0f, textY, 6.5f, ColorUtil.applyAlphaToColor(-1, animation));
                Fonts.e.a(event.h(), KeyUtil.b(module2.p()), (rightIconX - 4.0f) - bindWidth, textY, 6.5f, ColorUtil.applyAlphaToColor(-1, 0.55f * animation));
                Fonts.a.a(event.h(), "C", rightIconX, drawY + ((11.5f - Fonts.a.a(6.5f)) / 2.0f) + 0.15f, 6.5f, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), animation));
                contentY += 13.5f * animation;
            }
        }
        j().d(active ? (contentY - y) - 2.0f : this.d);
        super.a(event);
    }

    @Override
    public void a(GlobalEvent event) {
        boolean visible = mc.currentScreen instanceof ChatScreen;
        for (Module module : Delta.getInstance().getModuleProcessor().t().e()) {
            if (module.p() != -1 && module.f().c() > 0.0f) {
                visible = true;
                break;
            }
        }
        d().a(visible);
        super.a(event);
    }
}
