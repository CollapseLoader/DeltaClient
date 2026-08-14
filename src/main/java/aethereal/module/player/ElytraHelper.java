package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.util.InventoryUtil;
import lombok.Generated;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

import java.util.Objects;

@ModuleRegister(a = "Elytra Helper", b = "Выполняет действия с элитрой по нажатию назначенной клавиши", c = Category.Player)
public class ElytraHelper extends Module implements Interface {
    private final BooleanSetting b = new BooleanSetting("Автостарт после свапа", false);
    private final BooleanSetting c;
    private final BooleanSetting d;
    private final BindSetting e;
    private final BindSetting f;
    private boolean g;
    private boolean h;
    private int i;

    public ElytraHelper() {
        BooleanSetting booleanSetting = new BooleanSetting("Использовать /fly при свапе", false);
        BooleanSetting booleanSetting2 = this.b;
        Objects.requireNonNull(booleanSetting2);
        this.c = booleanSetting.a(booleanSetting2::c);
        BooleanSetting booleanSetting3 = new BooleanSetting("Автофейерверк", false);
        BooleanSetting booleanSetting4 = this.b;
        Objects.requireNonNull(booleanSetting4);
        this.d = booleanSetting3.a(booleanSetting4::c);
        this.e = new BindSetting("Кнопка фейерверка", -1).a(() -> {
            z();
        });
        this.f = new BindSetting("Кнопка переключения", -1).a(() -> {
            int slot = mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA ? InventoryUtil.a() : InventoryUtil.b(Items.ELYTRA);
            Delta.h().d().v().a().b(slot, 1, 1);
            if (this.b.c().booleanValue() && InventoryUtil.b(Items.ELYTRA) == slot) {
                this.i = mc.player.age;
                this.g = true;
            }
        });
        a(this.f, this.e, this.b, this.c, this.d);
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
    public BooleanSetting s() {
        return this.d;
    }

    @Generated
    public BindSetting t() {
        return this.e;
    }

    @Generated
    public BindSetting u() {
        return this.f;
    }

    @Generated
    public boolean v() {
        return this.g;
    }

    @Generated
    public boolean w() {
        return this.h;
    }

    @Generated
    public int x() {
        return this.i;
    }

    @EventTarget
    public void a(InputEvent event) {
        if (mc.player.age < 5) {
            this.g = false;
        } else if (this.g && y()) {
            b(event);
        }
    }

    private boolean y() {
        if (!this.c.c().booleanValue() || this.h) {
            return true;
        }
        if (this.i + 1 == mc.player.age) {
            mc.player.networkHandler.sendChatCommand("fly");
        }
        if (mc.player.getAbilities().allowFlying && this.i <= mc.player.age && mc.player.isOnGround()) {
            double x = mc.player.getX();
            double y = mc.player.getY() + 0.19999997317790985d;
            double z = mc.player.getZ();
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, mc.player.horizontalCollision));
            mc.player.setPosition(x, y, z);
            this.i = mc.player.age + 9;
        }
        if (mc.player.getAbilities().allowFlying && !mc.player.isOnGround()) {
            mc.player.getAbilities().flying = true;
            mc.player.setVelocity(0.0d, 0.0d, 0.0d);
            mc.player.sendAbilitiesUpdate();
            this.g = false;
        }
        if (b(15) || (b(3) && mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA)) {
            this.g = false;
            return false;
        }
        return false;
    }

    private void b(InputEvent event) {
        boolean wearingElytra = mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA;
        if (!mc.player.isGliding() && wearingElytra) {
            event.b(mc.player.age % 2 == 0);
            this.i = mc.player.age;
            if (!this.d.c().booleanValue()) {
                this.g = false;
            }
        }
        if (mc.player.isGliding() && this.d.c().booleanValue() && !mc.player.isTouchingWater() && !mc.player.isOnGround() && b(2)) {
            z();
            this.g = false;
        }
        if (b(10)) {
            this.g = false;
        }
    }

    private void z() {
        if (mc.player.isGliding()) {
            Delta.h().d().v().b().a(Items.FIREWORK_ROCKET.getDefaultStack());
        }
    }

    private boolean b(int delay) {
        return this.i + delay < mc.player.age;
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.c()) {
            if (event.d() instanceof GameMessageS2CPacket chat) {
                if (this.g && chat.content().getString().contains("Эту команду могут писать только донатеры выше рангом")) {
                    this.h = true;
                }
            }
        }
    }

    @Override
    public void c() {
        super.c();
        this.g = false;
    }
}
