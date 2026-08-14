package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.LookEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.MaceItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.stream.StreamSupport;

@ModuleRegister(name = "Aim Assistant", description = "Доводит прицел до цели", category = Category.Combat)
public class AimAssistant extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Цели для наведения", new BooleanSetting("Игроки", true), new BooleanSetting("Животные", false), new BooleanSetting("Мобы", false), new BooleanSetting("Друзья", true));
    private final BooleanSetting c = new BooleanSetting("Наводить за стеной", false);
    private final SliderSetting d = new SliderSetting("Порог", 5.0f, 1.0f, 5.0f, 0.25f);
    private final BooleanSetting e = new BooleanSetting("Только с оружием", true);
    private LivingEntity f;
    private Vec3d g;

    public AimAssistant() {
        a(this.b, this.c, this.d, this.e);
    }

    public LivingEntity q() {
        return this.f;
    }

    @Override
    public void c() {
        super.c();
        this.f = null;
    }

    @EventTarget
    public void a(TickEvent event) {
        TriggerBot trigger = Delta.h().d().t().X();
        LivingEntity found = trigger.m() ? trigger.s() : r();
        if (found != this.f) {
            this.g = null;
        }
        this.f = found;
    }

    @EventTarget
    public void a(LookEvent event) {
        if (!a(this.f) || mc.player.isUsingItem()) {
            return;
        }
        if (!this.e.c().booleanValue() || s()) {
            Vec3d position = AuraUtil.a(mc.player.getEyePos(), this.f, 3.0d, this.c.c().booleanValue());
            if (position == Vec3d.ZERO) {
                return;
            }
            this.g = this.g == null ? position : this.g.lerp(position, 0.2000000448441151d);
            float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(this.g.z, this.g.x)) - 90.0d);
            float pitch = (float) (-Math.toDegrees(Math.atan2(this.g.y, Math.hypot(this.g.x, this.g.z))));
            float deltaYaw = MathHelper.wrapDegrees(yaw - mc.player.getYaw());
            float deltaPitch = pitch - mc.player.getPitch();
            if (Math.abs(deltaPitch) <= 13.0f && Math.abs(deltaYaw) < 8.0f && AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), 3.0d, this.f, this.c.c().booleanValue())) {
                deltaPitch = 0.0f;
            }
            float frame = mc.getRenderTickCounter().getLastFrameDuration();
            float ease = MathHelper.clamp(((float) Math.hypot(deltaYaw, deltaPitch)) / 4.0f, 0.0f, 1.0f);
            float speed = this.d.c().floatValue() * frame * ease;
            if (speed <= 0.0f) {
                return;
            }
            float step = Math.min(1.0f, speed / Math.max(Math.abs(deltaYaw), Math.abs(deltaPitch) * 2.0f));
            mc.player.setYaw(mc.player.getYaw() + (deltaYaw * step));
            if (deltaPitch != 0.0f) {
                mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + (deltaPitch * step), -90.0f, 90.0f));
            }
        }
    }

    private LivingEntity r() {
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = Vec3d.fromPolar(mc.player.getPitch(), mc.player.getYaw());
        return StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .filter(entity -> a(entity) && (this.c.c().booleanValue() || AuraUtil.a(eye, entity, 4.0d)))
                .min(Comparator.comparingDouble(entity2 ->
                        Math.acos(MathHelper.clamp(look.dotProduct(entity2.getBoundingBox().getCenter().subtract(eye).normalize()), -1.0d, 1.0d))))
                .orElse(null);
    }

    private boolean s() {
        Item item = mc.player.getMainHandStack().getItem();
        return (item instanceof SwordItem) || (item instanceof AxeItem) || (item instanceof MaceItem);
    }

    private boolean a(LivingEntity entity) {
        return entity != null && entity.isAlive() && !entity.isRemoved() && entity != mc.player && AuraUtil.a((Entity) entity, 4.0d + (mc.player.getVelocity().length() * 3.0d)) && b(entity);
    }

    private boolean b(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return this.b.a("Игроки").c().booleanValue() && (this.b.a("Друзья").c().booleanValue() || !Delta.h().d().e().d(player.getName().getString()));
        }
        if (entity instanceof MobEntity) {
            return this.b.a("Мобы").c().booleanValue();
        }
        return (entity instanceof AnimalEntity) && this.b.a("Животные").c().booleanValue();
    }
}
