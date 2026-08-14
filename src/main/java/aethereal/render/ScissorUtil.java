package aethereal.render;

import aethereal.core.Interface;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScissorUtil implements Interface {
    private static final Deque<intersect> b = new ArrayDeque<>();

    private ScissorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void a(MatrixStack matrixStack, float x, float y, float width, float height) {
        float scaleFactor = (float) mc.getWindow().getScaleFactor();
        intersect scissorBox = new intersect((int) (x * scaleFactor), (int) (((mc.getWindow().getScaledHeight() - y) - height) * scaleFactor), (int) (width * scaleFactor), (int) (height * scaleFactor));
        if (!b.isEmpty()) {
            scissorBox = scissorBox.a(b.peek());
        }
        b.push(scissorBox);
        matrixStack.push();
        a(scissorBox);
    }

    public static void a(MatrixStack matrixStack) {
        b.pop();
        if (b.isEmpty()) {
            RenderSystem.disableScissor();
        } else {
            a(b.peek());
        }
        matrixStack.pop();
    }

    private static void a(intersect box) {
        RenderSystem.enableScissor(box.a, box.b, box.c, box.d);
    }

    record intersect(int a, int b, int c, int d) {

        intersect a(intersect p) {
            int nx = Math.max(this.a, p.a);
            int ny = Math.max(this.b, p.b);
            return new intersect(nx, ny, Math.max(0, Math.min(this.a + this.c, p.a + p.c) - nx), Math.max(0, Math.min(this.b + this.d, p.b + p.d) - ny));
        }
    }
}
