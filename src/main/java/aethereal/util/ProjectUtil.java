package aethereal.util;

import aethereal.core.Delta;
import aethereal.core.Interface;
import net.minecraft.util.math.Box;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import platform.inject.invokers.GameRendererInvoker;

public class ProjectUtil implements Interface {
    private ProjectUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Vector2f project(double x, double y, double z) {
        net.minecraft.client.render.Camera camera = mc.getEntityRenderDispatcher().camera;
        Vector3f result3f = new Vector3f((float) (x - camera.getPos().x), (float) (y - camera.getPos().y), (float) (z - camera.getPos().z));
        Quaternionf invCamRot = new Quaternionf(camera.getRotation()).conjugate();
        result3f.rotate(invCamRot);
        return project(result3f, ((GameRendererInvoker) mc.gameRenderer).invokeGetFov(camera, mc.getRenderTickCounter().getTickDelta(false), true));
    }

    private static Vector2f project(Vector3f result3f, double fov) {
        if (result3f.z >= 0.0f) {
            return new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
        }
        float realAspect = (float) mc.getWindow().getFramebufferWidth() / (float) mc.getWindow().getFramebufferHeight();
        float modifiedAspect = Delta.getInstance().getModuleProcessor().t().aB().m() ? Delta.getInstance().getModuleProcessor().t().aB().q() : realAspect;
        double halfHeightAtDepth = (-result3f.z) * Math.tan(Math.toRadians(fov / 2.0d));
        double halfWidthAtDepth = halfHeightAtDepth * ((double) modifiedAspect);
        double ndcX = result3f.x / halfWidthAtDepth;
        double ndcY = result3f.y / halfHeightAtDepth;
        float screenX = (float) (mc.getWindow().getScaledWidth() / 2.0f + ndcX * (mc.getWindow().getScaledWidth() / 2.0f));
        float screenY = (float) (mc.getWindow().getScaledHeight() / 2.0f - ndcY * (mc.getWindow().getScaledHeight() / 2.0f));
        return new Vector2f(screenX, screenY);
    }

    public static float[] getBounds(Box box) {
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -3.4028235E38f;
        float maxY = -3.4028235E38f;
        for (int corner = 0; corner < 8; corner++) {
            double x = (corner & 1) == 0 ? box.minX : box.maxX;
            double y = (corner & 2) == 0 ? box.minY : box.maxY;
            double z = (corner & 4) == 0 ? box.minZ : box.maxZ;
            Vector2f screen = project(x, y, z);
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

    public static boolean isOnScreen(Vector2f screen) {
        return screen.x() != Float.MAX_VALUE && screen.y() != Float.MAX_VALUE && screen.x() >= 0.0f && screen.y() >= 0.0f && screen.x() <= ((float) mc.getWindow().getScaledWidth()) && screen.y() <= ((float) mc.getWindow().getScaledHeight());
    }
}
