#version 150

in vec2 TexCoord;
in vec4 FragColor;
in float FragX;

uniform sampler2D Sampler0;
uniform float uRange;
uniform float uThickness;
uniform float uSmoothness;
uniform bool uOutline;
uniform float uOutlineThickness;
uniform vec4 uOutlineColor;
uniform int uFadeEnabled;
uniform float uFadeStart;
uniform float uFadeEnd;

out vec4 fragColor;

float median(vec3 color) {
    return max(min(color.r, color.g), min(max(color.r, color.g), color.b));
}

void main() {
    float dist = median(texture(Sampler0, TexCoord).rgb) - 0.5 + uThickness;
    vec2 h = vec2(dFdx(TexCoord.x), dFdy(TexCoord.y)) * textureSize(Sampler0, 0);
    float pixels = uRange * inversesqrt(h.x * h.x + h.y * h.y);
    float alpha = smoothstep(-uSmoothness, uSmoothness, dist * pixels);
    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (uOutline) {
        color = mix(uOutlineColor, FragColor, alpha);
        color.a *= smoothstep(-uSmoothness, uSmoothness, (dist + uOutlineThickness) * pixels);
    }

    if (uFadeEnabled != 0) {
        color.a *= 1.0 - smoothstep(uFadeStart, uFadeEnd, FragX);
    }

    fragColor = color;
}