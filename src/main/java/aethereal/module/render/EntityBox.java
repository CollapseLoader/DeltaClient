package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.util.MathUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

@ModuleRegister(name = "Entity Box", description = "Отображает боксы вокруг сущностей", category = Category.Render)
public class EntityBox extends Module {
    private final ModeSetting visualMode = new ModeSetting("Тип визуализации", "Квадрат", "Квадрат", "Углы", "Заливка", "Отключен");
    private final ModeSetting colorSource = new ModeSetting("Источник цвета", "Клиентский", "Клиентский", "Статичный");
    private final ModeSetting healthBarMode = (ModeSetting) new ModeSetting("Бар здоровья", "Отключен", "Отключен", "Стандартный").a(() -> {
        return Boolean.valueOf(this.visualMode.l("Квадрат") || this.visualMode.l("Углы"));
    });
    private final ColorSetting colorSetting = (ColorSetting) new ColorSetting("Цвет визуализации", Integer.valueOf(ColorUtil.convertToARGB(255, 255, 255, 255))).a(() -> {
        return Boolean.valueOf(this.colorSource.l("Статичный"));
    });

    public EntityBox() {
        a(this.visualMode, this.colorSource, this.healthBarMode, this.colorSetting);
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        if (this.visualMode.l("Заливка")) {
            if (event.c()) {
                for (Entity entity : mc.world.getEntities()) {
                    if (shouldRender(entity)) {
                        event.e().a(event.h(), entity.getBoundingBox().offset(MathUtil.a(entity, event.g()).subtract(entity.getPos())), this.colorSource.l("Статичный") ? this.colorSetting.c().intValue() : Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), 0.75f);
                    }
                }
                return;
            }
            return;
        }
        if (event.b()) {
            if (this.visualMode.l("Квадрат") || this.visualMode.l("Углы")) {
                Draw2DProcessor draw = event.d();
                for (Entity entity : mc.world.getEntities()) {
                    Box box = shouldRender(entity) ? entity.getBoundingBox().offset(MathUtil.a(entity, event.g()).subtract(entity.getPos())) : null;
                    float[] bounds = box == null ? null : ProjectUtil.getBounds(box);
                    if (bounds != null) {
                        LivingEntity living = entity instanceof LivingEntity ? (LivingEntity) entity : null;
                        boolean healthBar = !this.healthBarMode.l("Отключен") && living != null;
                        float percent = healthBar ? Math.min(Math.max(0.0f, ServerUtil.a.a(living)) / Math.max(1.0f, living.getMaxHealth()), 1.0f) : 0.0f;
                        int healthColor = ColorUtil.lerpColorValue(ColorUtil.convertToARGB(255, 0, 0, 255), ColorUtil.convertToARGB(0, 255, 0, 255), percent);
                        int color = this.colorSource.l("Статичный") ? this.colorSetting.c().intValue() : ColorUtil.combineColorWithAlpha(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor(), 255);
                        drawBox(draw, event, bounds[0], bounds[1], bounds[2], bounds[3], color, this.visualMode.l("Углы"), healthBar, percent, healthColor);
                    }
                }
            }
        }
    }

    private void drawBox(Draw2DProcessor draw, DrawEvent event, float minX, float minY, float maxX, float maxY, int color, boolean corners, boolean healthBar, float healthPercent, int healthColor) {
        float width = maxX - minX;
        float height = maxY - minY;
        if (width <= 0.0f || height <= 0.0f) {
            return;
        }
        float line = 0.75f;
        float outline = 1.75f;
        int outlineColor = ColorUtil.convertToARGB(0, 0, 0, 255);
        if (corners) {
            float length = Math.min(width, height) * 0.25f;
            drawLine(draw, event, minX, minY, length, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, minX, minY, 0.0f, length, line, outline, color, outlineColor);
            drawLine(draw, event, maxX, minY, -length, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, maxX, minY, 0.0f, length, line, outline, color, outlineColor);
            drawLine(draw, event, minX, maxY, length, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, minX, maxY, 0.0f, -length, line, outline, color, outlineColor);
            drawLine(draw, event, maxX, maxY, -length, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, maxX, maxY, 0.0f, -length, line, outline, color, outlineColor);
        } else {
            drawLine(draw, event, minX, minY, width, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, minX, maxY, width, 0.0f, line, outline, color, outlineColor);
            drawLine(draw, event, minX, minY, 0.0f, height, line, outline, color, outlineColor);
            drawLine(draw, event, maxX, minY, 0.0f, height, line, outline, color, outlineColor);
        }
        if (healthBar) {
            float barX = minX - 3.0f;
            draw.a(event.h(), barX - 0.5f, minY - 0.5f, 1.75f, height + 1.0f, 0.0f, outlineColor);
            draw.a(event.h(), barX, minY + (height * (1.0f - healthPercent)), 0.75f, height * healthPercent, 0.0f, healthColor);
        }
    }

    private void drawLine(Draw2DProcessor draw, DrawEvent event, float x, float y, float lengthX, float lengthY, float line, float outline, int color, int outlineColor) {
        float left = Math.min(x, x + lengthX);
        float top = Math.min(y, y + lengthY);
        float width = lengthX == 0.0f ? line : Math.abs(lengthX);
        float height = lengthY == 0.0f ? line : Math.abs(lengthY);
        if (lengthX == 0.0f) {
            left -= line * 0.5f;
        }
        if (lengthY == 0.0f) {
            top -= line * 0.5f;
        }
        float outlineOffset = (outline - line) * 0.5f;
        draw.a(event.h(), left - outlineOffset, top - outlineOffset, width + (outlineOffset * 2.0f), height + (outlineOffset * 2.0f), 0.0f, outlineColor);
        draw.a(event.h(), left, top, width, height, 0.0f, color);
    }

    private boolean shouldRender(Entity entity) {
        if ((entity instanceof PlayerEntity) || (entity instanceof ItemEntity)) {
            return (entity == mc.player && mc.options.getPerspective().isFirstPerson()) ? false : true;
        }
        return false;
    }
}
