#version 150

#moj_import <delta:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;
in vec2 TexCoord;

uniform sampler2D Sampler0;
uniform vec2 uSize;
uniform vec4 uRadius;
uniform float uAlpha;
uniform float uMix;
uniform float uSmoothness;
uniform vec4 uTopLeftColor;
uniform vec4 uBottomLeftColor;
uniform vec4 uTopRightColor;
uniform vec4 uBottomRightColor;
uniform vec4 uGlowColor;
uniform float uGlowRadius;

out vec4 fragColor;

float sdf(vec2 p, vec2 b, vec4 r) {
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x = (p.y > 0.0) ? r.x : r.y;
    vec2 q = abs(p) - b + r.x;
    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r.x;
}

vec4 superSex(vec2 uv) {
    vec4 topColor = mix(uTopLeftColor, uTopRightColor, uv.x);
    vec4 bottomColor = mix(uBottomLeftColor, uBottomRightColor, uv.x);
    return mix(topColor, bottomColor, uv.y);
}

void main() {
    float gr = max(uGlowRadius, 0.0);
    if (gr > 0.001) {
        vec2 rectHalf = uSize * 0.5;
        vec2 quadSize = uSize + vec2(gr * 2.0);
        vec2 local = FragCoord * quadSize - vec2(gr);
        float sdfVal = sdf(rectHalf - local, rectHalf - 1.0, uRadius);
        float glow = 1.0 - smoothstep(-gr, gr, sdfVal);
        float outside = step(0.0, sdfVal);
        float glowA = glow * uGlowColor.a * uAlpha * outside;
        fragColor = vec4(uGlowColor.rgb, glowA);
        return;
    }
    vec2 center = uSize * 0.5;
    vec2 fragPos = center - (FragCoord * uSize);
    float dist = rdist(fragPos, center - 1.0, uRadius);
    float smoothedAlpha = ralpha(uSize, FragCoord, uRadius, uSmoothness);
    if (smoothedAlpha > 0.0) {
        vec4 texColor = texture(Sampler0, TexCoord);
        vec4 mixedColor = mix(texColor, superSex(FragCoord), uMix);
        mixedColor.a *= smoothedAlpha * uAlpha;
        fragColor = mixedColor;
    } else {
        fragColor = vec4(0.0);
    }
}