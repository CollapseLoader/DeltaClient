package platform.client.processors.draw.fonts;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class FontData {
    private AtlasData atlas;
    private MetricsData metrics;
    private List<GlyphData> glyphs;

    @SerializedName("kerning")
    private List<KerningData> kernings;

    public AtlasData atlas() {
        return this.atlas;
    }

    public MetricsData metrics() {
        return this.metrics;
    }

    public List<GlyphData> glyphs() {
        return this.glyphs;
    }

    public List<KerningData> kernings() {
        return this.kernings;
    }

    public record AtlasData(@SerializedName("distanceRange") float range, float width, float height) {

    }

    public static final class MetricsData {
        private float lineHeight;
        private float ascender;
        private float descender;

        public float lineHeight() {
            return this.lineHeight;
        }

        public float ascender() {
            return this.ascender;
        }

        public float descender() {
            return this.descender;
        }

        public float baselineHeight() {
            return this.lineHeight + this.descender;
        }
    }

    public record GlyphData(int unicode, float advance, BoundsData planeBounds, BoundsData atlasBounds) {
    }

    public record BoundsData(float left, float top, float right, float bottom) {
    }

    public record KerningData(@SerializedName("unicode1") int leftChar, @SerializedName("unicode2") int rightChar,
                              float advance) {

    }
}
