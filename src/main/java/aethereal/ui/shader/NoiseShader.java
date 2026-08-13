package aethereal.ui.shader;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ResizeEvent;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.awt.*;

public class NoiseShader extends Shader implements Interface {
    private static final Identifier c = Identifier.of("delta", "core/noise/noise_shader");
    private final Matrix4f d;
    private SimpleFramebuffer e;
    private Uniform f;
    private Uniform g;

    public NoiseShader() {
        super(c, VertexFormats.POSITION_COLOR);
        this.d = new Matrix4f();
        EventManager.a(this);
    }

    @EventTarget
    public void a(ResizeEvent event) {
        this.e = new SimpleFramebuffer(aM_.getWindow().getFramebufferWidth(), aM_.getWindow().getFramebufferHeight(), true);
    }

    public void e() {
        if (this.e != null) {
            this.e.copyDepthFrom(aM_.getFramebuffer());
        }
    }

    @Override
    protected void b() {
        this.f = a("TintColor");
        this.g = a("Time");
    }

    public void a(float[] color) {
        if (this.e != null) {
            RenderSystem.backupProjectionMatrix();
            RenderSystem.setProjectionMatrix(this.d, ProjectionType.PERSPECTIVE);
            Matrix4fStack modelView = RenderSystem.getModelViewStack();
            modelView.pushMatrix().identity();
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.setShaderTexture(0, aM_.getFramebuffer().getColorAttachment());
            RenderSystem.setShaderTexture(1, aM_.getFramebuffer().getDepthAttachment());
            RenderSystem.setShaderTexture(2, this.e.getDepthAttachment());
            a();
            if (this.f != null) {
                this.f.set(color[0], color[1], color[2], color[3]);
            }
            if (this.g != null) {
                this.g.set((System.currentTimeMillis() % 100000) / 1000.0f);
            }
            int white = new Color(255, 255, 255, 255).getRGB();
            BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            builder.vertex(-1.0f, -1.0f, 0.0f).color(white);
            builder.vertex(-1.0f, 1.0f, 0.0f).color(white);
            builder.vertex(1.0f, 1.0f, 0.0f).color(white);
            builder.vertex(1.0f, -1.0f, 0.0f).color(white);
            BufferRenderer.drawWithGlobalProgram(builder.end());
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.setShaderTexture(1, 0);
            RenderSystem.setShaderTexture(2, 0);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            modelView.popMatrix();
            RenderSystem.restoreProjectionMatrix();
        }
    }
}
