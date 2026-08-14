package aethereal.ui.shader;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ResizeEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class BlurShader extends Shader implements Interface {
    private final List<SimpleFramebuffer> n;
    private final ShaderProgramKey o;
    private final ShaderProgramKey p;
    public Uniform c;
    public Uniform d;
    public Uniform e;
    public Uniform f;
    public Uniform g;
    public Uniform h;
    public Uniform i;
    public Uniform j;
    public Uniform k;
    public Uniform l;
    public Uniform m;

    public BlurShader() {
        super(Identifier.of("delta", "core/rect/blurred_rect"), VertexFormats.POSITION_TEXTURE_COLOR);
        this.n = new ArrayList<>();
        this.o = new ShaderProgramKey(Identifier.of("delta", "core/blur/upscale"), VertexFormats.POSITION, Defines.EMPTY);
        this.p = new ShaderProgramKey(Identifier.of("delta", "core/blur/downscale"), VertexFormats.POSITION, Defines.EMPTY);
        EventManager.a(this);
    }

    public List<SimpleFramebuffer> e() {
        return this.n;
    }

    @EventTarget
    public void a(ResizeEvent event) {
        this.n.clear();
        for (int i = 0; i <= 3; i++) {
            this.n.add(f());
        }
    }

    @Override
    protected void b() {
        this.c = a("uSize");
        this.d = a("uRadius");
        this.e = a("uSmoothness");
        this.f = a("uMix");
        this.g = a("uAlpha");
        this.h = a("uTopLeftColor");
        this.i = a("uBottomLeftColor");
        this.j = a("uTopRightColor");
        this.k = a("uBottomRightColor");
        this.l = a("uGlowColor");
        this.m = a("uGlowRadius");
    }

    public void a(MatrixStack matrixStack) {
        if (!this.n.isEmpty()) {
            int actualPasses = Math.max(this.n.size() - 1, 1);
            try {
                a(matrixStack, this.p, mc.getFramebuffer(), this.n.getFirst(), 0, 24);
                for (int i = 0; i < actualPasses; i++) {
                    a(matrixStack, this.p, this.n.get(i), this.n.get(i + 1), i + 1, 24);
                }
                for (int i2 = actualPasses; i2 > 0; i2--) {
                    a(matrixStack, this.o, this.n.get(i2), this.n.get(i2 - 1), i2, 24);
                }
            } finally {
                mc.getFramebuffer().beginWrite(false);
            }
        }
    }

    private void a(MatrixStack matrixStack, ShaderProgramKey shaderKey, Framebuffer source, Framebuffer destination, int pass, int offset) {
        destination.beginWrite(false);
        RenderSystem.setShaderTexture(0, source.getColorAttachment());
        ShaderProgram shader = RenderSystem.setShader(shaderKey);
        GlUniform class_284VarMethod_34582 = shader.getUniform("uHalfTexelSize");
        GlUniform class_284VarMethod_34583 = shader.getUniform("uOffset");
        if (class_284VarMethod_34582 != null) {
            class_284VarMethod_34582.set(0.5f / source.textureWidth, 0.5f / source.textureHeight);
        }
        if (class_284VarMethod_34583 != null) {
            class_284VarMethod_34583.set(offset * (pass / 3.0f));
        }
        a(matrixStack.peek().getPositionMatrix());
        destination.endWrite();
    }

    private void a(Matrix4f matrix4f) {
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        builder.vertex(matrix4f, 0.0f, 0.0f, 0.0f);
        builder.vertex(matrix4f, 0.0f, mc.getWindow().getScaledHeight(), 0.0f);
        builder.vertex(matrix4f, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), 0.0f);
        builder.vertex(matrix4f, mc.getWindow().getScaledWidth(), 0.0f, 0.0f);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }

    private SimpleFramebuffer f() {
        return new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false);
    }

    public void a(float width, float height) {
        if (this.c != null) {
            this.c.set(width, height);
        }
    }

    public void a(Vector4f radius) {
        if (this.d != null) {
            this.d.set(radius.x, radius.z, radius.w, radius.y);
        }
    }

    public void a(float smoothness) {
        if (this.e != null) {
            this.e.set(smoothness);
        }
    }

    public void b(float mix) {
        if (this.f != null) {
            this.f.set(mix);
        }
    }

    public void c(float alpha) {
        if (this.g != null) {
            this.g.set(alpha);
        }
    }

    public void a(float r, float g, float b, float a) {
        if (this.h != null) {
            this.h.set(r, g, b, a);
        }
    }

    public void b(float r, float g, float b, float a) {
        if (this.i != null) {
            this.i.set(r, g, b, a);
        }
    }

    public void c(float r, float g, float b, float a) {
        if (this.j != null) {
            this.j.set(r, g, b, a);
        }
    }

    public void d(float r, float g, float b, float a) {
        if (this.k != null) {
            this.k.set(r, g, b, a);
        }
    }

    public void e(float r, float g, float b, float a) {
        if (this.l != null) {
            this.l.set(r, g, b, a);
        }
    }

    public void d(float r) {
        if (this.m != null) {
            this.m.set(r);
        }
    }
}
