package aethereal.render;

import aethereal.core.Interface;
import aethereal.util.MathUtil;
import lombok.Generated;

public class AnimationUtil implements Interface {
    private float b;
    private float c;
    private float d;
    private float g;
    private float e = 0.0f;
    private float f = 1.0f;
    private long h = System.currentTimeMillis();

    @Generated
    public void c(float value) {
        this.b = value;
    }

    @Generated
    public void d(float prevValue) {
        this.c = prevValue;
    }

    @Generated
    public float a() {
        return this.b;
    }

    @Generated
    public float b() {
        return this.c;
    }

    @Generated
    public float c() {
        return this.g;
    }

    @Generated
    public void e(float animationValue) {
        this.g = animationValue;
    }

    public void a(boolean expanding) {
        this.c = this.b;
        float direction = expanding ? 1.0f : -1.0f;
        this.b = MathUtil.b(this.b + (direction * this.d * 20.0f * d()), this.e, this.f);
    }

    public void a(float fromValue, float toValue, float animationSpeed, EasingList.a easing, float partialTicks) {
        this.d = animationSpeed;
        this.e = fromValue;
        this.f = toValue;
        this.g = MathUtil.a(this.c, this.b, partialTicks);
    }

    public void a(float amount) {
        this.f += amount;
    }

    public void b(float value) {
        this.f = value;
        this.b = value;
    }

    public float a(float min, float max, float speed) {
        this.f = MathUtil.b(this.f, min, max);
        this.b = MathUtil.c(this.b, this.f, speed);
        return this.b;
    }

    public float a(float target, float speed) {
        return a(target, target, speed);
    }

    private float d() {
        long now = System.currentTimeMillis();
        float delta = (now - this.h) / 1000.0f;
        this.h = now;
        return delta;
    }
}
