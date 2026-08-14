package aethereal.event;

import aethereal.core.Delta;
import aethereal.core.Event;

import aethereal.core.Interface;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class DrawEvent extends Event implements Interface {
    private final Draw2DProcessor b = Delta.getInstance().getModuleProcessor().i();
    private final Draw3DProcessor c = Delta.getInstance().getModuleProcessor().j();
    private final a d;
    private final float e;
    private final MatrixStack f;
    private DrawContext g;

    public DrawEvent(MatrixStack stack, float tickDelta, a type) {
        this.f = stack;
        this.e = tickDelta;
        this.d = type;
    }

    public DrawEvent(DrawContext context, float tickDelta, a type) {
        this.g = context;
        this.f = context.getMatrices();
        this.e = tickDelta;
        this.d = type;
    }

    public Draw2DProcessor d() {
        return this.b;
    }

    public Draw3DProcessor e() {
        return this.c;
    }

    public a f() {
        return this.d;
    }

    public float g() {
        return this.e;
    }

    public MatrixStack h() {
        return this.f;
    }

    public DrawContext i() {
        return this.g;
    }

    public boolean b() {
        return this.d == a.D2D;
    }

    public boolean c() {
        return this.d == a.D3D;
    }

    public enum a {
        D2D,
        D3D
    }
}
