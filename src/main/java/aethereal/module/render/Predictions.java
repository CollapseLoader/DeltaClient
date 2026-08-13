package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.ProjectUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Vector2f;
import platform.inject.accessors.TridentEntityAccessor;

import java.awt.*;
import java.util.*;
import java.util.List;

@ModuleRegister(a = "Predictions", b = "Прогнозирует и отображает траекторию полёта трезубца, стрел и зелий", c = Category.Render)
public class Predictions extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отслеживаемые предметы", new BooleanSetting("Стрелы", true), new BooleanSetting("Трезубцы", true), new BooleanSetting("Эндер жемчуг", true), new BooleanSetting("Зелья", true));
    private final BooleanSetting c = new BooleanSetting("Радужный цвет", false);
    private final Map<Integer, b> d = new HashMap();

    public Predictions() {
        a(this.b, this.c);
    }

    private boolean a(Entity entity) {
        return entity.getX() != entity.prevX || entity.getY() != entity.prevY || entity.getZ() != entity.prevZ;
    }

    @EventTarget
    public void a(DrawEvent event) {
        if (aM_.player == null || aM_.world == null) {
            return;
        }
        if (event.c()) {
            Set<Integer> activeIds = new HashSet<>();
            Box range = aM_.player.getBoundingBox().expand(aM_.options.getViewDistance().getValue().intValue() * 16);
            if (this.b.a("Стрелы").c().booleanValue()) {
                aM_.world.getEntitiesByClass(ArrowEntity.class, range, (v1) -> {
                    return a(v1);
                }).forEach(e -> {
                    a(event, e, Items.ARROW.getDefaultStack(), activeIds);
                });
            }
            if (this.b.a("Трезубцы").c().booleanValue()) {
                aM_.world.getEntitiesByClass(TridentEntity.class, range, e2 -> {
                    return ((TridentEntityAccessor) e2).getReturnTimer() <= 0 && a(e2);
                }).forEach(e3 -> {
                    a(event, e3, Items.TRIDENT.getDefaultStack(), activeIds);
                });
            }
            if (this.b.a("Эндер жемчуг").c().booleanValue()) {
                aM_.world.getEntitiesByClass(EnderPearlEntity.class, range, (v1) -> {
                    return a(v1);
                }).forEach(e4 -> {
                    a(event, e4, Items.ENDER_PEARL.getDefaultStack(), activeIds);
                });
            }
            if (this.b.a("Зелья").c().booleanValue()) {
                aM_.world.getEntitiesByClass(PotionEntity.class, range, (v1) -> {
                    return a(v1);
                }).forEach(e5 -> {
                    a(event, e5, e5.getStack(), activeIds);
                });
            }
            b(event);
            this.d.entrySet().removeIf(entry -> {
                if (activeIds.contains(entry.getKey())) {
                    return false;
                }
                AnimationUtil anim = entry.getValue().d();
                anim.a(false);
                anim.a(0.0f, 1.0f, 0.25f, EasingList.s, event.g());
                return anim.c() <= 0.0f;
            });
        }
        if (event.b()) {
            this.d.values().forEach(info -> {
                a(event, info);
            });
        }
    }

    private void a(DrawEvent event, Entity entity, ItemStack item, Set<Integer> activeIds) {
        List<Vec3d> path = b(entity);
        if (path.size() < 2) {
            return;
        }
        activeIds.add(Integer.valueOf(entity.getId()));
        b existing = this.d.get(Integer.valueOf(entity.getId()));
        AnimationUtil anim = existing != null ? existing.d() : new AnimationUtil();
        anim.a(true);
        anim.a(0.0f, 1.0f, 0.25f, EasingList.s, event.g());
        float alpha = anim.c();
        int primaryColor = Delta.h().d().o().a(ThemeInfo.PRIMARY).a();
        float hueBase = (entity.getUuid().getLeastSignificantBits() & 65535) / 65535.0f;
        int segCount = path.size() - 1;
        for (int i = 0; i < segCount; i++) {
            float max = Math.max(0.0f, Math.min(1.0f, (alpha * segCount) - i));
            if (max <= 0.0f) {
                break;
            }
            float hue = (((hueBase + ((float) ((path.get(i).x * 0.05000000070627959d) + (path.get(i).z * 0.05000000070627959d)))) % 1.0f) + 1.0f) % 1.0f;
            int base = this.c.c().booleanValue() ? (-16777216) | (Color.HSBtoRGB(hue, 1.0f, 1.0f) & 16777215) : primaryColor;
            int lineAlpha = (int) (255.0f * (0.3f + (0.7f * (1.0f - (i / segCount)))) * max * alpha);
            event.e().a(event.h(), path.get(i), path.get(i + 1), null, (base & 16777215) | (lineAlpha << 24), 1.5f);
        }
        this.d.put(Integer.valueOf(entity.getId()), new b(path.getLast(), segCount, item, anim));
    }

    private void a(DrawEvent event, b info) {
        float alpha = info.d().c();
        if (alpha <= 0.0f) {
            return;
        }
        Vector2f screen = ProjectUtil.project(info.a().x, info.a().y, info.a().z);
        if (ProjectUtil.isOnScreen(screen)) {
            float iconSize = Fonts.e.d().lineHeight() * 7.25f;
            String format = String.format(Locale.US, "%.1fs", Float.valueOf(info.b() / 20.0f));
            float width = (2.0f * 3.0f) + iconSize + Fonts.e.a(format, 7.25f);
            float height = iconSize + (2.0f * 2.0f);
            float x = screen.x() - (width / 2.0f);
            float y = screen.y() - (height / 2.0f);
            MatrixStack matrices = event.i().getMatrices();
            matrices.push();
            matrices.translate(screen.x(), screen.y(), 0.0f);
            matrices.scale(0.8f + (alpha * 0.2f), 0.8f + (alpha * 0.2f), 1.0f);
            matrices.translate(-screen.x(), -screen.y(), 0.0f);
            event.d().a(matrices, x, y, width + 1.0f, height, 2.0f, ColorUtil.a(0, 0, 0, (int) (130.0f * alpha)));
            event.e().a(event.i(), info.c(), x + 2.0f, (y + 2.0f) - 0.25f, 0, alpha, iconSize / 16.0f, false);
            Fonts.e.a(matrices, format, x + (2.0f * 2.0f) + iconSize, y + 2.0f, 7.25f, ColorUtil.a(-1, alpha), 0.0f);
            matrices.pop();
        }
    }

    private List<Vec3d> b(Entity entity) {
        Vec3d vel = entity.getVelocity();
        Vec3d pos = entity.getPos();
        boolean isThrowable = entity instanceof ThrownEntity;
        double gravity = entity instanceof PotionEntity ? 0.05000000070627959d : 0.030000000582077163d;
        List<Vec3d> path = new ArrayList<>();
        path.add(pos);
        for (int i = 0; i < 140 && vel.lengthSquared() >= 1.0000000000139336E-6d && pos.getY() >= aM_.world.getBottomY() && pos.getY() <= aM_.world.getBottomY() + aM_.world.getHeight(); i++) {
            double drag = aM_.world.getFluidState(BlockPos.ofFloored(pos)).isIn(FluidTags.WATER) ? isThrowable ? 0.7999999144424994d : 0.6000000001891753d : 0.990000120151185d;
            if (isThrowable) {
                vel = new Vec3d(vel.x * drag, (vel.y - gravity) * drag, vel.z * drag);
            }
            Vec3d next = pos.add(vel);
            BlockHitResult impact = aM_.world.raycast(new RaycastContext(pos, next, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
            if (impact.getType() != HitResult.Type.MISS) {
                path.add(impact.getPos());
                return path;
            }
            pos = next;
            if (!isThrowable) {
                vel = new Vec3d(vel.x * drag, (vel.y * drag) - 0.05000000070627959d, vel.z * drag);
            }
            path.add(pos);
        }
        return path;
    }

    private void b(DrawEvent event) {
        ItemStack mainStack = aM_.player.getStackInHand(Hand.MAIN_HAND);
        Item main = mainStack.getItem();
        Item off = aM_.player.getStackInHand(Hand.OFF_HAND).getItem();
        float speed = 0.0f;
        boolean isThrowable = false;
        boolean potion = main == Items.SPLASH_POTION || main == Items.LINGERING_POTION || off == Items.SPLASH_POTION || off == Items.LINGERING_POTION;
        if ((main instanceof BowItem) && this.b.a("Стрелы").c().booleanValue()) {
            float pull = q();
            if (pull < 0.1f) {
                return;
            } else {
                speed = pull * 3.0f;
            }
        } else if ((main instanceof CrossbowItem) && this.b.a("Стрелы").c().booleanValue()) {
            speed = 3.0f;
        } else if ((main instanceof TridentItem) && this.b.a("Трезубцы").c().booleanValue()) {
            speed = 2.5f;
        } else if ((main == Items.ENDER_PEARL || off == Items.ENDER_PEARL) && this.b.a("Эндер жемчуг").c().booleanValue()) {
            speed = 1.5f;
            isThrowable = true;
        } else if (potion && this.b.a("Зелья").c().booleanValue()) {
            speed = 0.5f;
            isThrowable = true;
        }
        if (speed == 0.0f) {
            return;
        }
        float[] viewSpread = ((main instanceof CrossbowItem) && a(mainStack)) ? new float[]{-10.0f, 0.0f, 10.0f} : new float[]{0.0f};
        for (float viewSpreadDegrees : viewSpread) {
            a result = a(speed, isThrowable, potion ? -20.0f : 0.0f, potion ? 0.05000000070627959d : 0.030000000582077163d, viewSpreadDegrees, event.g());
            if (result.a().size() >= 2) {
                if (result.c() != null) {
                    event.e().a(event.h(), result.c().getBoundingBox(), ColorUtil.a(255, 100, 100, InterfaceC0020Opcode.aN), 1.0f);
                } else if (result.b() != null && result.d() != null) {
                    a(event, result.a().getLast(), 0.33f, ColorUtil.a(255, 255, 255, InterfaceC0020Opcode.aN), result.d());
                }
            }
        }
    }

    private void a(DrawEvent event, Vec3d center, double radius, int color, Direction face) {
        Direction.Axis axis = face.getAxis();
        Vec3d u = axis == Direction.Axis.Y ? new Vec3d(1.0d, 0.0d, 0.0d) : new Vec3d(0.0d, 1.0d, 0.0d);
        Vec3d class_243Var = (axis == Direction.Axis.Z) ? new Vec3d(1.0d, 0.0d, 0.0d) : new Vec3d(0.0d, 0.0d, 1.0d);
        Vec3d v = class_243Var;
        double step = 6.283186671116134d / ((double) 8);
        double controlRadius = radius / Math.cos(step / 2.0d);
        for (int i = 0; i < 8; i++) {
            double a1 = step * ((double) i);
            double a2 = step * ((double) (i + 1));
            double am = a1 + (step / 2.0d);
            event.e().a(event.h(), a(center, u, v, radius, a1), a(center, u, v, radius, a2), a(center, u, v, controlRadius, am), color, 1.5f);
        }
    }

    private Vec3d a(Vec3d center, Vec3d u, Vec3d v, double radius, double angle) {
        return center.add(u.multiply(Math.cos(angle) * radius)).add(v.multiply(Math.sin(angle) * radius));
    }

    private boolean a(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getLevel(aM_.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.MULTISHOT)) > 0;
    }

    private float q() {
        ItemStack active = aM_.player.getActiveItem();
        if (!aM_.player.isUsingItem() || !(active.getItem() instanceof BowItem)) {
            return 0.0f;
        }
        int useTicks = active.getItem().getMaxUseTime(active, aM_.player) - aM_.player.getItemUseTimeLeft();
        float f = useTicks / 20.0f;
        return Math.min(((f * f) + (f * 2.0f)) / 3.0f, 1.0f);
    }

    private a a(float speed, boolean isThrowable, float pitchOffset, double gravity, float viewSpreadDegrees, float tickDelta) {
        double pitchRad = Math.toRadians(aM_.player.getPitch(tickDelta));
        double yawRad = Math.toRadians(aM_.player.getYaw(tickDelta));
        Vec3d look = new Vec3d((-Math.sin(yawRad)) * Math.cos(pitchRad), -Math.sin(Math.toRadians(aM_.player.getPitch(tickDelta) + pitchOffset)), Math.cos(yawRad) * Math.cos(pitchRad)).normalize();
        if (viewSpreadDegrees != 0.0f) {
            Vec3d right = new Vec3d(0.0d, 1.0d, 0.0d).crossProduct(look);
            Vec3d axis = look.crossProduct(right.lengthSquared() < 9.999996190428959E-11d ? new Vec3d(Math.cos(yawRad), 0.0d, Math.sin(yawRad)) : right.normalize()).normalize();
            double rad = Math.toRadians(viewSpreadDegrees);
            double c = Math.cos(rad);
            double s = Math.sin(rad);
            look = look.multiply(c).add(axis.crossProduct(look).multiply(s)).add(axis.multiply(axis.dotProduct(look) * (1.0d - c)));
        }
        Vec3d vel = look.multiply(speed).add(aM_.player.getVelocity().x, aM_.player.isOnGround() ? 0.0d : aM_.player.getVelocity().y, aM_.player.getVelocity().z);
        Vec3d pos = aM_.player.getCameraPosVec(tickDelta);
        List<Vec3d> path = new ArrayList<>();
        path.add(pos);
        for (int i = 0; i < 130 && vel.lengthSquared() >= 1.0000000000139336E-6d && pos.getY() >= aM_.world.getBottomY() && pos.getY() <= aM_.world.getBottomY() + aM_.world.getHeight(); i++) {
            double drag = aM_.world.getFluidState(BlockPos.ofFloored(pos)).isIn(FluidTags.WATER) ? isThrowable ? 0.7999999144424994d : 0.6000000001891753d : 0.990000120151185d;
            if (isThrowable) {
                vel = new Vec3d(vel.x * drag, (vel.y - gravity) * drag, vel.z * drag);
            }
            Vec3d next = pos.add(vel);
            BlockHitResult hitBlock = aM_.world.raycast(new RaycastContext(pos, next, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, aM_.player));
            Vec3d end = hitBlock.getType() != HitResult.Type.MISS ? hitBlock.getPos() : next;
            Entity hitEntity = a(pos, end);
            if (hitEntity != null) {
                path.add(hitEntity.getBoundingBox().expand(0.30000001176381136d).raycast(pos, end).orElse(end));
                return new a(path, null, hitEntity, null);
            }
            if (hitBlock.getType() != HitResult.Type.MISS) {
                path.add(hitBlock.getPos());
                return new a(path, hitBlock.getBlockPos(), null, hitBlock.getSide());
            }
            pos = next;
            if (!isThrowable) {
                vel = new Vec3d(vel.x * drag, (vel.y * drag) - 0.05000000070627959d, vel.z * drag);
            }
            path.add(pos);
        }
        return new a(path, null, null, null);
    }

    private Entity a(Vec3d start, Vec3d end) {
        Entity closest = null;
        double closestDist = 1.7976922776554332E308d;
        for (Entity candidate : aM_.world.getOtherEntities(aM_.player, new Box(start, end).expand(1.0d))) {
            if (candidate.isAlive() && !candidate.isSpectator() && (candidate instanceof LivingEntity)) {
                Optional<Vec3d> hit = candidate.getBoundingBox().expand(0.30000001176381136d).raycast(start, end);
                if (hit.isPresent()) {
                    double dist = start.squaredDistanceTo(hit.get());
                    if (dist < closestDist) {
                        closestDist = dist;
                        closest = candidate;
                    }
                }
            }
        }
        return closest;
    }

    record b(Vec3d a, int b, ItemStack c, AnimationUtil d) {
    }

    record a(List<Vec3d> a, BlockPos b, Entity c, Direction d) {
    }
}
