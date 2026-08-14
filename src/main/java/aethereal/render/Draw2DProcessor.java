package aethereal.render;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.Interface;
import aethereal.ui.shader.*;
import aethereal.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Draw2DProcessor extends BaseProcessor implements Interface {
    private final RectangleShader c = new RectangleShader();
    private final TextureShader d = new TextureShader();
    private final GradientShader e = new GradientShader();
    private final BlurShader f = new BlurShader();
    private final NoiseShader g = new NoiseShader();
    private float b = 1.0f;

    @Override
    @Compile
    public void setup() {
    }

    @Generated
    public void a(float scale) {
        this.b = scale;
    }

    @Generated
    public float a() {
        return this.b;
    }

    @Generated
    public RectangleShader b() {
        return this.c;
    }

    @Generated
    public TextureShader c() {
        return this.d;
    }

    @Generated
    public GradientShader d() {
        return this.e;
    }

    @Generated
    public BlurShader e() {
        return this.f;
    }

    @Generated
    public NoiseShader f() {
        return this.g;
    }

    @Override
    public void unSetup() {
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, int color) {
        a(matrices, x, y, width, height, b(radius), color);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, int color) {
        float[] padding = MathUtil.b(0.8f);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float drawX = x - (padding[0] / 2.0f);
        float drawY = y - (padding[1] / 2.0f);
        float drawWidth = width + padding[0];
        float drawHeight = height + padding[1];
        g();
        this.c.a();
        this.c.a(width, height);
        this.c.a(radius);
        this.c.a(0.8f);
        this.c.b(0.0f);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, color);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, float outlineWidth, int color) {
        a(matrices, x, y, width, height, b(radius), outlineWidth, color);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, float outlineWidth, int color) {
        float[] padding = MathUtil.b(0.8f);
        float halfOutlineWidth = outlineWidth * 0.5f;
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float drawX = (x - halfOutlineWidth) - (padding[0] / 2.0f);
        float drawY = (y - halfOutlineWidth) - (padding[1] / 2.0f);
        float drawWidth = width + outlineWidth + padding[0];
        float drawHeight = height + outlineWidth + padding[1];
        g();
        this.c.a();
        this.c.a(width + outlineWidth, height + outlineWidth);
        this.c.a(new Vector4f(radius.x + halfOutlineWidth, radius.y + halfOutlineWidth, radius.z + halfOutlineWidth, radius.w + halfOutlineWidth));
        this.c.a(0.8f);
        this.c.b(outlineWidth);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, color);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void a(MatrixStack matrices, Identifier texture, float x, float y, float width, float height, float radius, int color) {
        a(matrices, x, y, width, height, radius, color, 0.0f, 0.0f, 1.0f, 1.0f, mc.getTextureManager().getTexture(texture).getGlId());
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, int color, float u, float v, float textureWidth, float textureHeight, int textureId) {
        a(matrices, x, y, width, height, b(radius), color, u, v, textureWidth, textureHeight, textureId);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, int color, float u, float v, float textureWidth, float textureHeight, int textureId) {
        float[] padding = MathUtil.b(0.8f);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float drawX = x - (padding[0] / 2.0f);
        float drawY = y - (padding[1] / 2.0f);
        float drawWidth = width + padding[0];
        float drawHeight = height + padding[1];
        g();
        RenderSystem.setShaderTexture(0, textureId);
        this.d.a();
        this.d.a(width, height);
        this.d.a(radius);
        this.d.a(0.8f);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, u, v, textureWidth, textureHeight, color);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void a(MatrixStack matrices, Identifier skin, LivingEntity target, float x, float y, float width, float height, float radius, float alpha) {
        if (skin == null) {
            return;
        }
        int color = ColorUtil.a(255, 255, 255, (int) (alpha * 255.0f));
        int textureId = mc.getTextureManager().getTexture(skin).getGlId();
        a(matrices, x, y, width, height, radius, color, 0.125f, 0.125f, 0.125f, 0.125f, textureId);
        a(matrices, x, y, width, height, radius, color, 0.625f, 0.125f, 0.125f, 0.125f, textureId);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
        a(matrices, x, y, width, height, b(radius), topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
        float[] padding = MathUtil.b(1.0f);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float[] normalizedTopLeft = ColorUtil.a(topLeftColor);
        float[] normalizedBottomLeft = ColorUtil.a(bottomLeftColor);
        float[] normalizedBottomRight = ColorUtil.a(bottomRightColor);
        float[] normalizedTopRight = ColorUtil.a(topRightColor);
        float drawX = x - (padding[0] / 2.0f);
        float drawY = y - (padding[1] / 2.0f);
        float drawWidth = width + padding[0];
        float drawHeight = height + padding[1];
        g();
        this.e.a();
        this.e.a(width, height);
        this.e.a(radius);
        this.e.a(1.0f);
        this.e.a(normalizedTopLeft[0], normalizedTopLeft[1], normalizedTopLeft[2], normalizedTopLeft[3]);
        this.e.b(normalizedBottomLeft[0], normalizedBottomLeft[1], normalizedBottomLeft[2], normalizedBottomLeft[3]);
        this.e.d(normalizedBottomRight[0], normalizedBottomRight[1], normalizedBottomRight[2], normalizedBottomRight[3]);
        this.e.c(normalizedTopRight[0], normalizedTopRight[1], normalizedTopRight[2], normalizedTopRight[3]);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, drawX, drawY, 0.0f).color(topLeftColor);
        buffer.vertex(matrix, drawX, drawY + drawHeight, 0.0f).color(bottomLeftColor);
        buffer.vertex(matrix, drawX + drawWidth, drawY + drawHeight, 0.0f).color(bottomRightColor);
        buffer.vertex(matrix, drawX + drawWidth, drawY, 0.0f).color(topRightColor);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void b(MatrixStack matrices, float x, float y, float width, float height, float radius, int color) {
        a(matrices, x, y, width, height, radius, color, 0.8f);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, int color, float mix) {
        a(matrices, x, y, width, height, b(radius), color, color, color, color, mix);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor, float mix) {
        if (this.f.e().isEmpty()) {
            return;
        }
        Framebuffer framebuffer = this.f.e().getFirst();
        float uLeft = 0.0f;
        float uRight = 0.0f;
        float vTop = 0.0f;
        float vBottom = 0.0f;
        if (mix != 1.0f) {
            float scale = framebuffer.textureWidth / mc.getWindow().getScaledWidth();
            uLeft = (x * scale) / framebuffer.textureWidth;
            uRight = ((x + width) * scale) / framebuffer.textureWidth;
            vTop = 1.0f - ((y * scale) / framebuffer.textureHeight);
            vBottom = 1.0f - (((y + height) * scale) / framebuffer.textureHeight);
        }
        float[] normalizedTopLeft = ColorUtil.a(topLeftColor);
        float[] normalizedBottomLeft = ColorUtil.a(bottomLeftColor);
        float[] normalizedBottomRight = ColorUtil.a(bottomRightColor);
        float[] normalizedTopRight = ColorUtil.a(topRightColor);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float drawX = x - (1.2f * 0.5f);
        float drawY = y - (1.2f * 0.5f);
        float drawWidth = width + 1.2f;
        float drawHeight = height + 1.2f;
        g();
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());
        this.f.a();
        this.f.a(width, height);
        this.f.a(radius);
        this.f.a(0.8f);
        this.f.b(mix);
        this.f.c((normalizedTopLeft[3] + normalizedBottomLeft[3] + normalizedBottomRight[3] + normalizedTopRight[3]) * 0.25f);
        this.f.a(normalizedTopLeft[0], normalizedTopLeft[1], normalizedTopLeft[2], normalizedTopLeft[3]);
        this.f.b(normalizedBottomLeft[0], normalizedBottomLeft[1], normalizedBottomLeft[2], normalizedBottomLeft[3]);
        this.f.d(normalizedBottomRight[0], normalizedBottomRight[1], normalizedBottomRight[2], normalizedBottomRight[3]);
        this.f.c(normalizedTopRight[0], normalizedTopRight[1], normalizedTopRight[2], normalizedTopRight[3]);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix, drawX, drawY, 0.0f).texture(uLeft, vTop).color(topLeftColor);
        buffer.vertex(matrix, drawX, drawY + drawHeight, 0.0f).texture(uLeft, vBottom).color(bottomLeftColor);
        buffer.vertex(matrix, drawX + drawWidth, drawY + drawHeight, 0.0f).texture(uRight, vBottom).color(bottomRightColor);
        buffer.vertex(matrix, drawX + drawWidth, drawY, 0.0f).texture(uRight, vTop).color(topRightColor);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, float radius, int color, float alpha, int glowColor, float glowRadius) {
        a(matrices, x, y, width, height, b(radius), color, alpha, glowColor, glowRadius);
    }

    public void a(MatrixStack matrices, float x, float y, float width, float height, Vector4f radius, int color, float alpha, int glowColor, float glowRadius) {
        if (this.f.e().isEmpty()) {
            return;
        }
        float clampedGlowRadius = Math.max(glowRadius, 0.0f);
        float padding = 0.8f * 1.5f;
        Framebuffer framebuffer = this.f.e().getFirst();
        float scale = framebuffer.textureWidth / mc.getWindow().getScaledWidth();
        float uLeft = (x * scale) / framebuffer.textureWidth;
        float uRight = ((x + width) * scale) / framebuffer.textureWidth;
        float vTop = 1.0f - ((y * scale) / framebuffer.textureHeight);
        float vBottom = 1.0f - (((y + height) * scale) / framebuffer.textureHeight);
        float[] normalizedGlowColor = ColorUtil.a(glowColor);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        a(matrix, x - (padding * 0.5f), y - (padding * 0.5f), width + padding, height + padding, width, height, radius, alpha, 0.8f, 0.0f, null, uLeft, vTop, uRight, vBottom, framebuffer);
        float innerWidth = width - ((-1.1f) * 2.0f);
        float innerHeight = height - ((-1.1f) * 2.0f);
        Vector4f innerRadius = new Vector4f(Math.max(0.0f, radius.x - (-1.1f)), Math.max(0.0f, radius.y - (-1.1f)), Math.max(0.0f, radius.z - (-1.1f)), Math.max(0.0f, radius.w - (-1.1f)));
        a(matrix, (x - 1.1f) - clampedGlowRadius, (y - 1.1f) - clampedGlowRadius, innerWidth + (clampedGlowRadius * 2.0f), innerHeight + (clampedGlowRadius * 2.0f), innerWidth, innerHeight, innerRadius, alpha, 0.8f, clampedGlowRadius, normalizedGlowColor, uLeft, vTop, uRight, vBottom, framebuffer);
        a(matrices, x, y, width, height, radius, color);
    }

    public void b(MatrixStack matrices, float x, float y, float width, float height, float radius, int color, float alpha) {
        if (this.f.e().isEmpty()) {
            return;
        }
        float padding = 0.8f * 1.5f;
        Framebuffer framebuffer = this.f.e().getFirst();
        float scale = framebuffer.textureWidth / mc.getWindow().getScaledWidth();
        float uLeft = (x * scale) / framebuffer.textureWidth;
        float uRight = ((x + width) * scale) / framebuffer.textureWidth;
        float vTop = 1.0f - ((y * scale) / framebuffer.textureHeight);
        float vBottom = 1.0f - (((y + height) * scale) / framebuffer.textureHeight);
        a(matrices.peek().getPositionMatrix(), x - (padding * 0.5f), y - (padding * 0.5f), width + padding, height + padding, width, height, b(radius), alpha, 0.8f, 0.0f, null, uLeft, vTop, uRight, vBottom, framebuffer);
        a(matrices, x, y, width, height, radius, color);
    }

    private void a(Matrix4f matrix, float drawX, float drawY, float drawWidth, float drawHeight, float width, float height, Vector4f radius, float alpha, float smoothness, float glowRadius, float[] glowColor, float uLeft, float vTop, float uRight, float vBottom, Framebuffer framebuffer) {
        g();
        RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());
        this.f.a();
        this.f.a(width, height);
        this.f.a(radius);
        this.f.c(alpha);
        this.f.d(glowRadius);
        if (glowColor != null) {
            this.f.e(glowColor[0], glowColor[1], glowColor[2], glowColor[3]);
        }
        this.f.a(smoothness);
        this.f.b(0.0f);
        this.f.a(1.0f, 1.0f, 1.0f, 1.0f);
        this.f.b(1.0f, 1.0f, 1.0f, 1.0f);
        this.f.d(1.0f, 1.0f, 1.0f, 1.0f);
        this.f.c(1.0f, 1.0f, 1.0f, 1.0f);
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix, drawX, drawY, 0.0f).texture(uLeft, vTop).color(-1);
        buffer.vertex(matrix, drawX, drawY + drawHeight, 0.0f).texture(uLeft, vBottom).color(-1);
        buffer.vertex(matrix, drawX + drawWidth, drawY + drawHeight, 0.0f).texture(uRight, vBottom).color(-1);
        buffer.vertex(matrix, drawX + drawWidth, drawY, 0.0f).texture(uRight, vTop).color(-1);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        h();
    }

    public void a(DrawContext context, float x, float y, float width, float height, int color) {
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0.0f);
        context.getMatrices().scale(width, height, 1.0f);
        context.fill(0, 0, 1, 1, color);
        context.getMatrices().pop();
    }

    private void g() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
    }

    private void h() {
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private Vector4f b(float radius) {
        return new Vector4f(radius, radius, radius, radius);
    }

    private void a(BufferBuilder buffer, Matrix4f matrix, float x, float y, float width, float height, int color) {
        buffer.vertex(matrix, x, y, 0.0f).color(color);
        buffer.vertex(matrix, x, y + height, 0.0f).color(color);
        buffer.vertex(matrix, x + width, y + height, 0.0f).color(color);
        buffer.vertex(matrix, x + width, y, 0.0f).color(color);
    }

    private void a(BufferBuilder buffer, Matrix4f matrix, float x, float y, float width, float height, float u, float v, float textureWidth, float textureHeight, int color) {
        buffer.vertex(matrix, x, y, 0.0f).texture(u, v).color(color);
        buffer.vertex(matrix, x, y + height, 0.0f).texture(u, v + textureHeight).color(color);
        buffer.vertex(matrix, x + width, y + height, 0.0f).texture(u + textureWidth, v + textureHeight).color(color);
        buffer.vertex(matrix, x + width, y, 0.0f).texture(u + textureWidth, v).color(color);
    }
}
