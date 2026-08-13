package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.util.Look;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleRegister(a = "Velocity", b = "Не позволяет игрокам откидывать вас", c = Category.Combat)
public class Velocity extends Module {
    private final ModeSetting b = new ModeSetting("Режим анти-отбрасывания", "Легитный", "Обычный", "Легитный");
    private final BooleanSetting c = new BooleanSetting("Прыгать в легит", true).a(() -> {
        return Boolean.valueOf(this.b.l("Легитный"));
    });
    private final BooleanSetting d = new BooleanSetting("Легитный", true).a(() -> {
        return Boolean.valueOf(this.b.l("Легитный"));
    });
    private Vec3d e = Vec3d.ZERO;
    private int f;

    public Velocity() {
        a(this.b, this.c, this.d);
    }

    @Override
    public void c() {
        super.c();
        this.e = Vec3d.ZERO;
        this.f = 0;
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (!event.c() || aM_.player == null) {
            return;
        }
        EntityDamageS2CPacket class_8143VarD = (EntityDamageS2CPacket) event.d();
        if (class_8143VarD instanceof EntityDamageS2CPacket) {
            EntityDamageS2CPacket damage = class_8143VarD;
            if (damage.entityId() == aM_.player.getId()) {
                DamageSource source = damage.createDamageSource(aM_.world);
                boolean player = source.getAttacker() instanceof PlayerEntity;
                this.f = player ? aM_.player.age : 0;
                if (!player) {
                    this.e = Vec3d.ZERO;
                }
            }
        }
        EntityVelocityUpdateS2CPacket class_2743VarD = (EntityVelocityUpdateS2CPacket) event.d();
        if (class_2743VarD instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacket packet = class_2743VarD;
            if (packet.getEntityId() == aM_.player.getId()) {
                if (!this.b.l("Обычный")) {
                    this.e = new Vec3d(packet.getVelocityX(), 0.0d, packet.getVelocityZ());
                } else {
                    event.a(true);
                }
            }
        }
    }

    @EventTarget
    public void a(InputEvent event) {
        float f;
        float f2;
        if (aM_.player.hurtTime <= 0) {
            this.e = Vec3d.ZERO;
            return;
        }
        if (!this.b.l("Легитный") || this.e.lengthSquared() == 0.0d || aM_.player.age - this.f > 10) {
            return;
        }
        double angle = MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(-this.e.z, -this.e.x)) - 90.0d) - ((double) Look.b()));
        if (this.d.c().booleanValue() && aM_.options.forwardKey.isPressed() && Math.abs(angle) >= 140.0d) {
            return;
        }
        if (angle <= -45.0d || angle >= 45.0d) {
            f = (angle > 135.0d || angle < -135.0d) ? -1.0f : 0.0f;
        } else {
            f = 1.0f;
        }
        event.a(f);
        if (angle < 45.0d || angle > 135.0d) {
            f2 = (angle > -45.0d || angle < -135.0d) ? 0.0f : 1.0f;
        } else {
            f2 = -1.0f;
        }
        event.b(f2);
        event.b(this.c.c().booleanValue() && aM_.player.isOnGround());
    }
}
