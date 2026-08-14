package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.WillLandEvent;
import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.GUIScreen;
import aethereal.util.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ModuleRegister(a = "Aura", b = "Автоматически атакует цели рядом с вами", c = Category.Combat)
public class Aura extends Module {

    @Generated
    private static final Logger_2 g = LoggerFactory.a(Aura.class);
    private final ModeSetting h = new ModeSetting("Выберите тип наведения", "ФанТайм", "ФанТайм", "ФанТайм ФОВ", "Легит");
    private final MultiModeSetting i = new MultiModeSetting("Цели для атаки", new BooleanSetting("Без брони", true), new BooleanSetting("Враждебные мобы", false), new BooleanSetting("Животные", false), new BooleanSetting("Друзья", false), new BooleanSetting("Игроки", true));
    private final SliderSetting j = new SliderSetting("Дистанция атаки", 3.0f, 0.1f, 6.0f, 0.1f);
    private final SliderSetting k = new SliderSetting("Дополнительная дистанция", 0.5f, 0.1f, 3.0f, 0.1f);
    private final BooleanSetting l = new BooleanSetting("Только критические удары", true);
    private final BooleanSetting m = new BooleanSetting("Адаптивные удары", true).a(() -> {
        return this.l.c();
    });
    private final MultiModeSetting n = new MultiModeSetting("Не бить когда", new BooleanSetting("Используется предмет", true), new BooleanSetting("Открыт контейнер", true), new BooleanSetting("Враг за стеной", true));
    private final BooleanSetting o = new BooleanSetting("Пробитие щита", true);
    private final BooleanSetting p = new BooleanSetting("Умный спринт", false);
    private final ModeSetting q = new ModeSetting("Приоритет цели", "Прицел", "Прицел", "Дистанция", "ХП");
    private final ModeSetting r = new ModeSetting("Коррекция движения", "Фокус", "Фокус", "Свободно");
    private final ModeSetting s = new ModeSetting("Визуализация цели", "Сферы", "Сферы", "Круг", "Тест");
    private final float[] u = new float[30];
    public int b = 0;
    boolean d;
    final float[] c = {-1.0f, -1.0f, -1.0f, -1.0f, 0.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f};
    final int[] e = {-1, -1};
    boolean f = false;
    private LivingEntity t;

    public Aura() {
        a(this.j, this.k, this.h, this.r, this.s, this.q, this.i, this.n, this.l, this.m, this.o, this.p);
    }

    @Generated
    public ModeSetting r() {
        return this.s;
    }

    @Generated
    public LivingEntity s() {
        return this.t;
    }

    @Override
    public void b() {
        if (this.c[9] == -1.0f) {
            this.c[9] = (int) MathUtil.a(9.0f, 13.0f);
        }
        super.b();
        this.c[8] = 2.0f;
        this.c[10] = 0.0f;
        this.c[11] = 0.0f;
        Arrays.fill(this.u, mc.player != null ? mc.player.getPitch() : 0.0f);
        this.t = null;
    }

    @Override
    public void c() {
        super.c();
        this.c[10] = 0.0f;
        this.t = null;
    }

    @EventTarget
    public void a(InputEvent e) {
        if (this.t != null) {
            MoveUtil.a(e, !this.r.l("Фокус") ? Look.b() : this.c[1], 2);
        }
        if (this.c[0] > 0.0f && this.t != null && AuraUtil.a(this.t, this.j.c().floatValue())) {
            e.setForward(0.0f);
            e.setStrafe(0.0f);
            float[] fArr = this.c;
            fArr[0] = fArr[0] - 1.0f;
        }
    }

    @EventTarget
    public void a(GlobalEvent e) {
        if (!b(this.t) || (MaceUtil.a() && !this.d && !mc.player.getItemCooldownManager().isCoolingDown(Items.MACE.getDefaultStack()))) {
            LivingEntity prev = this.t;
            boolean fresh = !b(prev);
            this.t = (fresh && this.n.a("Враг за стеной").c().booleanValue()) ? d(false).or(() -> {
                return d(true);
            }).orElse(null) : v().orElse(null);
            if (this.t != prev && this.t != null) {
                this.c[10] = 0.0f;
                this.c[11] = 0.0f;
                Arrays.fill(this.u, mc.player != null ? mc.player.getPitch() : 0.0f);
            }
        }
        t();
        if (this.t != null) {
            u();
            w();
            u();
            return;
        }
        this.c[8] = 1.0f;
    }

    @EventTarget
    public void a(WillLandEvent e) {
        this.d = e.b() && !mc.player.isOnGround();
    }

    private int a(int from, int to) {
        for (int i = from; i < to; i++) {
            if (mc.player.getInventory().getStack(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }

    private void t() {
        if (this.o.c().booleanValue() && this.t != null && this.t.isBlocking()) {
            return;
        }
        if (this.e[0] != -1) {
            mc.player.getInventory().selectedSlot = this.e[0];
            this.e[0] = -1;
        }
        if (this.e[1] != -1 && Delta.h().d().v().a().a().isEmpty()) {
            Delta.h().d().v().a().a(mc.player.getInventory().selectedSlot, this.e[1], 1);
            this.e[1] = -1;
        }
    }

    private void u() {
        if (!AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), this.j.c().floatValue(), this.t, !this.n.a("Враг за стеной").c().booleanValue())) {
            return;
        }
        if (this.o.c().booleanValue() && this.t.isBlocking()) {
            if (mc.player.getMainHandStack().getItem() instanceof AxeItem) {
                mc.interactionManager.attackEntity(mc.player, this.t);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            int hotbar = a(0, 9);
            if (hotbar != -1) {
                if (mc.player.getInventory().selectedSlot != hotbar) {
                    if (this.e[0] == -1) {
                        this.e[0] = mc.player.getInventory().selectedSlot;
                    }
                    mc.player.getInventory().selectedSlot = hotbar;
                }
            } else {
                int inventory = a(9, 36);
                if (inventory != -1 && this.e[1] == -1 && Delta.h().d().v().a().a().isEmpty()) {
                    this.e[1] = inventory;
                    Delta.h().d().v().a().a(inventory, mc.player.getInventory().selectedSlot, 1);
                }
            }
        }
        if (q()) {
            boolean skip = false;
            if ((Delta.h().d().t().H().e || (mc.player.fallDistance > 2.0f && Delta.h().d().t().H().c.c().booleanValue())) && InventoryUtil.b(Items.MACE) != -1) {
                if (mc.player.fallDistance < 1.5f) {
                    return;
                }
                double landDist = MaceUtil.a(mc.player, mc.world).map(pos -> {
                    return Double.valueOf(pos.distanceTo(this.t.getPos()));
                }).orElse(Double.valueOf(33.0d)).doubleValue();
                boolean hitNow = landDist > 2.0d;
                if ((!this.d && !MaceUtil.b() && Delta.h().d().t().H().b.c().booleanValue() && !hitNow) || !MaceUtil.a() || mc.player.isGliding()) {
                    return;
                } else {
                    skip = true;
                }
            }
            if (((platform.inject.accessors.ClientPlayerEntityAccessor) mc.player).getWasSprinting() && !mc.player.isTouchingWater() && !mc.player.isInLava() && !mc.player.isSwimming() && !mc.player.isOnGround() && !skip) {
                if (!this.p.c().booleanValue()) {
                    ((platform.inject.accessors.ClientPlayerEntityAccessor) mc.player).setWasSprinting(false);
                    mc.player.setSprinting(false);
                    mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    this.c[0] = 1.0f;
                } else {
                    this.c[0] = 1.0f;
                    if (((platform.inject.accessors.ClientPlayerEntityAccessor) mc.player).getWasSprinting()) {
                        return;
                    }
                }
            }
            if (mc.interactionManager != null) {
                this.c[3] = 0.0f;
                mc.interactionManager.attackEntity(mc.player, this.t);
                mc.player.swingHand(Hand.MAIN_HAND);
                this.b = 0;
                this.c[5] = MathUtil.a(8.0f, 10.0f);
                this.c[9] = (int) MathUtil.a(9.0f, 13.0f);
                if (this.c[2] == -1.0f) {
                    this.c[4] = (int) MathUtil.a(30.0f, 35.0f);
                }
                float[] fArr = this.c;
                fArr[2] = fArr[2] + 1.0f;
            }
        }
    }

    public boolean q() {
        if (this.n.a("Используется предмет") != null && this.n.a("Используется предмет").c().booleanValue() && mc.player.isUsingItem() && mc.player.getItemUseTimeLeft() > 0 && this.b >= 8) {
            this.b = 8;
            return false;
        }
        if ((this.n.a("Открыт контейнер") != null && this.n.a("Открыт контейнер").c().booleanValue() && mc.currentScreen != null && !(mc.currentScreen instanceof GUIScreen) && !(mc.currentScreen instanceof AssistantScreen)) || !AuraUtil.a(this.t, this.j.c().floatValue())) {
            return false;
        }
        if (Delta.h().d().t().H().e) {
            if (mc.player.getItemCooldownManager().isCoolingDown(mc.player.getMainHandStack())) {
                return false;
            }
        } else if (mc.player.fallDistance > 1.5f) {
            if (mc.player.getItemCooldownManager().isCoolingDown(mc.player.getMainHandStack()) || this.b <= 3) {
                return false;
            }
        } else if (MaceUtil.a()) {
            if (mc.player.getItemCooldownManager().isCoolingDown(mc.player.getMainHandStack()) || mc.player.getAttackCooldownProgress(0.5f) < 0.9f) {
                return false;
            }
        } else if (mc.player.getAttackCooldownProgress(0.5f) < 0.9f || this.b < 10) {
            return false;
        }
        return AuraUtil.c() || (this.m.c().booleanValue() && mc.player.isOnGround() && !mc.player.input.playerInput.jump()) || !AuraUtil.b();
    }

    private boolean a(LivingEntity entity) {
        if (Delta.h().d().t().G().m() && mc.player.isGliding()) {
            return true;
        }
        return AuraUtil.a((Entity) entity, ((double) (this.j.c().floatValue() + this.k.c().floatValue())) + (mc.player.getVelocity().length() * 3.0d) + ((double) ((InventoryUtil.b(Items.MACE) == -1 || ((double) mc.player.fallDistance) <= 1.5d) ? 0.0f : 1.5f)) + ((double) ((Delta.h().d().t().H().m() && InventoryUtil.b(Items.MACE) != -1 && MaceUtil.a(mc.player, mc.world).map(p -> {
            return Boolean.valueOf(mc.player.getY() - p.getY() > 2.0d);
        }).orElse(false).booleanValue()) ? 10 : 0)));
    }

    private Optional<LivingEntity> v() {
        return d(true);
    }

    private Optional<LivingEntity> d(boolean allowBehindWalls) {
        Comparator<LivingEntity> comparatorComparingDouble;
        Comparator<LivingEntity> order;
        if (mc.world == null || mc.player == null) {
            return Optional.empty();
        }
        Vec3d eye = mc.player.getEyePos();
        double reach = this.j.c().floatValue() + this.k.c().floatValue();
        if (MaceUtil.a()) {
            Vec3d landing = MaceUtil.a(mc.player, mc.world).orElse(null);
            Vec3d landingEye = landing != null ? landing.add(0.0d, mc.player.getStandingEyeHeight(), 0.0d) : null;
            order = Comparator.comparing((LivingEntity e) ->
                    Boolean.valueOf(!AuraUtil.a(eye, e, reach) && (landingEye == null || !AuraUtil.a(landingEye, e, reach)))
            ).thenComparing((LivingEntity e2) ->
                    Boolean.valueOf(mc.player.fallDistance > 1.0f && !c(e2))
            ).thenComparingDouble((LivingEntity v0) -> AuraUtil.a(v0));
        } else {
            switch (this.q.c()) {
                case "Дистанция":
                    comparatorComparingDouble = Comparator.comparingDouble((v0) -> {
                        return AuraUtil.a(v0);
                    });
                    break;
                case "ХП":
                    comparatorComparingDouble = Comparator.comparingDouble((v0) -> {
                        return v0.getHealth();
                    });
                    break;
                default:
                    comparatorComparingDouble = Comparator.comparingDouble(e3 -> {
                        return Math.acos(MathHelper.clamp(Vec3d.fromPolar(mc.player.getPitch(), mc.player.getYaw()).dotProduct(e3.getBoundingBox().getCenter().subtract(eye).normalize()), -1.0d, 1.0d));
                    });
                    break;
            }
            order = comparatorComparingDouble;
        }
        Stream<LivingEntity> stream2 = StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(LivingEntity.class::isInstance)
                .map(LivingEntity.class::cast)
                .filter(e4 -> e4 != mc.player && e4.isAlive())
                .filter(this::a)
                .filter(this::b);
        if (!allowBehindWalls) {
            stream2 = stream2.filter(e5 -> {
                return AuraUtil.a(eye, e5, reach);
            });
        }
        return stream2.min(order);
    }

    private boolean b(LivingEntity entity) {
        if (entity == null || !entity.isAlive() || !a(entity)) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            boolean isFriend = Delta.h().d().e().d(entity.getName().getString());
            boolean naked = Stream.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).noneMatch(slot -> {
                return entity.getEquippedStack(slot).getItem() instanceof ArmorItem;
            });
            if (!a("Игроки")) {
                return false;
            }
            if (isFriend) {
                return a("Друзья");
            }
            return !naked || a("Без брони");
        }
        if ((entity instanceof HostileEntity) || (entity instanceof SlimeEntity) || (entity instanceof FlyingEntity) || (entity instanceof EnderDragonEntity)) {
            return a("Враждебные мобы");
        }
        if ((entity instanceof PassiveEntity) || (entity instanceof GolemEntity) || (entity instanceof AllayEntity) || (entity instanceof AmbientEntity)) {
            return a("Животные");
        }
        return false;
    }

    private boolean a(String name) {
        BooleanSetting setting = this.i.a(name);
        return setting != null && setting.c().booleanValue();
    }

    private boolean c(LivingEntity entity) {
        return Stream.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).anyMatch(s -> {
            return entity.getEquippedStack(s).getItem() instanceof ArmorItem;
        });
    }

    private void w() {
        Vec3d eye = mc.player.getEyePos();
        LivingEntity target = this.t;
        double reach = this.j.c().floatValue();
        boolean throughWalls = this.h.c().contains("ФанТайм") || !this.n.a("Враг за стеной").c().booleanValue();
        Vec3d targetPosition = AuraUtil.a(eye, target, reach, throughWalls);
        float yawToTarget = targetPosition == Vec3d.ZERO ? Look.b() : (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(targetPosition.z, targetPosition.x)) - 90.0d);
        float pitchToTarget = targetPosition == Vec3d.ZERO ? Look.c() : (float) (-Math.toDegrees(Math.atan2(targetPosition.y, Math.hypot(targetPosition.x, targetPosition.z))));
        System.arraycopy(this.u, 0, this.u, 1, 29);
        this.u[0] = pitchToTarget;
        if (this.t != null && this.b >= 2 && ((ServerUtil.a.a(this.t) > 6.0f || this.c[2] > 43.0f) && this.c[2] >= 33.0f && ((this.b == 4 || Math.random() > 0.5d) && (!this.f || !AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), 3.0d, this.t, false))))) {
            ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoAttack();
            if (Math.random() > 0.5d) {
                this.f = !this.f;
            }
            this.c[2] = (int) MathUtil.a(-10.0f, 10.0f);
        }
        boolean skip = (this.n.a("Используется предмет").c().booleanValue() && mc.player.isUsingItem() && mc.player.getItemUseTimeLeft() > 0 && this.b >= 8) || !(this.n.a("Открыт контейнер") == null || !this.n.a("Открыт контейнер").c().booleanValue() || mc.currentScreen == null || (mc.currentScreen instanceof GUIScreen) || (mc.currentScreen instanceof AssistantScreen));
        if ((this.c[3] <= 0.0f && q()) || AuraUtil.a(this.b, this.t, skip)) {
            this.c[3] = 1.0f;
            if (!mc.player.isTouchingWater() && this.p.c().booleanValue() && !mc.player.isOnGround()) {
                this.c[0] = 1.0f;
            }
        }
        if (Delta.h().d().t().F().m() && q() && AuraUtil.a(this.t, 3.0d) && mc.player.isGliding()) {
            Delta.h().d().k().a(new Rotation(yawToTarget, pitchToTarget), 180.0f, 0, 3);
        }
        if (!this.h.c().contains("ФанТайм") && (InventoryUtil.b(Items.MACE) != -1 || (Delta.h().d().t().H().e && mc.player.fallDistance > 3.0f && MaceUtil.a(mc.player, mc.world).map(pos -> {
            return Double.valueOf(pos.distanceTo(mc.player.getPos()));
        }).orElse(Double.valueOf(0.0d)).doubleValue() > 2.0d && AuraUtil.a(this.t, 4.0d + (mc.player.getVelocity().length() * 3.0d))))) {
            float time = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
            float smoothW = ((float) ((((Math.sin(time * 0.31f) * 0.5d) + (Math.sin((time * 0.73f) + 1.1f) * 0.3000000314327426d)) + (Math.sin((time * 1.7f) + 2.6f) * 0.2000000098386085d)) * 8.0d)) / 8.0f;
            float finalYaw = AuraUtil.a(mc.player.getYaw(), yawToTarget, 0.8f);
            float finalPitch = AuraUtil.a(mc.player.getPitch(), pitchToTarget, 0.8f);
            Delta.h().d().k().a(new Rotation(finalYaw + smoothW, finalPitch + smoothW), 180.0f, 1, 2);
        }
        switch (this.h.c()) {
            case "ФанТайм":
            case "ФанТайм ФОВ":
                a(yawToTarget, pitchToTarget, targetPosition);
                break;
            case "Легит":
                b(yawToTarget, pitchToTarget, targetPosition);
                break;
        }
        float[] fArr = this.c;
        fArr[3] = fArr[3] - 1.0f;
        float[] fArr2 = this.c;
        fArr2[5] = fArr2[5] - 1.0f;
        float[] fArr3 = this.c;
        fArr3[8] = fArr3[8] - 1.0f;
        this.c[1] = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(this.t.getZ() - mc.player.getZ(), this.t.getX() - mc.player.getX())) - 90.0d);
    }

    private void a(float yawToTarget, float pitchToTarget, Vec3d vec3d) {
        float time = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float smoothW = (float) ((Math.sin(((double) time) * 0.4000000008323731d) * 3.0d) + (Math.sin((((double) time) * 0.9500002390239708d) + 1.4000004888461306d) * 2.0d));
        float smoothH = (float) ((Math.cos((((double) time) * 0.5d) + 0.7000001555309916d) * 0.5d) + (Math.cos((((double) time) * 0.7800000620494261d) + 3.10000031689524d) * 1.5d));
        float finalPitch = AuraUtil.a(mc.player.getPitch(), this.u[MathHelper.clamp(10 - this.b, 0, 29)] + (smoothH * 1.5f), MathUtil.a(0.1f, 0.5f));
        float finalYaw = AuraUtil.a(mc.player.getYaw(), yawToTarget + smoothW, MathUtil.a(0.1f, 0.4f));
        if (this.c[3] >= 0.0f) {
            if (!AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), this.j.c().floatValue(), this.t, true) && this.c[8] <= 0.0f) {
                finalYaw = yawToTarget;
            }
            if (!AuraUtil.a(yawToTarget, finalPitch, this.j.c().floatValue(), this.t, true) && this.c[8] <= 0.0f) {
                finalPitch = pitchToTarget;
            }
            if (!AuraUtil.a(mc.player.getYaw() + smoothW, mc.player.getYaw() + smoothH, this.j.c().floatValue(), this.t, true) && AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), this.j.c().floatValue(), this.t, true)) {
                smoothW = MathHelper.clamp(smoothW, -0.05f, 0.05f);
                smoothH = MathHelper.clamp(smoothH, -0.05f, 0.05f);
            }
        }
        if (this.b <= 4 && this.c[2] % 2.0f == 0.0f) {
            finalYaw = mc.player.getYaw();
        }
        Delta.h().d().k().a(new Rotation(finalYaw + smoothW, (this.h.c().equals("ФанТайм") ? finalPitch : Look.c()) + smoothH), 220.0f, 1, 1);
    }

    private void b(float yawToTarget, float pitchToTarget, Vec3d vec3d) {
        float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float fSin = ((float) (((Math.sin(t * 0.31f) * 0.5d) + (Math.sin((t * 1.7f) + 2.6f) * 0.2000000098386085d)) * 8.0d)) / 4.0f;
        float smoothW = fSin;
        float smoothH = fSin;
        float finalYaw = AuraUtil.a(mc.player.getYaw(), yawToTarget, MathUtil.a(0.2f, 0.35f));
        float finalPitch = AuraUtil.a(mc.player.getPitch(), pitchToTarget, MathUtil.a(0.15f, 0.25f));
        if (this.c[3] >= 0.0f) {
            finalPitch = AuraUtil.a(mc.player.getPitch(), pitchToTarget, 0.35f);
            smoothH /= 3.0f;
            smoothW /= 3.0f;
            if (!AuraUtil.a(mc.player.getYaw(), mc.player.getPitch(), this.j.c().floatValue(), this.t, true)) {
                finalYaw = AuraUtil.a(mc.player.getYaw(), yawToTarget, MathUtil.a(0.7f, 1.0f));
            }
        }
        if (!AuraUtil.a(finalYaw + smoothW, finalPitch + smoothH, this.j.c().floatValue(), this.t, true) && AuraUtil.a(yawToTarget, pitchToTarget, this.j.c().floatValue(), this.t, true)) {
            smoothW = MathHelper.clamp(smoothW, -0.15f, 0.15f);
            smoothH = MathHelper.clamp(smoothH, -0.15f, 0.15f);
        }
        if (this.c[5] >= 0.0f) {
            smoothW *= 8.0f;
            if (this.b >= 1 && this.c[2] % 5.0f == 0.0f) {
                finalPitch = AuraUtil.a(mc.player.getPitch(), -pitchToTarget, 0.05f);
            }
        }
        Delta.h().d().k().a(new Rotation(finalYaw + smoothW, finalPitch + smoothH), 180.0f, 1, 1);
    }
}
