package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

@ModuleRegister(a = "No Crouch", b = "Убирает замедление от приседания на вашей стороне", c = Category.Movement)
public class NoCrouch extends Module {
    private boolean b;

    @EventTarget
    public void a(InputEvent e) {
        ClientCommandC2SPacket.Mode class_2849Var;
        boolean sneaking = e.e();
        if (sneaking) {
            class_2849Var = ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY;
        } else {
            class_2849Var = this.b ? ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY : null;
        }
        ClientCommandC2SPacket.Mode mode = class_2849Var;
        if (mode != null) {
            aM_.player.networkHandler.sendPacket(new ClientCommandC2SPacket(aM_.player, mode));
        }
        this.b = sneaking;
        e.c(false);
    }
}
