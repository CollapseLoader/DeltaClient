package aethereal.module.combat;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;

import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.util.ProjectUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

public class AimHandler extends BaseHandler implements Interface {
    private final AnimationUtil b = new AnimationUtil();
    private LivingEntity target;

    public AnimationUtil getAnimation() {
        return this.b;
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        this.b.a(0.0f, 1.0f, 0.25f, EasingList.g, event.g());
        float alpha = this.b.c();
        if (event.b() && this.target != null && alpha > 0.0f) {
            Vec3d real = getInterpolatedPosition(this.target, event.g());
            Vector2f screen = ProjectUtil.project(real.x, real.y, real.z);
            if (!ProjectUtil.isOnScreen(screen)) {
                return;
            }
            float distance = (float) mc.player.getEyePos().distanceTo(real);
            float size = ((float) Math.max(28.0d, 40.0d - (((double) distance) * 0.7000002488091963d)))
                    * (1.2f - (0.2f * alpha));
            event.h().push();
            event.h().translate(screen.x(), screen.y(), 0.0f);
            event.h().multiply(RotationAxis.POSITIVE_Z
                    .rotationDegrees(((float) Math.sin(System.currentTimeMillis() / 820.0d)) * 350.0f));
            event.getDraw2DProcessor().a(event.h(), Identifier.of("delta", "pictures/marker.png"), (-size) / 2.0f, (-size) / 2.0f, size,
                    size, 0.0f, ColorUtil.applyAlphaToColor(-1, alpha * 0.8f));
            event.h().pop();
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        ProjectileHelper projectile = Delta.getInstance().getModuleProcessor().t().D();
        LivingEntity current = null;
        if (projectile.m() && projectile.isChargingProjectile()) {
            current = projectile.getPrimaryTarget();
        }
        boolean visible = current != null;
        if (visible) {
            this.target = current;
        }
        this.b.a(visible);
        if (!visible && this.b.a() <= 0.0f) {
            this.target = null;
        }
    }

    private Vec3d getInterpolatedPosition(LivingEntity entity, float delta) {
        return new Vec3d(MathHelper.lerp(delta, entity.prevX, entity.getX()),
                MathHelper.lerp(delta, entity.prevY, entity.getY()) + (((double) entity.getHeight()) / 2.0d),
                MathHelper.lerp(delta, entity.prevZ, entity.getZ()));
    }
}
