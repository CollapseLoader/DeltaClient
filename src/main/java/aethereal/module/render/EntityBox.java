package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
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
import org.joml.Matrix4f;

@ModuleRegister(a = "Entity Box", b = "Отображает боксы вокруг сущностей", c = Category.Render)
public class EntityBox extends Module {
    private final ModeSetting b = new ModeSetting("Тип визуализации", "Квадрат", "Квадрат", "Углы", "Заливка", "Отключен");
    private final ModeSetting c = new ModeSetting("Источник цвета", "Клиентский", "Клиентский", "Статичный");
    private final ModeSetting d = new ModeSetting("Бар здоровья", "Отключен", "Отключен", "Стандартный").a(() -> {
        return Boolean.valueOf(this.b.l("Квадрат") || this.b.l("Углы"));
    });
    private final ColorSetting e = new ColorSetting("Цвет визуализации", Integer.valueOf(ColorUtil.a(255, 255, 255, 255))).a(() -> {
        return Boolean.valueOf(this.c.l("Статичный"));
    });

    public EntityBox() {
        a(this.b, this.c, this.d, this.e);
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (this.b.l("Заливка")) {
            if (event.c()) {
                for (Entity entity : aM_.world.getEntities()) {
                    if (a(entity)) {
                        event.e().a(event.h(), entity.getBoundingBox().offset(MathUtil.a(entity, event.g()).subtract(entity.getPos())), this.c.l("Статичный") ? this.e.c().intValue() : Delta.h().d().o().a(ThemeInfo.PRIMARY).a(), 0.75f);
                    }
                }
                return;
            }
            return;
        }
        if (event.b()) {
            if (this.b.l("Квадрат") || this.b.l("Углы")) {
                Matrix4f matrix = event.i().getMatrices().peek().getPositionMatrix();
                for (LivingEntity class_1309Var : aM_.world.getEntitiesByClass(LivingEntity.class, aM_.player.getBoundingBox().expand(256.0), e -> true)) {
                    Box box = a(class_1309Var) ? class_1309Var.getBoundingBox().offset(MathUtil.a(class_1309Var, event.g()).subtract(class_1309Var.getPos())) : null;
                    float[] bounds = box == null ? null : ProjectUtil.a(box);
                    if (bounds != null) {
                        boolean healthBar = !this.d.l("Отключен") && (class_1309Var instanceof LivingEntity);
                        float percent = healthBar ? Math.min(Math.max(0.0f, ServerUtil.a.a(class_1309Var)) / Math.max(1.0f, class_1309Var.getMaxHealth()), 1.0f) : 0.0f;
                        int healthColor = ColorUtil.b(ColorUtil.a(255, 0, 0, 255), ColorUtil.a(0, 255, 0, 255), percent);
                        event.e().a(matrix, bounds[0], bounds[1], bounds[2], bounds[3], this.c.l("Статичный") ? this.e.c().intValue() : ColorUtil.a(Delta.h().d().o().a(ThemeInfo.PRIMARY).a(), 255), this.b.l("Углы"), healthBar, percent, healthColor);
                    }
                }
            }
        }
    }

    private boolean a(Entity entity) {
        if ((entity instanceof PlayerEntity) || (entity instanceof ItemEntity)) {
            return entity != aM_.player || !aM_.options.getPerspective().isFirstPerson();
        }
        return false;
    }
}
