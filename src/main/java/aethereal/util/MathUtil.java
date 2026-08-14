package aethereal.util;

import aethereal.core.Interface;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MathUtil implements Interface {
    private MathUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static float a(float start, float end, float delta) {
        return start + ((end - start) * delta);
    }

    public static Vec3d a(Entity entity, float partialTicks) {
        return new Vec3d(entity.prevX + ((entity.getX() - entity.prevX) * ((double) partialTicks)), entity.prevY + ((entity.getY() - entity.prevY) * ((double) partialTicks)), entity.prevZ + ((entity.getZ() - entity.prevZ) * ((double) partialTicks)));
    }

    public static float b(float num, float min, float max) {
        return Math.min(Math.max(num, min), max);
    }

    public static double scale(double coordinate, int factor) {
        return (coordinate * mc.getWindow().getScaleFactor()) / ((double) mc.getWindow().calculateScaleFactor(factor, mc.forcesUnicodeFont()));
    }

    public static float a(float value) {
        float clamped = b(value, 0.0f, 1.0f);
        return clamped * clamped * (3.0f - (2.0f * clamped));
    }

    public static float a(float min, float max) {
        return (float) ((Math.random() * ((double) (max - min))) + ((double) min));
    }

    public static float[] b(float smoothness) {
        float horizontal = ((-smoothness) / 2.0f) + (smoothness * 2.0f);
        float vertical = (smoothness / 2.0f) + smoothness;
        return new float[]{horizontal, vertical};
    }

    public static String a(int amplifier) {
        String[] strings = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        return (amplifier < 0 || amplifier >= strings.length) ? String.valueOf(amplifier + 1) : strings[amplifier];
    }

    public static int a(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        String norm = text.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim();
        String[] strings = {"i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix", "x"};
        for (int i = strings.length - 1; i >= 0; i--) {
            if (norm.contains(strings[i])) {
                return i + 1;
            }
        }
        String digits = norm.replaceAll("[^0-9]", "");
        if (!digits.isEmpty() && Integer.parseInt(digits) >= 1 && Integer.parseInt(digits) <= 10) {
            return Integer.parseInt(digits);
        }
        return 0;
    }

    public static boolean a(double mouseX, double mouseY, float x, float y, float width, float height) {
        return mouseX >= ((double) x) && mouseX <= ((double) (x + width)) && mouseY >= ((double) y) && mouseY <= ((double) (y + height));
    }

    public static float c(float current, float target, float speed) {
        float delta = mc.getRenderTickCounter().getLastFrameDuration();
        return current + ((target - current) * (1.0f - ((float) Math.exp((-speed) * delta))));
    }
}
