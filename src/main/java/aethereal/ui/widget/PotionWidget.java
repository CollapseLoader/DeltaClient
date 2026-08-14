package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.event.DrawEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.notification.Notification;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.ui.element.DragInfo;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import org.joml.Vector4f;

import java.util.*;

public class PotionWidget extends Widget implements Interface {
    private final BooleanSetting f;
    private final StatusEffectInstance g;

    public PotionWidget() {
        super(new DragInfo("Зелья", 0.0f, 0.0f, 0.0f, 0.0f));
        this.f = new BooleanSetting("Боковое отображение", false);
        this.g = new StatusEffectInstance(StatusEffects.SPEED, 1200, 0);
        j().a(this);
        a(this.f);
    }

    @Override
    public void a(DrawEvent event) {
        d().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        Iterator<StatusEffectInstance> it = k().iterator();
        while (it.hasNext()) {
            ((IStatusEffectInstance) it.next()).getAnimation().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        }
        if (this.f.c().booleanValue()) {
            d(event);
        } else {
            c(event);
        }
        j().a(this.f.c().booleanValue() ? 2 : 0);
    }

    private void c(DrawEvent event) {
        float x = j().a();
        float y = j().b();
        float targetWidth = 14.5f + Fonts.e.a("Potion-list", this.e) + 5.0f + 2.0f;
        float contentY = y + this.d + 3.0f;
        boolean active = false;
        Iterator<StatusEffectInstance> it = k().iterator();
        while (it.hasNext()) {
            IStatusEffectInstance iStatusEffectInstance = (IStatusEffectInstance) it.next();
            if (iStatusEffectInstance.getAnimation().c() > 0.0f) {
                active = true;
                String name = Text.translatable(iStatusEffectInstance.getEffectType().value().getTranslationKey()).getString() + " " + (iStatusEffectInstance.getAmplifier() + 1);
                targetWidth = Math.max(targetWidth, 19.0f + Fonts.e.a(name, 6.5f) + 8.0f + Fonts.e.a(iStatusEffectInstance.getDuration() > 1000000 ? "∞" : ((iStatusEffectInstance.getDuration() / 20) / 60) + ":" + String.format("%02d", Integer.valueOf((iStatusEffectInstance.getDuration() / 20) % 60)), 6.5f) + 5.0f + 2.0f);
            }
        }
        float width = MathUtil.c(j().f(), targetWidth, 0.5f);
        j().c(width);
        if (a() > 0.0f) {
            a(event, "E", "Potion-list", width, a());
        }
        Iterator<StatusEffectInstance> it2 = k().iterator();
        while (it2.hasNext()) {
            IStatusEffectInstance iStatusEffectInstance2 = (IStatusEffectInstance) it2.next();
            float animation = iStatusEffectInstance2.getAnimation().c() * a();
            if (animation > 0.0f) {
                String name2 = Text.translatable(iStatusEffectInstance2.getEffectType().value().getTranslationKey()).getString() + " " + (iStatusEffectInstance2.getAmplifier() + 1);
                int seconds = iStatusEffectInstance2.getDuration() / 20;
                String duration = iStatusEffectInstance2.getDuration() > 1000000 ? "∞" : (seconds / 60) + ":" + String.format("%02d", Integer.valueOf(seconds % 60));
                float offsetX = (-8.0f) * (1.0f - animation);
                float offsetY = -(1.0f - animation);
                float drawY = contentY + offsetY;
                float durationWidth = Fonts.e.a(duration, 6.5f);
                float textY = (drawY + ((11.5f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f;
                a(event, x + offsetX, drawY, width, 11.5f, false, animation);
                a(event, x + offsetX + 15.0f, drawY, 11.5f, animation);
                event.e().a(event.i(), mc.getStatusEffectSpriteManager().getSprite(iStatusEffectInstance2.getEffectType()), x + offsetX + 5.0f, drawY + 2.0f, 0.0f, 0.4f, animation);
                Fonts.e.a(event.h(), name2, x + offsetX + 19.0f, textY, 6.5f, ColorUtil.a(iStatusEffectInstance2.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL ? ColorUtil.a(255, InterfaceC0020Opcode.cG, InterfaceC0020Opcode.cG, 255) : -1, animation));
                Fonts.e.a(event.h(), duration, ((((x + offsetX) + width) - 5.0f) - durationWidth) - 1.0f, textY, 6.5f, ColorUtil.a(-1, 0.55f * animation));
                contentY += 13.5f * animation;
            }
        }
        j().d(active ? (contentY - y) - 2.0f : this.d);
        super.a(event);
    }

    private void d(DrawEvent event) {
        int primary = Delta.h().d().o().a(ThemeInfo.PRIMARY).a();
        int visibleCount = 0;
        Iterator<StatusEffectInstance> it = k().iterator();
        while (it.hasNext()) {
            if (((IStatusEffectInstance) it.next()).getAnimation().c() > 0.0f) {
                visibleCount++;
            }
        }
        float posY = (mc.getWindow().getScaledHeight() - ((visibleCount * 26.0f) + ((visibleCount - 1) * 2.0f))) / 2.0f;
        float contentY = posY;
        float maxWidth = 0.0f;
        Iterator<StatusEffectInstance> it2 = k().iterator();
        while (it2.hasNext()) {
            IStatusEffectInstance iStatusEffectInstance = (IStatusEffectInstance) it2.next();
            float animation = iStatusEffectInstance.getAnimation().c() * a();
            if (animation > 0.0f) {
                boolean harmful = iStatusEffectInstance.getEffectType().value().getCategory() == StatusEffectCategory.HARMFUL;
                String name = Text.translatable(iStatusEffectInstance.getEffectType().value().getTranslationKey()).getString() + " " + (iStatusEffectInstance.getAmplifier() + 1);
                int seconds = iStatusEffectInstance.getDuration() / 20;
                String duration = iStatusEffectInstance.getDuration() > 1000000 ? "∞" : (seconds / 60) + ":" + String.format("%02d", Integer.valueOf(seconds % 60));
                float textWidth = Math.max(Fonts.e.a(name, 7.0f), Fonts.e.a(duration, 6.0f));
                float width = 18.5f + textWidth + 8.0f;
                float drawX = 3.0f - (width * (1.0f - animation));
                float textX = drawX + 13.5f + 6.0f;
                a(event, drawX, contentY, width, 24.0f, true, animation);
                event.e().a(event.i(), mc.getStatusEffectSpriteManager().getSprite(iStatusEffectInstance.getEffectType()), drawX + 3.5f, contentY + 5.75f, 0.0f, 0.6944444f, animation);
                Fonts.e.a(event.h(), name, textX, contentY + 3.5f, 7.0f, ColorUtil.a(harmful ? ColorUtil.a(215, 76, 76, 255) : -1, animation));
                Fonts.e.a(event.h(), duration, textX, contentY + 13.0f, 6.0f, ColorUtil.a(-1, 0.55f * animation));
                int initialDuration = iStatusEffectInstance.getInitialDuration();
                float progress = initialDuration <= 0 ? 1.0f : Math.min(1.0f, iStatusEffectInstance.getDuration() / initialDuration);
                int accent = harmful ? ColorUtil.a(215, 76, 76, 255) : primary;
                event.d().a(event.h(), drawX + 2.0f, (contentY + 24.0f) - 1.5f, width - 4.0f, 1.5f, new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), ColorUtil.a(accent, 0.15f * animation));
                event.d().a(event.h(), drawX + 2.0f, (contentY + 24.0f) - 1.5f, (width - 4.0f) * progress, 1.5f, new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), ColorUtil.a(accent, animation));
                maxWidth = Math.max(maxWidth, width);
                contentY += 28.0f * animation;
            }
        }
        j().a(3.0f);
        j().b(posY);
        j().c(maxWidth);
        j().d((contentY - posY) - 2.0f);
        super.a(event);
    }

    @Override
    public void a(GlobalEvent event) {
        boolean visible = mc.currentScreen instanceof ChatScreen;
        for (StatusEffectInstance effect : k()) {
            if (!effect.getEffectType().equals(StatusEffects.NIGHT_VISION)) {
                ((IStatusEffectInstance) effect).getAnimation().a(effect == this.g ? mc.currentScreen instanceof ChatScreen : effect.getDuration() > 20);
                if (((IStatusEffectInstance) effect).getAnimation().c() > 0.0d) {
                    visible = true;
                }
                if (effect != this.g && effect.getDuration() == 100 && (effect.getEffectType().equals(StatusEffects.STRENGTH) || effect.getEffectType().equals(StatusEffects.SPEED) || effect.getEffectType().equals(StatusEffects.HEALTH_BOOST) || effect.getEffectType().equals(StatusEffects.INVISIBILITY))) {
                    Delta.h().d().m().a(new Notification("E", Text.literal("Эффект ").append(Text.translatable(effect.getEffectType().value().getTranslationKey()).append(" " + (effect.getAmplifier() + 1)).styled(style -> {
                        return style.withColor(Delta.h().d().o().a(ThemeInfo.PRIMARY).a());
                    })).append(Text.literal(" заканчивается")), 2500));
                }
            }
        }
        d().a(visible);
        super.a(event);
    }

    private List<StatusEffectInstance> k() {
        List<StatusEffectInstance> effects = new ArrayList<>(mc.player.getStatusEffects());
        boolean empty = effects.stream().allMatch(effect -> {
            return effect.getEffectType().equals(StatusEffects.NIGHT_VISION);
        });
        if (this.f.c().booleanValue() && empty && ((mc.currentScreen instanceof ChatScreen) || ((IStatusEffectInstance) this.g).getAnimation().c() > 0.0f)) {
            effects.add(this.g);
        }
        effects.sort(Comparator.comparingInt(effect2 -> {
            if (effect2.getEffectType().equals(StatusEffects.STRENGTH)) {
                return 0;
            }
            return effect2.getEffectType().equals(StatusEffects.WEAKNESS) ? 1 : 2;
        }));
        return effects;
    }
}
