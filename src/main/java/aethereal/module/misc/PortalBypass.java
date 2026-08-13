package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PortalEvent;

@ModuleRegister(a = "Portal Bypass", b = "Позволяет открывать окна, находясь в портале", c = Category.Misc)
public class PortalBypass extends Module {
    @EventTarget
    public void a(PortalEvent event) {
        event.b(false);
    }
}
