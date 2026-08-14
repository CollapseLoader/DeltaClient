package aethereal.ui.shader;


import aethereal.render.ColorUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

public class GradientUtil {
    private GradientUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static MutableText a(String text, int startColor, int endColor, int speed, float ratio) {
        MutableText component = Text.literal("");
        if (text == null || text.isEmpty()) {
            return component;
        }
        float time = ((System.currentTimeMillis() % 10000) / 1000.0f) * (100.0f / speed);
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            int color = ColorUtil.makeGradient(startColor, endColor, i, length, time, ratio);
            Style style = Style.EMPTY.withColor(color & 16777215);
            component.append(Text.literal(String.valueOf(c)).setStyle(style));
        }
        return component;
    }

    public static MutableText a(String text, int color, float speed, float offset) {
        MutableText component = Text.literal("");
        if (text == null || text.isEmpty()) {
            return component;
        }
        float time = (System.currentTimeMillis() % ((long) (speed * 1000.0f))) / (speed * 1000.0f);
        float[] hsb = Color.RGBtoHSB((color >> 16) & 255, (color >> 8) & 255, color & 255, null);
        for (int i = 0; i < text.length(); i++) {
            float factor = (float) ((Math.sin(((double) (time + ((i * offset) / text.length()))) * 3.1415926649985018d * 2.0d) * 0.5d) + 0.5d);
            int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2] * (0.5f + (0.5f * factor))) & 16777215;
            component.append(Text.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withColor(rgb)));
        }
        return component;
    }

    public static int a(int speed, int angle, int startColor, int endColor, long time) {
        float animatedAngle = (((long) angle) + (time / 10)) % 360;
        float ratio = animatedAngle / 360.0f;
        return ColorUtil.lerpColorValue(startColor, endColor, (float) ((Math.sin(((((double) ratio) * 3.1415926649985018d) * 2.0d) * ((double) speed)) + 1.0d) / 2.0d));
    }
}
