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

@ModuleRegister(a = "Air Stuck", b = "Позволяет зависнуть в воздухе на месте", c = Category.Movement)
public class AirStuck extends Module implements Interface {
    private final ModeSetting b = new ModeSetting("Режим зависания", "Обычный", "Обычный", "Удаляющий игрока");
    private Vec3d c;

    public AirStuck() {
        a(this.b);
    }

    @Override
    public void b() {
        super.b();
        if (aM_.player != null) {
            this.c = aM_.player.getPos();
            if (this.b.l("Удаляющий игрока")) {
                aM_.player.setRemoved(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void c() {
        super.c();
        if (aM_.player != null && this.b.l("Удаляющий игрока")) {
            ((platform.inject.accessors.EntityInvoker) aM_.player).unset();
            aM_.world.addEntity(aM_.player);
            aM_.player.refreshPositionAfterTeleport(this.c);
        }
        this.c = null;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.c != null) {
            aM_.player.setVelocity(0.0d, 0.0d, 0.0d);
            aM_.player.setPosition(this.c.x, this.c.y, this.c.z);
        }
    }

    @EventTarget
    public void a(GlobalEvent event) {
        if (aM_.player == null || !aM_.player.isRemoved()) {
            return;
        }
        ((platform.inject.accessors.EntityInvoker) aM_.player).baseTickInvoker();
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
