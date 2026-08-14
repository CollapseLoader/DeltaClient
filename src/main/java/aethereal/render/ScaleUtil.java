package aethereal.render;

import aethereal.core.Interface;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;

public class ScaleUtil implements Interface {
    private ScaleUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void a(DrawContext context, int scale) {
        Window window = mc.getWindow();
        double previous = window.getScaleFactor();
        double target = window.calculateScaleFactor(scale, mc.forcesUnicodeFont());
        window.setScaleFactor(target);
        context.getMatrices().push();
        context.getMatrices().scale((float) (target / previous), (float) (target / previous), 1.0f);
    }

    public static void a(DrawContext context) {
        context.getMatrices().pop();
        mc.getWindow().setScaleFactor(mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue().intValue(), mc.forcesUnicodeFont()));
    }

    public static void b(DrawContext context) {
        a(context, mc.options.getGuiScale().getValue().intValue());
    }

    public static void c(DrawContext context) {
        context.getMatrices().pop();
        mc.getWindow().setScaleFactor(mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont()));
    }
}
