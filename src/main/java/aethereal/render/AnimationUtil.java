package aethereal.render;

import aethereal.core.Interface;
import aethereal.util.MathUtil;

public class AnimationUtil implements Interface {
    private float currentValue;
    private float previousValue;
    private float animationSpeed;
    private float animationValue;
    private float fromValue = 0.0f;
    private float toValue = 1.0f;
    private long lastUpdateTime = System.currentTimeMillis();

    public void c(float value) {
        this.currentValue = value;
    }

    public void d(float prevValue) {
        this.previousValue = prevValue;
    }

    public float a() {
        return this.currentValue;
    }

    public float b() {
        return this.previousValue;
    }

    public float c() {
        return this.animationValue;
    }

    public void e(float animationValue) {
        this.animationValue = animationValue;
    }

    public void a(boolean expanding) {
        this.previousValue = this.currentValue;
        float direction = expanding ? 1.0f : -1.0f;
        this.currentValue = MathUtil.b(this.currentValue + (direction * this.animationSpeed * 20.0f * d()), this.fromValue, this.toValue);
    }

    public void a(float fromValue, float toValue, float animationSpeed, EasingList.a easing, float partialTicks) {
        this.animationSpeed = animationSpeed;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.animationValue = MathUtil.a(this.previousValue, this.currentValue, partialTicks);
    }

    public void a(float amount) {
        this.toValue += amount;
    }

    public void b(float value) {
        this.toValue = value;
        this.currentValue = value;
    }

    public float a(float min, float max, float speed) {
        this.toValue = MathUtil.b(this.toValue, min, max);
        this.currentValue = MathUtil.c(this.currentValue, this.toValue, speed);
        return this.currentValue;
    }

    public float a(float target, float speed) {
        return a(target, target, speed);
    }

    private float d() {
        long now = System.currentTimeMillis();
        float delta = (now - this.lastUpdateTime) / 1000.0f;
        this.lastUpdateTime = now;
        return delta;
    }
}
