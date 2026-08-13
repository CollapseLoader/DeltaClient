package aethereal.util;

import aethereal.core.Delta;
import aethereal.core.Interface;
import lombok.Generated;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import platform.inject.invokers.GameRendererInvoker;

public class ProjectUtil implements Interface {
    @Generated
    private ProjectUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Vector2f a(double x, double y, double z) {
        Quaternionf yawQuat = RotationAxis.POSITIVE_Y.rotationDegrees(-aM_.getEntityRenderDispatcher().camera.getYaw());
        Quaternionf pitchQuat = RotationAxis.POSITIVE_X.rotationDegrees(aM_.getEntityRenderDispatcher().camera.getPitch());
        Quaternionf cameraRotation = yawQuat.mul(pitchQuat, new Quaternionf());
        Quaternionf cameraRotation2 = cameraRotation.conjugate(new Quaternionf());
        Vector3f result3f = new Vector3f((float) (aM_.getEntityRenderDispatcher().camera.getPos().x - x), (float) (aM_.getEntityRenderDispatcher().camera.getPos().y - y), (float) (aM_.getEntityRenderDispatcher().camera.getPos().getZ() - z));
        result3f.rotate(cameraRotation2);
        return a(result3f, ((GameRendererInvoker) aM_.gameRenderer).invokeGetFov(aM_.getEntityRenderDispatcher().camera, aM_.getRenderTickCounter().getTickDelta(false), true));
    }

    private static Vector2f a(Vector3f result3f, double fov) {
        float realAspect = aM_.getWindow().getFramebufferWidth() / aM_.getWindow().getFramebufferHeight();
        float modifiedAspect = Delta.h().d().t().aB().m() ? Delta.h().d().t().aB().q() : realAspect;
        double scaleFactorY = ((double) (aM_.getWindow().getScaledHeight() / 2.0f)) / (((double) result3f.z) * Math.tan(Math.toRadians(fov / 2.0d)));
        double scaleFactorX = (scaleFactorY * ((double) realAspect)) / ((double) modifiedAspect);
        return result3f.z < 0.0f ? new Vector2f((float) ((((double) (-result3f.x())) * scaleFactorX) + ((double) (aM_.getWindow().getScaledWidth() / 2.0f))), (float) (((double) (aM_.getWindow().getScaledHeight() / 2.0f)) - (((double) result3f.y()) * scaleFactorY))) : new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
    }

    public static float[] a(Box box) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -3.4028235E38f;
        float maxY = -3.4028235E38f;
        for (int corner = 0; corner < 8; corner++) {
            double x = (corner & 1) == 0 ? box.minX : box.maxX;
            double y = (corner & 2) == 0 ? box.minY : box.maxY;
            double z = (corner & 4) == 0 ? box.minZ : box.maxZ;
            Vector2f screen = a(x, y, z);
            if (screen.x() != Float.MAX_VALUE) {
                minX = Math.min(minX, screen.x());
                minY = Math.min(minY, screen.y());
                maxX = Math.max(maxX, screen.x());
                maxY = Math.max(maxY, screen.y());
            }
        }
        if (maxX <= minX || maxY <= minY) {
            return null;
        }
        return new float[]{minX, minY, maxX, maxY};
    }

    public static boolean a(Vector2f screen) {
        return screen.x() != Float.MAX_VALUE && screen.y() != Float.MAX_VALUE && screen.x() >= 0.0f && screen.y() >= 0.0f && screen.x() <= ((float) aM_.getWindow().getScaledWidth()) && screen.y() <= ((float) aM_.getWindow().getScaledHeight());
    }
}
