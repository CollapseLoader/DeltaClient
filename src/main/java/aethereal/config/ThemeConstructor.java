package aethereal.config;


import aethereal.render.ColorUtil;

import lombok.Generated;

public class ThemeConstructor {
    private String name;
    private int red;
    private int green;
    private int blue;
    private int alpha;

    @Generated
    public ThemeConstructor() {
    }

    @Generated
    public ThemeConstructor(String name, int r, int g, int b, int a) {
        this.name = name;
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public int getRed() {
        return this.red;
    }

    @Generated
    public void setRed(int r) {
        this.red = r;
    }

    @Generated
    public int getGreen() {
        return this.green;
    }

    @Generated
    public void setGreen(int g) {
        this.green = g;
    }

    @Generated
    public int getBlue() {
        return this.blue;
    }

    @Generated
    public void setBlue(int b) {
        this.blue = b;
    }

    @Generated
    public int getAlpha() {
        return this.alpha;
    }

    @Generated
    public void setAlpha(int a) {
        this.alpha = a;
    }

    public int toIntColor() {
        return ColorUtil.a(this.red, this.green, this.blue, this.alpha);
    }

    /**
     * @deprecated Use {@link #toIntColor()}
     */
    @Deprecated
    public int a() {
        return toIntColor();
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
