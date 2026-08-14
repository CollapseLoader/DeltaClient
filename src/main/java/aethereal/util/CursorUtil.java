package aethereal.util;

import aethereal.core.Interface;
import org.lwjgl.glfw.GLFW;

public class CursorUtil implements Interface {
    private CursorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void a(a type) {
        if (mc.getWindow() != null) {
            long cursor = GLFW.glfwCreateStandardCursor(type.getGlfwType());
            if (cursor != 0) {
                GLFW.glfwSetCursor(mc.getWindow().getHandle(), cursor);
            }
        }
    }

    public enum a {
        DEFAULT(221185),
        HAND(221188),
        ARROW_HORIZONTAL(221189),
        ARROW_VERTICAL(221190),
        TEXT(221186),
        CROSSHAIR(221187),
        BLOCK(221194),
        RESIZE_ALL(221193);

        private final int glfwType;

        a(final int glfwType) {
            this.glfwType = glfwType;
        }

        public int getGlfwType() {
            return this.glfwType;
        }
    }
}
