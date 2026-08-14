package aethereal.util;

import aethereal.core.Interface;
import aethereal.event.InputEvent;
import net.minecraft.util.math.MathHelper;

public class MoveUtil implements Interface {
    private static int minPriority = Integer.MAX_VALUE;
    private static float targetYaw;

    private MoveUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean a() {
        return mc.player.input.movementForward != 0.0f || mc.player.input.movementSideways != 0.0f;
    }

    public static void a(InputEvent event, float yaw, int priority) {
        if (priority >= minPriority) {
            return;
        }
        minPriority = priority;
        targetYaw = yaw;
    }

    public static void a(InputEvent event) {
        if (minPriority == Integer.MAX_VALUE) {
            return;
        }
        float forward = event.getForward();
        float strafe = event.getStrafe();
        if (forward == 0.0f && strafe == 0.0f) {
            return;
        }
        float yaw = targetYaw;
        double angle = MathHelper.wrapDegrees(Math.toDegrees(a(yaw, forward, strafe)));
        float bestF = 0.0f;
        float bestS = 0.0f;
        float bestDiff = Float.MAX_VALUE;
        for (float pf = -1.0f; pf <= 1.0f; pf += 1.0f) {
            for (float ps = -1.0f; ps <= 1.0f; ps += 1.0f) {
                if (pf != 0.0f || ps != 0.0f) {
                    double predicted = MathHelper.wrapDegrees(Math.toDegrees(a(mc.player.getYaw(), pf, ps)));
                    float diff = (float) Math.abs(angle - predicted);
                    if (diff < bestDiff) {
                        bestDiff = diff;
                        bestF = pf;
                        bestS = ps;
                    }
                }
            }
        }
        minPriority = Integer.MAX_VALUE;
        event.setForward(bestF);
        event.setStrafe(bestS);
    }

    private static double a(float rotationYaw, double moveForward, double moveStrafing) {
        float f;
        float f2;
        float f3;
        float f4 = moveForward < 0.0d ? rotationYaw + 180.0f : rotationYaw;
        if (moveStrafing > 0.0d) {
            if (moveForward < 0.0d) {
                f3 = -0.5f;
            } else {
                f3 = moveForward > 0.0d ? 0.5f : 1.0f;
            }
            f = (-90.0f) * f3;
        } else if (moveStrafing < 0.0d) {
            if (moveForward < 0.0d) {
                f2 = -0.5f;
            } else {
                f2 = moveForward > 0.0d ? 0.5f : 1.0f;
            }
            f = 90.0f * f2;
        } else {
            f = 0.0f;
        }
        return Math.toRadians(f4 + f);
    }

    public static void b(InputEvent event) {
        event.setForward(0.0f);
        event.setStrafe(0.0f);
    }

    public static boolean a(float under) {
        if (mc.player.getY() < 0.0d) {
            return false;
        }
        return mc.world.getCollisions(mc.player, mc.player.getBoundingBox().offset(0.0d, -under, 0.0d)).iterator().hasNext();
    }
}
