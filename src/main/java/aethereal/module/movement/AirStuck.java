package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

@ModuleRegister(name = "Air Stuck", description = "Позволяет зависнуть в воздухе на месте", category = Category.Movement)
public class AirStuck extends Module {
    private final ModeSetting b = new ModeSetting("Режим зависания", "Обычный", "Обычный", "Удаляющий игрока");
    private Vec3d stuckPos;

    public AirStuck() {
        a(this.b);
    }

    @Override
    public void b() {
        super.b();
        if (mc.player != null) {
            this.stuckPos = mc.player.getPos();
            if (this.b.l("Удаляющий игрока")) {
                mc.player.setRemoved(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void c() {
        super.c();
        if (mc.player != null && this.b.l("Удаляющий игрока")) {
            ((platform.inject.accessors.EntityInvoker) mc.player).unset();
            mc.world.addEntity(mc.player);
            mc.player.refreshPositionAfterTeleport(this.stuckPos);
        }
        this.stuckPos = null;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.stuckPos != null) {
            mc.player.setVelocity(0.0d, 0.0d, 0.0d);
            mc.player.setPosition(this.stuckPos.x, this.stuckPos.y, this.stuckPos.z);
        }
    }

    @EventTarget
    public void a(GlobalEvent event) {
        if (mc.player == null || !mc.player.isRemoved()) {
            return;
        }
        ((platform.inject.accessors.EntityInvoker) mc.player).baseTickInvoker();
    }

    @EventTarget
    public void a(InputEvent event) {
        if (this.b.l("Обычный")) {
            event.a(true);
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            if ((event.d() instanceof PlayerMoveC2SPacket) || (event.d() instanceof PlayerInputC2SPacket) || (event.d() instanceof PlayerActionC2SPacket)) {
                event.a(true);
            }
        }
    }
}
