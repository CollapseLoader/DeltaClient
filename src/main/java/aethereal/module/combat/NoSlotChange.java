package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;

@ModuleRegister(a = "No Slot Change", b = "Не даёт серверу принудительно менять активный слот в хотбаре", c = Category.Combat)
public class NoSlotChange extends Module {
    @EventTarget
    public void a(PacketEvent event) {
        if (event.c() && (event.d() instanceof UpdateSelectedSlotS2CPacket)) {
            aM_.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(aM_.player.getInventory().selectedSlot));
            event.a(true);
        }
    }
}
