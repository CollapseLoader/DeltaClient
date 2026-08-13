package aethereal.ui.element;

import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import lombok.Generated;

public class DragInfo implements Interface {
    private final String i;
    private Widget b;
    private float c;
    private float d;
    private float e;
    private float f;
    private double g = 0.0d;
    private double h = 0.0d;
    private int j = 0;

    public DragInfo(String name, float x, float y, float width, float height) {
        this.i = name;
        this.c = x;
        this.d = y;
        this.e = width;
        this.f = height;
        Delta.h().d().s().e().add(this);
    }

    @Generated
    public void a(Widget widget) {
        this.b = widget;
    }

    @Generated
    public void a(float x) {
        this.c = x;
    }

    @Generated
    public void b(float y) {
        this.d = y;
    }

    @Generated
    public void c(float width) {
        this.e = width;
    }

    @Generated
    public void d(float height) {
        this.f = height;
    }

    @Generated
    public void a(double offsetX) {
        this.g = offsetX;
    }

    @Generated
    public void b(double offsetY) {
        this.h = offsetY;
    }

    @Generated
    public void a(int status) {
        this.j = status;
    }

    @Generated
    public Widget e() {
        return this.b;
    }

    @Generated
    public float f() {
        return this.e;
    }

    @Generated
    public float g() {
        return this.f;
    }

    @Generated
    public double h() {
        return this.g;
    }

    @Generated
    public double i() {
        return this.h;
    }

    @Generated
    public String j() {
        return this.i;
    }

    @Generated
    public int k() {
        return this.j;
    }

    public float a() {
        return MathUtil.b(this.c, 0.0f, (aM_.getWindow().getFramebufferWidth() / aM_.getWindow().calculateScaleFactor(2, aM_.forcesUnicodeFont())) - this.e);
    }

    public float b() {
        return MathUtil.b(this.d, 0.0f, (aM_.getWindow().getFramebufferHeight() / aM_.getWindow().calculateScaleFactor(2, aM_.forcesUnicodeFont())) - this.f);
    }

    public float c() {
        return this.c;
    }

    public float d() {
        return this.d;
    }
}
