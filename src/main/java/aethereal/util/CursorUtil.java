package aethereal.util;

import aethereal.core.Interface;
import lombok.Generated;
import org.lwjgl.glfw.GLFW;

public class CursorUtil implements Interface {
    @Generated
    private CursorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void a(a type) {
        if (aM_.getWindow() != null) {
            long cursor = GLFW.glfwCreateStandardCursor(type.a());
            if (cursor != 0) {
                GLFW.glfwSetCursor(aM_.getWindow().getHandle(), cursor);
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

        private final int i;

        @Generated
        a(final int glfwType) {
            this.i = glfwType;
        }

        @Generated
        public int a() {
            return this.i;
        }
    }
}
