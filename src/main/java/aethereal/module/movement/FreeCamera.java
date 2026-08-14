package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.*;
import aethereal.setting.BooleanSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.Look;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

@ModuleRegister(name = "Free Camera", description = "Позволяет свободно перемещать камеру, пока игрок остаётся на месте", category = Category.Movement)
public class FreeCamera extends Module {
    private final SliderSetting b = new SliderSetting("Скорость движения XZ", 1.0f, 0.1f, 5.0f, 0.1f);
    private final SliderSetting c = new SliderSetting("Скорость движения Y", 1.0f, 0.1f, 5.0f, 0.1f);
    private final BooleanSetting d = new BooleanSetting("Замораживать пакеты в полете", true);
    private Vec3d e;
    private Vec3d f;
    private Vec3d g;
    private float h;
    private float i;
    private boolean j;
    private boolean k;

    public FreeCamera() {
        a(this.d, this.b, this.c);
    }

    @Override
    public void b() {
        super.b();
        if (mc.player == null) {
            d(true);
            a();
            return;
        }
        this.f = mc.player.getEyePos();
        this.e = mc.player.getEyePos();
        if (this.d.c().booleanValue()) {
            this.g = mc.player.getPos();
        }
        d(false);
    }

    @Override
    public void c() {
        super.c();
        d(true);
    }

    @EventTarget
    public void a(CameraPositionEvent event) {
        if (this.e != null && mc.player.isAlive()) {
            if (mc.options.getPerspective() != Perspective.FIRST_PERSON) {
                mc.options.setPerspective(Perspective.FIRST_PERSON);
            }
            Vec3d basePrev = this.f != null ? this.f : this.e;
            Vec3d interpolated = new Vec3d(basePrev.x + ((this.e.x - basePrev.x) * ((double) mc.getRenderTickCounter().getTickDelta(false))), basePrev.y + ((this.e.y - basePrev.y) * ((double) mc.getRenderTickCounter().getTickDelta(false))), basePrev.z + ((this.e.z - basePrev.z) * ((double) mc.getRenderTickCounter().getTickDelta(false))));
            event.a(interpolated);
            event.a(true);
        }
    }

    @EventTarget
    public void a(CrosshairTargetEvent event) {
        if (this.e != null && mc.player.isAlive()) {
            event.a(mc.world.raycast(new RaycastContext(this.e, this.e.add(mc.player.getRotationVec(event.b()).multiply(mc.player.getBlockInteractionRange())), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player)));
            event.a(true);
        }
    }

    @EventTarget
    public void a(TickEvent eventTick) {
        float f;
        float f2;
        if (this.e != null && mc.player.isAlive()) {
            if (mc.currentScreen == null) {
                if (mc.options.forwardKey.isPressed()) {
                    f = 1.0f;
                } else {
                    f = mc.options.backKey.isPressed() ? -1.0f : 0.0f;
                }
                this.h = f;
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 65)) {
                    f2 = 1.0f;
                } else {
                    f2 = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 68) ? -1.0f : 0.0f;
                }
                this.i = f2;
                this.j = mc.options.jumpKey.isPressed();
                this.k = mc.options.sneakKey.isPressed();
            } else {
                d(false);
            }
            if (this.d.c().booleanValue() && !mc.player.isOnGround()) {
                mc.player.setVelocity(0.0d, 0.0d, 0.0d);
                if (this.g != null) {
                    mc.player.setPosition(this.g.x, this.g.y, this.g.z);
                }
            }
            this.f = this.e;
            this.e = this.e.add(((((double) this.h) * (-Math.sin(Math.toRadians(Look.b())))) + (((double) this.i) * Math.cos(Math.toRadians(Look.b())))) * ((double) this.b.c().floatValue()), (this.j ? this.c.c().floatValue() : 0.0d) - (this.k ? this.c.c().floatValue() : 0.0d), ((((double) this.h) * Math.cos(Math.toRadians(Look.b()))) + (((double) this.i) * Math.sin(Math.toRadians(Look.b())))) * ((double) this.b.c().floatValue()));
        }
    }

    @EventTarget
    public void a(InputEvent event) {
        float f;
        float f2;
        if (this.e != null && mc.player.isAlive()) {
            if (mc.currentScreen != null) {
                event.setForward(0.0f);
                event.setStrafe(0.0f);
                event.setJump(false);
                event.setSneak(false);
                return;
            }
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 265)) {
                f = 1.0f;
            } else {
                f = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 264) ? -1.0f : 0.0f;
            }
            event.setForward(f);
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 262)) {
                f2 = -1.0f;
            } else {
                f2 = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 263) ? 1.0f : 0.0f;
            }
            event.setStrafe(f2);
            event.setJump(false);
            event.setSneak(false);
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.b() && this.d.c().booleanValue() && this.e != null && !mc.player.isOnGround() && mc.player.isAlive()) {
            if ((event.d() instanceof PlayerInputC2SPacket) || (event.d() instanceof ClientCommandC2SPacket)) {
                event.a(true);
            }
        }
    }

    private void d(boolean clearPositions) {
        if (clearPositions) {
            this.e = null;
            this.f = null;
            this.g = null;
        } else {
            this.i = 0.0f;
            this.h = 0.0f;
            this.k = false;
            this.j = false;
        }
    }
}
