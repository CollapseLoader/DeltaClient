package aethereal.handler;

import aethereal.config.BaseProcessor;
import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.event.InputEvent;
import aethereal.module.combat.AuraUtil;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class RotationProcessor extends BaseProcessor {
    private static rotationState state;
    private static float resetSpeed;
    private static int lookMode;
    private static int maxTicks;
    private static int currentTick;
    private static int minResetTicks;
    private static int priority;

    static {
        state = rotationState.IDLE;
    }

    private final Look currentLook = new Look();

    public static rotationState getState() {
        return state;
    }

    public static int getMinResetTicks() {
        return minResetTicks;
    }

    public void setMinResetTicks(int ticks) {
        minResetTicks = Math.max(ticks, 0);
    }

    public void reset() {
        this.currentLook.a(false);
        state = rotationState.IDLE;
        currentTick = 0;
    }

    private static boolean isUsingUseableItem() {
        List<UseableHandler.UseableTask> tasks = Delta.getInstance().getModuleProcessor().v().getUseableHandler().a();
        if (tasks.isEmpty() || tasks.getFirst().d() >= 1) {
            return false;
        }
        Item item = tasks.getFirst().a().getItem();
        return item == Items.WIND_CHARGE || item == Items.ENDER_PEARL || item == Items.SNOWBALL
                || item == Items.SPLASH_POTION || item == Items.DRIED_KELP;
    }

    private static Rotation getDefaultWobbleRotation() {
        return addSinusoidalWobble(new Rotation(Look.b(), Look.c()));
    }

    public static Rotation addSinusoidalWobble(Rotation rotation) {
        float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float sw = ((float) ((((Math.sin(t * 0.8f) * 11.0d)
                + (Math.sin((((double) t) * 0.04000001502137623d) + 17.200010267039897d) * 1.5d))
                + (Math.sin((((double) t) * 0.10999997113093289d) + 5.8000000238651515d) * 3.0d))
                + (Math.sin((((double) t) * 0.07000004685868849d) + 12.300000009313816d)))) / 6.0f;
        float sh = ((float) (Math.sin(((double) t) * 0.09999998815548458d)
                + (Math.sin((((double) t) * 0.029999993539464892d) + 54.10000012300467d) * 0.5d))) / 4.0f;
        return new Rotation(rotation.c() + MathHelper.clamp(sw, -0.15f, 0.15f),
                rotation.d() + MathHelper.clamp(sh, -0.15f, 0.15f));
    }

    private static int getMaxTicksForMode(int mode) {
        switch (mode) {
            case 0:
                return 1;
            case 1:
                return 9;
            case 7:
                return 30;
            default:
                return 10;
        }
    }

    private static Rotation calculateModeRotation(int mode, int idleTicks) {
        float baseYaw = Look.b();
        float basePitch = Look.c();
        float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float sw = ((float) ((((Math.sin(t * 0.31f) * 0.5d) + (Math.sin((t * 0.73f) + 1.1f) * 0.3000000002422922d))
                + (Math.sin((t * 1.7f) + 2.6f) * 0.19999998556632664d)) * 12.0d)) / 4.0f;
        switch (mode) {
            case 1:
                float baseYaw2 = AuraUtil.a(mc.player.getYaw(), Look.b(), MathUtil.a(0.1f, 0.45f));
                float basePitch2 = AuraUtil.a(mc.player.getPitch(), Look.c(), MathUtil.a(0.1f, 0.45f));
                return new Rotation(baseYaw2 + sw, MathHelper.clamp(basePitch2 + sw, -90.0f, 90.0f));
            case 7:
                if (!Delta.getInstance().getModuleProcessor().t().aS().m()) {
                    idleTicks = 25;
                }
                if (idleTicks <= 20) {
                    return new Rotation(mc.player.getYaw() + sw,
                            MathHelper.clamp(mc.player.getPitch() + sw, -90.0f, 90.0f));
                }
                float baseYaw3 = AuraUtil.a(mc.player.getYaw(), Look.b(), MathUtil.a(0.2f, 0.35f));
                float basePitch3 = AuraUtil.a(mc.player.getPitch(), Look.c(), MathUtil.a(0.2f, 0.35f));
                return new Rotation(baseYaw3 + sw, MathHelper.clamp(basePitch3 + sw, -90.0f, 90.0f));
            default:
                return new Rotation(baseYaw + sw, basePitch + sw);
        }
    }

    public static float snapToGCD(float lastYaw, float current) {
        double sens = (mc.options.getMouseSensitivity().getValue().doubleValue() * 0.6000000498956214d)
                + 0.19999998556632664d;
        double gcd = sens * sens * sens * 8.0d;
        return (float) (((double) lastYaw) + (Math.ceil((((double) (current - lastYaw)) / gcd) / 0.15000006556510925d)
                * gcd * 0.15000006556510925d));
    }

    @Override

    public void setup() {
    }

    public Look getCurrentLook() {
        return this.currentLook;
    }

    @Override
    public void unSetup() {
    }

    @EventTarget
    private void onInputEvent(InputEvent e2) {
        if (isRotating()) {
            MoveUtil.a(e2, Look.b(), 10);
        }
    }

    @EventTarget
    private void onGlobalEvent(GlobalEvent e2) {
        currentTick++;
        if (isRotating()) {
            if (isUsingUseableItem()) {
                applyRotationStep(getDefaultWobbleRotation(), resetSpeed, false);
            } else {
                applyRotationStep(calculateModeRotation(lookMode, currentTick), resetSpeed, false);
            }
        }
        if (state == rotationState.AIM && currentTick > maxTicks) {
            state = rotationState.RESET;
        }
        if (state == rotationState.RESET && applyRotationStep(Rotation.a(), resetSpeed, true)) {
            this.currentLook.a(false);
            state = rotationState.IDLE;
        }
    }

    private boolean isRotating() {
        return currentTick >= 2 && currentTick <= maxTicks && state != rotationState.IDLE;
    }

    public void startAiming(Rotation rotation, float turnSpeed, int lookMode, int priority) {
        startAimingWithSpeeds(rotation, turnSpeed, turnSpeed, lookMode, priority);
    }

    public void startAimingWithSpeeds(Rotation rotation, float aimSpeed, float resetSpeed, int lookMode, int priority) {
        if (priority < RotationProcessor.priority) {
            return;
        }
        if (state != rotationState.IDLE && isUsingUseableItem()) {
            rotation = getDefaultWobbleRotation();
        }
        if (state == rotationState.IDLE) {
            this.currentLook.a(true);
        }
        RotationProcessor.resetSpeed = resetSpeed;
        RotationProcessor.lookMode = lookMode;
        maxTicks = getMaxTicksForMode(lookMode);
        RotationProcessor.priority = priority;
        state = rotationState.AIM;
        currentTick = 0;
        applyRotationStep(rotation, aimSpeed, true);
    }

    private boolean applyRotationStep(Rotation rotation, float turnSpeed, boolean bait) {
        Rotation currentRotation = new Rotation(mc.player);
        float yawDelta = MathHelper.wrapDegrees(rotation.c() - currentRotation.c());
        float pitchDelta = rotation.d() - currentRotation.d();
        float totalDelta = Math.abs(yawDelta) + Math.abs(pitchDelta);
        float yawSpeed = totalDelta == 0.0f ? 0.0f : Math.abs(yawDelta / totalDelta) * turnSpeed;
        float pitchSpeed = totalDelta == 0.0f ? 0.0f : Math.abs(pitchDelta / totalDelta) * turnSpeed;
        float newYaw = mc.player.getYaw() + MathHelper.clamp(yawDelta, -yawSpeed, yawSpeed);
        float newPitch = mc.player.getPitch() + MathHelper.clamp(pitchDelta, -pitchSpeed, pitchSpeed);
        float newYaw2 = snapToGCD(mc.player.getYaw(), newYaw);
        float newPitch2 = MathHelper.clamp(snapToGCD(mc.player.getPitch(), newPitch), -90.0f, 90.0f);
        if (minResetTicks > 0) {
            newPitch2 = mc.player.getPitch();
            newYaw2 = mc.player.getYaw();
            minResetTicks--;
        }
        mc.player.setYaw(newYaw2);
        mc.player.setPitch(newPitch2);
        Rotation finalRotation = new Rotation(mc.player);
        if (bait) {
            currentTick = 0;
        }
        return finalRotation.a(rotation) < ((double) turnSpeed);
    }

    public enum rotationState {
        AIM,
        RESET,
        IDLE
    }
}
