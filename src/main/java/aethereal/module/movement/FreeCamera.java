package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
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
    private Vec3d cameraPos;
    private Vec3d eyePos;
    private Vec3d playerPos;
    private float cameraPitch;
    private float cameraYaw;
    private boolean isFlying;
    private boolean freezePackets;

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
        this.eyePos = mc.player.getEyePos();
        this.cameraPos = mc.player.getEyePos();
        if (this.d.c().booleanValue()) {
            this.playerPos = mc.player.getPos();
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
        if (this.cameraPos != null && mc.player.isAlive()) {
            if (mc.options.getPerspective() != Perspective.FIRST_PERSON) {
                mc.options.setPerspective(Perspective.FIRST_PERSON);
            }
            Vec3d basePrev = this.eyePos != null ? this.eyePos : this.cameraPos;
            Vec3d interpolated = new Vec3d(basePrev.x + ((this.cameraPos.x - basePrev.x) * ((double) mc.getRenderTickCounter().getTickDelta(false))), basePrev.y + ((this.cameraPos.y - basePrev.y) * ((double) mc.getRenderTickCounter().getTickDelta(false))), basePrev.z + ((this.cameraPos.z - basePrev.z) * ((double) mc.getRenderTickCounter().getTickDelta(false))));
            event.setPosition(interpolated);
            event.a(true);
        }
    }

    @EventTarget
    public void a(CrosshairTargetEvent event) {
        if (this.cameraPos != null && mc.player.isAlive()) {
            event.setTarget(mc.world.raycast(new RaycastContext(this.cameraPos, this.cameraPos.add(mc.player.getRotationVec(event.b()).multiply(mc.player.getBlockInteractionRange())), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player)));
            event.a(true);
        }
    }

    @EventTarget
    public void a(TickEvent eventTick) {
        float f;
        float f2;
        if (this.cameraPos != null && mc.player.isAlive()) {
            if (mc.currentScreen == null) {
                if (mc.options.forwardKey.isPressed()) {
                    f = 1.0f;
                } else {
                    f = mc.options.backKey.isPressed() ? -1.0f : 0.0f;
                }
                this.cameraPitch = f;
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 65)) {
                    f2 = 1.0f;
                } else {
                    f2 = InputUtil.isKeyPressed(mc.getWindow().getHandle(), 68) ? -1.0f : 0.0f;
                }
                this.cameraYaw = f2;
                this.isFlying = mc.options.jumpKey.isPressed();
                this.freezePackets = mc.options.sneakKey.isPressed();
            } else {
                d(false);
            }
            if (this.d.c().booleanValue() && !mc.player.isOnGround()) {
                mc.player.setVelocity(0.0d, 0.0d, 0.0d);
                if (this.playerPos != null) {
                    mc.player.setPosition(this.playerPos.x, this.playerPos.y, this.playerPos.z);
                }
            }
            this.eyePos = this.cameraPos;
            this.cameraPos = this.cameraPos.add(((((double) this.cameraPitch) * (-Math.sin(Math.toRadians(Look.b())))) + (((double) this.cameraYaw) * Math.cos(Math.toRadians(Look.b())))) * ((double) this.b.c().floatValue()), (this.isFlying ? this.c.c().floatValue() : 0.0d) - (this.freezePackets ? this.c.c().floatValue() : 0.0d), ((((double) this.cameraPitch) * Math.cos(Math.toRadians(Look.b()))) + (((double) this.cameraYaw) * Math.sin(Math.toRadians(Look.b())))) * ((double) this.b.c().floatValue()));
        }
    }

    @EventTarget
    public void a(InputEvent event) {
        float f;
        float f2;
        if (this.cameraPos != null && mc.player.isAlive()) {
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
        if (event.isSend() && this.d.c().booleanValue() && this.cameraPos != null && !mc.player.isOnGround() && mc.player.isAlive()) {
            if ((event.getPacket() instanceof PlayerInputC2SPacket) || (event.getPacket() instanceof ClientCommandC2SPacket)) {
                event.a(true);
            }
        }
    }

    private void d(boolean clearPositions) {
        if (clearPositions) {
            this.cameraPos = null;
            this.eyePos = null;
            this.playerPos = null;
        } else {
            this.cameraYaw = 0.0f;
            this.cameraPitch = 0.0f;
            this.freezePackets = false;
            this.isFlying = false;
        }
    }
}
