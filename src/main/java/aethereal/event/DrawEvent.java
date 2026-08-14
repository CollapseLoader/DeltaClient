package aethereal.event;

import aethereal.core.Delta;
import aethereal.core.Event;

import aethereal.core.Interface;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class DrawEvent extends Event implements Interface {
    private final Draw2DProcessor draw2DProcessor = Delta.getInstance().getModuleProcessor().i();
    private final Draw3DProcessor draw3DProcessor = Delta.getInstance().getModuleProcessor().j();
    private final a type;
    private final float tickDelta;
    private final MatrixStack matrixStack;
    private DrawContext drawContext;

    public DrawEvent(MatrixStack stack, float tickDelta, a type) {
        this.matrixStack = stack;
        this.tickDelta = tickDelta;
        this.type = type;
    }

    public DrawEvent(DrawContext context, float tickDelta, a type) {
        this.drawContext = context;
        this.matrixStack = context.getMatrices();
        this.tickDelta = tickDelta;
        this.type = type;
    }

    public Draw2DProcessor getDraw2DProcessor() {
        return this.draw2DProcessor;
    }

    public Draw3DProcessor getDraw3DProcessor() {
        return this.draw3DProcessor;
    }

    public a getType() {
        return this.type;
    }

    public float g() {
        return this.tickDelta;
    }

    public MatrixStack h() {
        return this.matrixStack;
    }

    public DrawContext i() {
        return this.drawContext;
    }

    public boolean b() {
        return this.type == a.D2D;
    }

    public boolean c() {
        return this.type == a.D3D;
    }

    public enum a {
        D2D,
        D3D
    }
}
