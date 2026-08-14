package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.module.combat.Aura;
import aethereal.module.combat.AuraUtil;
import aethereal.util.CounterUtil;
import aethereal.util.Rotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

@ModuleRegister(name = "Elytra Target", description = "Наводит на врага в полёте на элитре и ускоряется фейерверком из второй руки", category = Category.Movement)
public class ElytraTarget extends Module {
    private final CounterUtil b = new CounterUtil();

    @EventTarget
    public void a(TickEvent event) {
        Aura aura = Delta.getInstance().getModuleProcessor().t().B();
        if (aura.m() && mc.player.isGliding()) {
            if (mc.player.getOffHandStack().getItem() != Items.FIREWORK_ROCKET && !Delta.getInstance().getModuleProcessor().t().V().b) {
                if (Delta.getInstance().getModuleProcessor().v().getInventoryHandler().a().isEmpty()) {
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItemByType(Items.FIREWORK_ROCKET, 45, 1);
                }
            } else if ((this.b.a(150L) && mc.player.getVelocity().length() < 1.5d) || aura.attackCooldown == 1) {
                mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
                this.b.b();
            }
            LivingEntity target = aura.getTarget();
            if (target == null) {
                return;
            }
            Vec3d eye = mc.player.getEyePos();
            Vec3d enemy = target.getBoundingBox().getCenter();
            double dx = enemy.x - eye.x;
            double dz = enemy.z - eye.z;
            double horizontal = Math.sqrt((dx * dx) + (dz * dz));
            double nx = horizontal == 0.0d ? 0.0d : dx / horizontal;
            double nz = horizontal == 0.0d ? 0.0d : dz / horizontal;
            double lift = Math.max(0.0d, 3.0d - q());
            Vec3d aim = new Vec3d(enemy.x + (nx * 4.0d), enemy.y + lift, enemy.z + (nz * 4.0d));
            Rotation aimRotation = Rotation.a(eye, aim);
            float Yaw = AuraUtil.a(mc.player.getYaw(), aimRotation.c(), 1.0f);
            float Pitch = AuraUtil.a(mc.player.getPitch(), aura.attackCooldown <= 3 ? 0.0f : aimRotation.d(), aura.attackCooldown <= 3 ? 1.0f : Math.clamp(aura.attackCooldown / 10.0f, 0.0f, 1.0f));
            Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(Yaw, Pitch), 180.0f, 1, 1);
        }
    }

    private double q() {
        Vec3d start = mc.player.getPos();
        Vec3d end = start.subtract(0.0d, 2.0d, 0.0d);
        BlockHitResult hit = mc.world.raycast(new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        if (hit.getType() == HitResult.Type.MISS) {
            return 2.0d;
        }
        return start.getY() - hit.getPos().y;
    }
}
