package aethereal.ui.widget;


import aethereal.core.Delta;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.util.MathUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

public class EffectMarker {
    private EffectMarker() {
    }

    public static void a(List<a> list, float x, float y) {
        if (list != null) {
            list.add(new a(x, y));
        }
    }

    public static void a(MatrixStack matrices, float partialTicks, List<a> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).update(matrices, partialTicks)) {
                list.remove(i);
            }
        }
    }

    public static final class a {
        private final AnimationUtil a = new AnimationUtil();
        private final long b = System.nanoTime() + 300000000;
        private final float c;
        private final float d;
        private boolean e;

        a(float x, float y) {
            this.c = x;
            this.d = y;
        }

        boolean update(MatrixStack matrices, float partialTicks) {
            this.a.a(0.0f, 1.0f, 0.2f, EasingList.h, partialTicks);
            if (!this.e && System.nanoTime() >= this.b) {
                this.e = true;
            }
            this.a.a(!this.e);
            float progress = MathUtil.b(this.a.c(), 0.0f, 1.0f);
            float scale = this.e ? progress : easeScale(progress);
            float length = 4.0f * Math.max(1.0E-4f, scale);
            int color = ColorUtil.convertToARGB(255, 255, 255, Math.round(250.0f * progress));
            matrices.push();
            matrices.translate(this.c, this.d, 0.0f);
            for (int i = 0; i < 4; i++) {
                drawMarker(matrices, length, 45.0f + (90.0f * i), length, color);
            }
            matrices.pop();
            return this.e && progress <= 0.01f;
        }

        private void drawMarker(MatrixStack matrices, float length, float angleDeg, float offset, int color) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angleDeg));
            matrices.translate(offset, 0.0f, 0.0f);
            Delta.getInstance().getModuleProcessor().i().a(matrices, (-length) / 2.0f, -0.25f, length, 0.5f, 0.0f, color);
            matrices.pop();
        }

        private float easeScale(float scale) {
            if (scale <= 0.0f) {
                return 0.0f;
            }
            if (scale < 0.6f) {
                return scale / 0.6f;
            }
            return scale < 0.8f ? 1.0f + (((scale - 0.6f) / 0.5f) * 0.5f) : 1.2f - (((scale - 0.8f) / 0.2f) * 0.2f);
        }
    }
}
