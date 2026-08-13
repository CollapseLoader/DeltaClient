package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.handler.UseableHandler;
import aethereal.setting.BooleanSetting;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import platform.inject.accessors.ItemCooldownManagerAccessor;

import java.util.List;

@ModuleRegister(a = "Mace Helper", b = "Автоматизирует действия при использовании булавы", c = Category.Combat)
public class MaceHelper extends Module {
    public int d;
    public boolean e;
    public final BooleanSetting b = new BooleanSetting("Усиление урона", true);
    public final BooleanSetting c = new BooleanSetting("Авто-переключение булавы", false);
    int[] f = {-1, -1};

    public MaceHelper() {
        a(this.b, this.c);
    }

    @Generated
    public BooleanSetting q() {
        return this.b;
    }

    @Generated
    public BooleanSetting r() {
        return this.c;
    }

    @Generated
    public int s() {
        return this.d;
    }

    @Generated
    public boolean t() {
        return this.e;
    }

    @Generated
    public int[] u() {
        return this.f;
    }

    @EventTarget
    public void a(TickEvent event) {
        Vec3d landing;
        this.d--;
        List<UseableHandler.a> tasks = Delta.h().d().v().b().a();
        if ((this.d <= 198 && aM_.player.isOnGround()) || aM_.player.isTouchingWater() || aM_.player.age < 5) {
            if (this.e && tasks.isEmpty()) {
                if (this.f[1] > 8) {
                    Delta.h().d().v().a().a(this.f[0], this.f[1], 1);
                } else {
                    aM_.player.getInventory().selectedSlot = this.f[0];
                }
                this.e = false;
                this.f = new int[]{-1, -1};
            }
            this.d = 0;
        }
        if (!tasks.isEmpty() && tasks.getFirst().a().getItem() == Items.WIND_CHARGE) {
            this.d = InterfaceC0020Opcode.aN;
        }
        int hotbar = InventoryUtil.a(Items.MACE, true);
        int slotMace = InventoryUtil.a(Items.MACE, false);
        if (!this.c.c().booleanValue() || slotMace == -1) {
            return;
        }
        if (!ServerUtil.a.a() || ServerUtil.e()) {
            ItemCooldownManagerAccessor cooldowns = (ItemCooldownManagerAccessor) aM_.player.getItemCooldownManager();
            Object entry = cooldowns.getEntries().get(aM_.player.getItemCooldownManager().getGroup(Items.MACE.getDefaultStack()));
            if (entry == null || ((platform.inject.accessors.ItemCooldownEntryAccessor) entry).getEndTick() - cooldowns.getTick() <= 10) {
                Aura aura = Delta.h().d().t().B();
                TriggerBot triggerBot = Delta.h().d().t().X();
                boolean fromAura = aura.s() != null;
                LivingEntity target = fromAura ? aura.s() : triggerBot.s();
                if (target == null || target.isBlocking() || aM_.player.isOnGround() || MaceUtil.a() || this.e || aM_.player.fallDistance <= 0.0f || !tasks.isEmpty() || !Delta.h().d().v().a().a().isEmpty() || Math.hypot(target.getPos().x - aM_.player.getPos().x, target.getPos().z - aM_.player.getPos().z) > 6.0d) {
                    return;
                }
                if ((fromAura ? aura.b : triggerBot.d) <= 1 || (landing = MaceUtil.a(aM_.player, aM_.world).orElse(null)) == null || aM_.player.getY() + aM_.player.getVelocity().y <= landing.getY() || aM_.player.getY() - landing.getY() <= 3.5d) {
                    return;
                }
                this.e = true;
                int[] iArr = new int[2];
                iArr[0] = aM_.player.getInventory().selectedSlot;
                iArr[1] = hotbar != -1 ? hotbar : slotMace;
                this.f = iArr;
                if (hotbar == -1) {
                    Delta.h().d().v().a().a(slotMace, aM_.player.getInventory().selectedSlot, 1);
                } else {
                    aM_.player.getInventory().selectedSlot = hotbar;
                }
            }
        }
    }
}
