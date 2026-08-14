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
    private LivingEntity target;
    private Vec3d targetPos;

    public AimAssistant() {
        a(this.b, this.c, this.d, this.e);
    }

    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public void c() {
        super.c();
        this.target = null;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        TriggerBot trigger = Delta.getInstance().getModuleProcessor().t().X();
        LivingEntity found = trigger.m() ? trigger.getTarget() : findTarget();
        if (found != this.target) {
            this.targetPos = null;
        }
        this.target = found;
    }

    @EventTarget
    public void onLook(LookEvent event) {
        if (!isValidTarget(this.target) || mc.player.isUsingItem()) {
            return;
        }
        if (!this.e.c().booleanValue() || hasWeapon()) {
            Vec3d position = AuraUtil.a(mc.player.getEyePos(), this.target, 3.0d, this.c.c().booleanValue());
            if (position == Vec3d.ZERO) {
                return;
            }
            this.targetPos = this.targetPos == null ? position : this.targetPos.lerp(position, 0.2000000448441151d);
            float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(this.targetPos.z, this.targetPos.x)) - 90.0d);
            float pitch = (float) (-Math.toDegrees(Math.atan2(this.targetPos.y, Math.hypot(this.targetPos.x, this.targetPos.z))));
            float deltaYaw = MathHelper.wrapDegrees(yaw - mc.player.getYaw());
            float deltaPitch = pitch - mc.player.getPitch();
            if (Math.abs(deltaPitch) <= 13.0f && Math.abs(deltaYaw) < 8.0f && AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), 3.0d, this.target, this.c.c().booleanValue())) {
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

    private LivingEntity findTarget() {
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = Vec3d.fromPolar(mc.player.getPitch(), mc.player.getYaw());
        return StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .filter(entity -> isValidTarget(entity) && (this.c.c().booleanValue() || AuraUtil.a(eye, entity, 4.0d)))
                .min(Comparator.comparingDouble(entity2 ->
                        Math.acos(MathHelper.clamp(look.dotProduct(entity2.getBoundingBox().getCenter().subtract(eye).normalize()), -1.0d, 1.0d))))
                .orElse(null);
    }

    private boolean hasWeapon() {
        Item item = mc.player.getMainHandStack().getItem();
        return (item instanceof SwordItem) || (item instanceof AxeItem) || (item instanceof MaceItem);
    }

    private boolean isValidTarget(LivingEntity entity) {
        return entity != null && entity.isAlive() && !entity.isRemoved() && entity != mc.player && AuraUtil.a((Entity) entity, 4.0d + (mc.player.getVelocity().length() * 3.0d)) && isTargetAllowed(entity);
    }

    private boolean isTargetAllowed(LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            return this.b.a("Игроки").c().booleanValue() && (this.b.a("Друзья").c().booleanValue() || !Delta.getInstance().getModuleProcessor().e().d(player.getName().getString()));
        }
        if (entity instanceof MobEntity) {
            return this.b.a("Мобы").c().booleanValue();
        }
        return (entity instanceof AnimalEntity) && this.b.a("Животные").c().booleanValue();
    }
}
