package aethereal.ui.element;

import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;

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

    public void a(Widget widget) {
        this.b = widget;
    }

    public void a(float x) {
        this.c = x;
    }

    public void b(float y) {
        this.d = y;
    }

    public void c(float width) {
        this.e = width;
    }

    public void d(float height) {
        this.f = height;
    }

    public void a(double offsetX) {
        this.g = offsetX;
    }

    public void b(double offsetY) {
        this.h = offsetY;
    }

    public void a(int status) {
        this.j = status;
    }

    public Widget e() {
        return this.b;
    }

    public float f() {
        return this.e;
    }

    public float g() {
        return this.f;
    }

    public double h() {
        return this.g;
    }

    public double i() {
        return this.h;
    }

    public String j() {
        return this.i;
    }

    public int k() {
        return this.j;
    }

    public float a() {
        return MathUtil.b(this.c, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - this.e);
    }

    public float b() {
        return MathUtil.b(this.d, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - this.f);
    }

    public float c() {
        return this.c;
    }

    public float d() {
        return this.d;
    }
}
