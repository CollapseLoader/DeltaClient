package aethereal.render;


import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import platform.client.processors.draw.fonts.FontData;

public class MsdfGlyph {
    private final int a;
    private final float b;
    private final float c;
    private final float d;
    private final float e;
    private final float f;
    private final float g;
    private final float h;
    private final float i;

    public MsdfGlyph(FontData.GlyphData data, float atlasWidth, float atlasHeight) {
        this.a = data.unicode();
        this.f = data.advance();
        FontData.BoundsData atlasBounds = data.atlasBounds();
        if (atlasBounds != null) {
            this.b = atlasBounds.left() / atlasWidth;
            this.c = atlasBounds.right() / atlasWidth;
            this.d = 1.0f - (atlasBounds.top() / atlasHeight);
            this.e = 1.0f - (atlasBounds.bottom() / atlasHeight);
        } else {
            this.b = 0.0f;
            this.c = 0.0f;
            this.d = 0.0f;
            this.e = 0.0f;
        }
        FontData.BoundsData planeBounds = data.planeBounds();
        if (planeBounds != null) {
            this.h = planeBounds.right() - planeBounds.left();
            this.i = planeBounds.top() - planeBounds.bottom();
            this.g = planeBounds.top();
        } else {
            this.h = 0.0f;
            this.i = 0.0f;
            this.g = 0.0f;
        }
    }

    public float a(Matrix4f matrix, VertexConsumer consumer, float size, float x, float y, float z, int color) {
        float adjustedY = y - (this.g * size);
        float scaledWidth = this.h * size;
        float scaledHeight = this.i * size;
        consumer.vertex(matrix, x, adjustedY, z).texture(this.b, this.d).color(color);
        consumer.vertex(matrix, x, adjustedY + scaledHeight, z).texture(this.b, this.e).color(color);
        consumer.vertex(matrix, x + scaledWidth, adjustedY + scaledHeight, z).texture(this.c, this.e).color(color);
        consumer.vertex(matrix, x + scaledWidth, adjustedY, z).texture(this.c, this.d).color(color);
        return this.f * size;
    }

    public float a(float size) {
        return this.f * size;
    }

    public float b(float size) {
        return this.h * size;
    }

    public float a() {
        return this.g;
    }

    public float b() {
        return this.i;
    }

    public int c() {
        return this.a;
    }

    public record a(char a, int b) {
    }
}
