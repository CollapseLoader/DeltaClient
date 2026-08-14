package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.AttackEvent;

@ModuleRegister(name = "No Friend Damage", description = "Не позволяет наносить урон вашим друзьям", category = Category.Combat)
public class NoFriendDamage extends Module {
    @EventTarget
    public void a(AttackEvent event) {
        event.a(Delta.h().d().e().d(event.b().getName().getString()));
    }
}
