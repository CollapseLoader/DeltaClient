package aethereal.config;

import aethereal.render.ColorUtil;

public class ThemeConstructor {
    private String name;
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public ThemeConstructor() {
    }

    public ThemeConstructor(String name, int r, int g, int b, int a) {
        this.name = name;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int r) {
        this.red = r;
    }

    public int getGreen() {
        return this.green;
    }

    public void setGreen(int g) {
        this.green = g;
    }

    public int getBlue() {
        return this.blue;
    }

    public void setBlue(int b) {
        this.blue = b;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public void setAlpha(int a) {
        this.alpha = a;
    }

    public int toIntColor() {
        return ColorUtil.convertToARGB(this.red, this.green, this.blue, this.alpha);
    }

    public void fromIntColor(int color) {
        int[] components = ColorUtil.b(color);
        this.red = components[0];
        this.green = components[1];
        this.blue = components[2];
        this.alpha = components[3];
    }

    /**
     * @deprecated Use {@link #fromIntColor(int)}
     */
    @Deprecated
    public void a(int color) {
        fromIntColor(color);
    }

    public float getAlphaFloat() {
        return this.alpha / 255.0f;
    }

    /**
     * @deprecated Use {@link #getAlphaFloat()}
     */
    @Deprecated
    public float b() {
        return getAlphaFloat();
    }

    /**
     * @deprecated Use {@link #setAlpha(int)}
     */
    @Deprecated
    public void e(int a) {
        setAlpha(a);
    }
}
