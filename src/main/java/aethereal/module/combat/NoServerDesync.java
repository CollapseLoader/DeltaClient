package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

@ModuleRegister(a = "No Server Desync", b = "Не даёт серверу принудительно сбрасывать поворот вашей камеры", c = Category.Combat)
public class NoServerDesync extends Module {
    @EventTarget
    public void a(PacketEvent event) {
        if (event.c() && event.d() instanceof PlayerPositionLookS2CPacket packet) {
            if (packet.change().pitch() != 0.0f && packet.change().yaw() != 0.0f) {
                event.a(true);
            }
        }
    }
}
