package aethereal.render;

import aethereal.ui.shader.GradientUtil;
import aethereal.util.ChatUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import platform.client.processors.draw.fonts.FontData;

import java.util.*;

public class Font {
    private final ShaderProgramKey shaderKey = new ShaderProgramKey(Identifier.of("delta", "core/text/text"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
    private final String fontName;
    private final AbstractTexture fontTexture;
    private final FontData.AtlasData atlasData;
    private final FontData.MetricsData metricsData;
    private final Map<Integer, MsdfGlyph> glyphs;
    private final Map<Integer, Map<Integer, Float>> kernings;

    public Font(String name, AbstractTexture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs, Map<Integer, Map<Integer, Float>> kernings) {
        this.fontName = name;
        this.fontTexture = texture;
        this.atlasData = atlas;
        this.metricsData = metrics;
        this.glyphs = glyphs;
        this.kernings = kernings;
    }

    public static FontBuilder a() {
        return new FontBuilder();
    }

    public String b() {
        return this.fontName;
    }

    public FontData.AtlasData c() {
        return this.atlasData;
    }

    public FontData.MetricsData d() {
        return this.metricsData;
    }

    private void a(float outlineThickness, float thickness, float smoothness, int outlineColor) {
        a(outlineThickness, thickness, smoothness, outlineColor, -1.0f, -1.0f);
    }

    private void a(float outlineThickness, float thickness, float smoothness, int outlineColor, float fadeStart, float fadeEnd) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.fontTexture.getGlId());
        ShaderProgram shader = RenderSystem.setShader(this.shaderKey);
        if (shader == null) {
            return;
        }
        shader.getUniform("uRange").set(this.atlasData.range());
        shader.getUniform("uThickness").set(thickness);
        shader.getUniform("uSmoothness").set(smoothness);
        boolean outlineEnabled = outlineThickness > 0.0f;
        shader.getUniform("uOutline").set(outlineEnabled ? 1 : 0);
        if (outlineEnabled) {
            shader.getUniform("uOutlineThickness").set(outlineThickness);
            float[] outlineComponents = ColorUtil.a(outlineColor);
            shader.getUniform("uOutlineColor").set(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }
        boolean fadeEnabled = fadeEnd > fadeStart;
        shader.getUniform("uFadeEnabled").set(fadeEnabled ? 1 : 0);
        if (fadeEnabled) {
            shader.getUniform("uFadeStart").set(fadeStart);
            shader.getUniform("uFadeEnd").set(fadeEnd);
        }
    }

    private void a(BufferBuilder builder) {
        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void a(MatrixStack matrixStack, Text text, float x, float y, float size, float alpha, float thickness, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        try {
            Matrix4f matrix = matrixStack.peek().getPositionMatrix();
            a(outlineThickness, thickness, smoothness, outlineColor);
            BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            float adjustedThickness = (thickness + (outlineThickness * 0.5f)) * 0.5f * size;
            float baselineY = y + (this.metricsData.baselineHeight() * size);
            boolean hasGlyphs = a(matrix, builder, a(text), size, alpha, adjustedThickness, spacing, x, baselineY, 0.0f);
            if (hasGlyphs) {
                a(builder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void a(MatrixStack matrixStack, String text, float x, float y, float size, float thickness, int color, int colorSecond, float offset, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        if (Objects.equals(text, "11111")) {
            ChatUtil.sendMessage(Integer.valueOf(color));
        }
        if (text == null || text.isEmpty()) {
            return;
        }
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        a(outlineThickness, thickness, smoothness, outlineColor);
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float adjustedThickness = (thickness + (outlineThickness * 0.5f)) * 0.5f * size;
        float baselineY = y + (this.metricsData.baselineHeight() * size);
        boolean hasGlyphs = a(matrix, builder, text, size, adjustedThickness, spacing, x, baselineY, 0.0f, color, colorSecond, offset);
        if (hasGlyphs) {
            a(builder);
        }
    }

    public List<MsdfGlyph.a> a(Text text) {
        List<MsdfGlyph.a> result = new ArrayList<>();
        boolean[] started = {false};
        text.visit((style, string) -> {
            if (string == null || string.isEmpty()) {
                return Optional.empty();
            }
            if (!started[0]) {
                string = string.replaceFirst("^\\s+", "");
                if (string.isEmpty()) {
                    return Optional.empty();
                }
                started[0] = true;
            }
            int color = style.getColor() != null ? style.getColor().getRgb() | (-16777216) : -1;
            result.addAll(a(string, color));
            return Optional.empty();
        }, Style.EMPTY);
        return result;
    }

    private List<MsdfGlyph.a> a(String raw, int color) {
        String raw2 = raw.replace((char) 9889, (char) 349).replace((char) 9733, (char) 350);
        List<MsdfGlyph.a> result = new ArrayList<>();
        int i = 0;
        while (i < raw2.length()) {
            char c = raw2.charAt(i);
            if (i + 1 < raw2.length()) {
                char n = raw2.charAt(i + 1);
                if (c == 3618 || c == 9889 || (c == 167 && "0123456789abcdefklor".indexOf(n) >= 0)) {
                    i++;
                } else if (this.glyphs.containsKey(Integer.valueOf(c))) {
                    result.add(new MsdfGlyph.a(c, color));
                }
            } else if (this.glyphs.containsKey(Integer.valueOf(c))) {
                result.add(new MsdfGlyph.a(c, color));
            }
            i++;
        }
        return result;
    }

    public void a(MatrixStack matrixStack, Text text, float x, float y, float size) {
        a(matrixStack, text, x, y, size, 0.0f, 1.0f);
    }

    public void a(MatrixStack matrixStack, Text text, float x, float y, float size, double alpha) {
        a(matrixStack, text, x, y, size, 0.0f, (float) alpha);
    }

    public void a(MatrixStack matrixStack, Text text, float x, float y, float size, float thickness, float alpha) {
        a(matrixStack, text, x, y, size, alpha, thickness, 0.5f, 0.0f, -1, thickness);
    }

    public void a(MatrixStack matrixStack, String text, float x, float y, float size, int color) {
        a(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void a(MatrixStack matrixStack, String text, float x, float y, float size, int color, float thickness) {
        a(matrixStack, text, x, y, size, thickness, color, -1, -1.0f, 0.5f, 0.0f, -1, thickness);
    }

    public void a(MatrixStack matrixStack, String text, float x, float y, float size, int color, float speed, float offset) {
        a(matrixStack, GradientUtil.a(text, color, speed, offset), x, y, size);
    }

    public void b(MatrixStack matrixStack, String text, float x, float y, float size, int color) {
        b(matrixStack, text, x, y, size, color, 0.0f);
    }

    public void b(MatrixStack matrixStack, String text, float x, float y, float size, int color, float thickness) {
        float textWidth = b(text, size, thickness);
        a(matrixStack, text, x - (textWidth / 2.0f), y, size, color, thickness);
    }

    public void b(MatrixStack matrixStack, String text, float x, float y, float size, int color, float speed, float offset) {
        a(matrixStack, GradientUtil.a(text, color, speed, offset), x - (a(text, size) / 2.0f), y, size);
    }

    public void c(MatrixStack matrixStack, String text, float x, float y, float size, int color, float visibleWidth) {
        c(matrixStack, text, x, y, size, color, 0.0f, visibleWidth);
    }

    public void c(MatrixStack matrixStack, String text, float x, float y, float size, int color, float thickness, float visibleWidth) {
        if (text == null || text.isEmpty() || visibleWidth <= 0.0f) {
            return;
        }
        float fadeStart = x + Math.max(0.0f, visibleWidth - 5.0f);
        float fadeEnd = x + visibleWidth;
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        a(0.0f, thickness, 0.5f, -1, fadeStart, fadeEnd);
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float adjustedThickness = (thickness + (thickness * 0.5f)) * 0.5f * size;
        float baselineY = y + (this.metricsData.baselineHeight() * size);
        boolean hasGlyphs = a(matrix, builder, text, size, adjustedThickness, 0.0f, x, baselineY, 0.0f, color, -1, -1.0f);
        if (hasGlyphs) {
            a(builder);
        }
    }

    public boolean a(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color, int colorSecond, float offset) {
        String text2 = text.replace((char) 9889, (char) 349).replace((char) 9733, (char) 350);
        int previousChar = -1;
        float totalWidth = a(text2, size);
        float time = (System.currentTimeMillis() % 3000.0f) / 3000.0f;
        boolean hasGlyphs = false;
        for (int i = 0; i < text2.length(); i++) {
            int codePoint = text2.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(codePoint));
            if (glyph != null) {
                hasGlyphs = true;
                float x2 = x + a(previousChar, codePoint, size);
                int currentColor = color;
                if (offset > 1.0f) {
                    currentColor = ColorUtil.makeGradient(color, colorSecond, x2 - x, totalWidth, time, offset);
                }
                x = x2 + glyph.a(matrix, consumer, size, x2, y, z, currentColor) + thickness + spacing;
                previousChar = codePoint;
            }
        }
        return hasGlyphs;
    }

    public boolean a(Matrix4f matrix, VertexConsumer consumer, List<MsdfGlyph.a> coloredGlyphs, float size, float alpha, float thickness, float spacing, float x, float y, float z) {
        int previousChar = -1;
        boolean started = false;
        boolean hasGlyphs = false;
        for (int i = 0; i < coloredGlyphs.size(); i++) {
            MsdfGlyph.a glyphData = coloredGlyphs.get(i);
            int codePoint = glyphData.a();
            if (started || codePoint != 32) {
                started = true;
                int color = glyphData.b();
                MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(codePoint));
                if (glyph != null) {
                    hasGlyphs = true;
                    float x2 = x + a(previousChar, codePoint, size);
                    float advance = glyph.a(matrix, consumer, size, x2, y, z, ColorUtil.applyAlphaToColor(color, alpha));
                    if (i < coloredGlyphs.size() - 1) {
                        advance += thickness + spacing;
                    }
                    x = x2 + advance;
                    previousChar = codePoint;
                }
            }
        }
        return hasGlyphs;
    }

    private float a(int previousChar, int currentChar, float size) {
        Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(previousChar));
        if (kerning == null) {
            return 0.0f;
        }
        return kerning.getOrDefault(Integer.valueOf(currentChar), Float.valueOf(0.0f)).floatValue() * size;
    }

    public float a(float size) {
        return size;
    }

    public float a(Text text, float size) {
        return a(text, size, 0.0f);
    }

    public float a(Text text, float size, float thickness) {
        if (text == null) {
            return 0.0f;
        }
        List<MsdfGlyph.a> coloredGlyphs = a(text);
        return a(coloredGlyphs, size, thickness);
    }

    public float a(String text, float size) {
        return b(text, size, 0.0f);
    }

    public float b(String text, float size) {
        MsdfGlyph glyph;
        if (text == null || text.isEmpty() || (glyph = this.glyphs.get(Integer.valueOf(text.charAt(0)))) == null) {
            return 0.0f;
        }
        return glyph.b(size);
    }

    public float a(String text, float size, float centerY) {
        if (text == null || text.isEmpty()) {
            return centerY - (a(size) / 2.0f);
        }
        MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(text.charAt(0)));
        if (glyph == null) {
            return centerY - (a(size) / 2.0f);
        }
        float inkCenter = ((this.metricsData.baselineHeight() - glyph.a()) + (glyph.b() / 2.0f)) * size;
        return centerY - inkCenter;
    }

    public float b(String text, float size, float thickness) {
        if (text == null || text.isEmpty()) {
            return 0.0f;
        }
        String text2 = text.replace((char) 9889, (char) 349).replace((char) 9733, (char) 350);
        int previousChar = -1;
        float width = 0.0f;
        int renderedGlyphs = 0;
        for (int i = 0; i < text2.length(); i++) {
            int codePoint = text2.charAt(i);
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(codePoint));
            if (glyph != null) {
                width = width + a(previousChar, codePoint, size) + glyph.a(size);
                renderedGlyphs++;
                previousChar = codePoint;
            }
        }
        return renderedGlyphs > 0 ? width + (renderedGlyphs * thickness) : width;
    }

    private float a(List<MsdfGlyph.a> coloredGlyphs, float size, float thickness) {
        int previousChar = -1;
        float width = 0.0f;
        int renderedGlyphs = 0;
        for (MsdfGlyph.a coloredGlyph : coloredGlyphs) {
            int codePoint = coloredGlyph.a();
            MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(codePoint));
            if (glyph != null) {
                width = width + a(previousChar, codePoint, size) + glyph.a(size);
                renderedGlyphs++;
                previousChar = codePoint;
            }
        }
        return renderedGlyphs > 1 ? width + ((renderedGlyphs - 1) * thickness) : width;
    }

    public float a(MatrixStack matrixStack, String text, float x, float y, float size, int color, float maxWidth, boolean isHovered, float offset, float delta) {
        if (text == null || text.isEmpty() || maxWidth <= 0.0f) {
            return 0.0f;
        }
        float textWidth = a(text, size);
        float wrap = textWidth + 12.0f;
        if (isHovered || offset > 0.0f) {
            offset += delta * 1.5f;
            if (offset >= wrap) {
                offset = isHovered ? offset - wrap : 0.0f;
            }
        }
        if (textWidth <= maxWidth || offset == 0.0f) {
            c(matrixStack, text, x, y, size, color, 0.0f, maxWidth);
            return offset;
        }
        ScissorUtil.a(matrixStack, x - 1.0f, y - (size * 0.5f), maxWidth + 2.0f, (size * 1.5f) + 0.5f);
        a(0.0f, 0.0f, 0.5f, -1, (x + maxWidth) - 5.0f, x + maxWidth);
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float baselineY = y + (this.metricsData.baselineHeight() * size);
        a(matrixStack.peek().getPositionMatrix(), builder, text, size, 0.0f, 0.0f, x - offset, baselineY, 0.0f, color, -1, -1.0f);
        a(matrixStack.peek().getPositionMatrix(), builder, text, size, 0.0f, 0.0f, (x - offset) + wrap, baselineY, 0.0f, color, -1, -1.0f);
        a(builder);
        ScissorUtil.a(matrixStack);
        return offset;
    }
}
