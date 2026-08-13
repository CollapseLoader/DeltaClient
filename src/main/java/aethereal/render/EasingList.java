package aethereal.render;


public class EasingList {
    public static final double a = 1.7015791506357025d;
    public static final double b = 2.5949079670432127d;
    public static final double c = 2.701578217685146d;
    public static final double d = 2.094396299644887d;
    public static final double e = 1.3962634699317127d;
    public static final a f = value -> {
        return (float) (1.0d - Math.cos((((double) value) * 3.1415928959671753d) / 2.0d));
    };
    public static final a g = value -> {
        return (float) Math.sin((((double) value) * 3.1415928959671753d) / 2.0d);
    };
    public static final a h = value -> {
        return (float) ((-(Math.cos(3.1415928959671753d * ((double) value)) - 1.0d)) / 2.0d);
    };
    public static final a i = value -> {
        return (float) (1.0d - Math.sqrt(1.0d - Math.pow(value, 2.0d)));
    };
    public static final a j = value -> {
        return (float) Math.sqrt(1.0d - Math.pow(((double) value) - 1.0d, 2.0d));
    };
    public static final a k = value -> {
        return (float) (((double) value) < 0.5d ? (1.0d - Math.sqrt(1.0d - Math.pow(2.0d * ((double) value), 2.0d))) / 2.0d : (Math.sqrt(1.0d - Math.pow(((-2.0d) * ((double) value)) + 2.0d, 2.0d)) + 1.0d) / 2.0d);
    };
    public static final a l = value -> {
        return (((double) value) == 0.0d || ((double) value) == 1.0d) ? value : (float) (Math.pow(-2.0d, (10.0d * ((double) value)) - 10.0d) * Math.sin(((((double) value) * 10.0d) - 10.75d) * 2.094396299644887d));
    };
    public static final a m = value -> {
        return (((double) value) == 0.0d || ((double) value) == 1.0d) ? value : (float) ((Math.pow(2.0d, (-10.0d) * ((double) value)) * Math.sin(((((double) value) * 10.0d) - 0.75d) * 2.094396299644887d)) + 1.0d);
    };
    public static final a n = value -> {
        if (value == 0.0d || value == 1.0d) {
            return value;
        }
        return (float) (((double) value) < 0.5d ? (-(Math.pow(2.0d, (20.0d * ((double) value)) - 10.0d) * Math.sin(((20.0d * ((double) value)) - 11.125d) * 1.3962634699317127d))) / 2.0d : ((Math.pow(2.0d, ((-20.0d) * ((double) value)) + 10.0d) * Math.sin(((20.0d * ((double) value)) - 11.125d) * 1.3962634699317127d)) / 2.0d) + 1.0d);
    };
    public static final a o = value -> {
        return ((double) value) != 0.0d ? (float) Math.pow(2.0d, (10.0d * ((double) value)) - 10.0d) : value;
    };
    public static final a p = value -> {
        return ((double) value) != 1.0d ? (float) (1.0d - Math.pow(2.0d, (-10.0d) * ((double) value))) : value;
    };
    public static final a q = value -> {
        if (value == 0.0d || value == 1.0d) {
            return value;
        }
        return (float) (((double) value) < 0.5d ? Math.pow(2.0d, (20.0d * ((double) value)) - 10.0d) / 2.0d : (2.0d - Math.pow(2.0d, ((-20.0d) * ((double) value)) + 10.0d)) / 2.0d);
    };
    public static final a r = value -> {
        return (float) ((2.701578217685146d * Math.pow(value, 3.0d)) - (1.7015791506357025d * Math.pow(value, 2.0d)));
    };
    public static final a s = value -> {
        return (float) (1.0d + (2.701578217685146d * Math.pow(((double) value) - 1.0d, 3.0d)) + (1.7015791506357025d * Math.pow(((double) value) - 1.0d, 2.0d)));
    };
    public static final a t = value -> {
        return value;
    };
    public static final a u = value -> {
        return (float) (((double) value) < 0.5d ? (Math.pow(2.0d * ((double) value), 2.0d) * ((7.189816336825345d * ((double) value)) - 2.5949079670432127d)) / 2.0d : ((Math.pow((2.0d * ((double) value)) - 2.0d, 2.0d) * ((3.594908086491756d * ((((double) value) * 2.0d) - 2.0d)) + 2.5949079670432127d)) + 2.0d) / 2.0d);
    };
    public static final a v = value -> {
        if (value < 1.0d / ((double) 2.75f)) {
            return (float) (((double) 7.5625f) * Math.pow(value, 2.0d));
        }
        if (value < 2.0d / ((double) 2.75f)) {
            return (float) ((((double) 7.5625f) * Math.pow(((double) value) - (1.5d / ((double) 2.75f)), 2.0d)) + 0.75d);
        }
        return (float) (((double) value) < 2.5d / ((double) 2.75f) ? (((double) 7.5625f) * Math.pow(((double) value) - (2.25d / ((double) 2.75f)), 2.0d)) + 0.9375d : (((double) 7.5625f) * Math.pow(((double) value) - (2.625d / ((double) 2.75f)), 2.0d)) + 0.984375d);
    };
    public static final a w = value -> {
        return (float) (1.0d - ((double) v.ease((float) (1.0d - ((double) value)))));
    };
    public static final a x = value -> {
        return (float) (((double) value) < 0.5d ? (1.0d - ((double) v.ease((float) (1.0d - (2.0d * ((double) value)))))) / 2.0d : (1.0d + ((double) v.ease((float) ((2.0d * ((double) value)) - 1.0d)))) / 2.0d);
    };
    public static final a y = x2 -> {
        return ((double) x2) < 0.5d ? 16.0f * x2 * x2 * x2 * x2 * x2 : (float) (1.0d - (Math.pow(((-2.0f) * x2) + 2.0f, 5.0d) / 2.0d));
    };

    @FunctionalInterface
    public interface a {
        float ease(float f);
    }
}
