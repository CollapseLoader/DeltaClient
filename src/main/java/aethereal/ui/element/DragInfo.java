package aethereal.ui.element;

import aethereal.core.Delta;
import aethereal.core.Interface;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;

public class DragInfo implements Interface {
    private final String name;
    private Widget widget;
    private float x;
    private float y;
    private float width;
    private float height;
    private double offsetX = 0.0d;
    private double offsetY = 0.0d;
    private int dragStatus = 0;

    public DragInfo(String name, float x, float y, float width, float height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Delta.getInstance().getModuleProcessor().s().e().add(this);
    }

    public Widget getWidget() {
        return this.widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public String getName() {
        return this.name;
    }

    public int getDragStatus() {
        return this.dragStatus;
    }

    public void setDragStatus(int status) {
        this.dragStatus = status;
    }

    public float getClampedX() {
        return MathUtil.b(this.x, 0.0f, (mc.getWindow().getFramebufferWidth() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - this.width);
    }

    public float getClampedY() {
        return MathUtil.b(this.y, 0.0f, (mc.getWindow().getFramebufferHeight() / mc.getWindow().calculateScaleFactor(2, mc.forcesUnicodeFont())) - this.height);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
