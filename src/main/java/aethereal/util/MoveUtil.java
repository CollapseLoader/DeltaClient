package aethereal.util;

import aethereal.core.Interface;
import aethereal.event.InputEvent;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;

public class MoveUtil implements Interface {
    private static int b = Integer.MAX_VALUE;
    private static float c;

    @Generated
    private MoveUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean a() {
        return aM_.player.input.movementForward != 0.0f || aM_.player.input.movementSideways != 0.0f;
    }

    public static void a(InputEvent event, float yaw, int priority) {
        if (priority >= b) {
            return;
        }
        b = priority;
        c = yaw;
    }

    public static void a(InputEvent event) {
        if (b == Integer.MAX_VALUE) {
            return;
        }
        float forward = event.b();
        float strafe = event.c();
        if (forward == 0.0f && strafe == 0.0f) {
            return;
        }
        float yaw = c;
        double angle = MathHelper.wrapDegrees(Math.toDegrees(a(yaw, forward, strafe)));
        float bestF = 0.0f;
        float bestS = 0.0f;
        float bestDiff = Float.MAX_VALUE;
        for (float pf = -1.0f; pf <= 1.0f; pf += 1.0f) {
            for (float ps = -1.0f; ps <= 1.0f; ps += 1.0f) {
                if (pf != 0.0f || ps != 0.0f) {
                    double predicted = MathHelper.wrapDegrees(Math.toDegrees(a(aM_.player.getYaw(), pf, ps)));
                    float diff = (float) Math.abs(angle - predicted);
                    if (diff < bestDiff) {
                        bestDiff = diff;
                        bestF = pf;
                        bestS = ps;
                    }
                }
            }
        }
        b = Integer.MAX_VALUE;
        event.a(bestF);
        event.b(bestS);
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
        event.a(0.0f);
        event.b(0.0f);
    }

    public static boolean a(float under) {
        if (aM_.player.getY() < 0.0d) {
            return false;
        }
        return aM_.world.getCollisions(aM_.player, aM_.player.getBoundingBox().offset(0.0d, -under, 0.0d)).iterator().hasNext();
    }
}
