package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.handler.UseableHandler;
import aethereal.setting.BooleanSetting;
import aethereal.util.InventoryUtil;
import aethereal.util.ServerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import platform.inject.accessors.ItemCooldownManagerAccessor;

import java.util.List;

@ModuleRegister(name = "Mace Helper", description = "Автоматизирует действия при использовании булавы", category = Category.Combat)
public class MaceHelper extends Module {
    public final BooleanSetting b = new BooleanSetting("Усиление урона", true);
    public final BooleanSetting c = new BooleanSetting("Авто-переключение булавы", false);
    public int d;
    public boolean e;
    int[] f = {-1, -1};

    public MaceHelper() {
        a(this.b, this.c);
    }

    public BooleanSetting q() {
        return this.b;
    }

    public BooleanSetting r() {
        return this.c;
    }

    public int s() {
        return this.d;
    }

    public boolean t() {
        return this.e;
    }

    public int[] u() {
        return this.f;
    }

    @EventTarget
    public void a(TickEvent event) {
        Vec3d landing;
        this.d--;
        List<UseableHandler.UseableTask> tasks = Delta.getInstance().getModuleProcessor().v().getUseableHandler().a();
        if ((this.d <= 198 && mc.player.isOnGround()) || mc.player.isTouchingWater() || mc.player.age < 5) {
            if (this.e && tasks.isEmpty()) {
                if (this.f[1] > 8) {
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(this.f[0], this.f[1], 1);
                } else {
                    mc.player.getInventory().selectedSlot = this.f[0];
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
        if (!ServerUtil.a.a$() || ServerUtil.e()) {
            ItemCooldownManagerAccessor cooldowns = (ItemCooldownManagerAccessor) mc.player.getItemCooldownManager();
            Object entry = cooldowns.getEntries().get(mc.player.getItemCooldownManager().getGroup(Items.MACE.getDefaultStack()));
            if (entry == null || ((platform.inject.accessors.ItemCooldownEntryAccessor) entry).getEndTick() - cooldowns.getTick() <= 10) {
                Aura aura = Delta.getInstance().getModuleProcessor().t().B();
                TriggerBot triggerBot = Delta.getInstance().getModuleProcessor().t().X();
                boolean fromAura = aura.getTarget() != null;
                LivingEntity target = fromAura ? aura.getTarget() : triggerBot.getTarget();
                if (target == null || target.isBlocking() || mc.player.isOnGround() || MaceUtil.a() || this.e || mc.player.fallDistance <= 0.0f || !tasks.isEmpty() || !Delta.getInstance().getModuleProcessor().v().getInventoryHandler().a().isEmpty() || Math.hypot(target.getPos().x - mc.player.getPos().x, target.getPos().z - mc.player.getPos().z) > 6.0d) {
                    return;
                }
                if ((fromAura ? aura.attackCooldown : triggerBot.d) <= 1 || (landing = MaceUtil.a(mc.player, mc.world).orElse(null)) == null || mc.player.getY() + mc.player.getVelocity().y <= landing.getY() || mc.player.getY() - landing.getY() <= 3.5d) {
                    return;
                }
                this.e = true;
                int[] iArr = new int[2];
                iArr[0] = mc.player.getInventory().selectedSlot;
                iArr[1] = hotbar != -1 ? hotbar : slotMace;
                this.f = iArr;
                if (hotbar == -1) {
                    Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(slotMace, mc.player.getInventory().selectedSlot, 1);
                } else {
                    mc.player.getInventory().selectedSlot = hotbar;
                }
            }
        }
    }
}
