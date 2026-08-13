#version 150

#moj_import <delta:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;

uniform vec2 uSize;
uniform vec4 uRadius;
uniform float uSmoothness;
uniform vec4 uColorModulator;
uniform vec4 uTopLeftColor;
uniform vec4 uBottomLeftColor;
uniform vec4 uTopRightColor;
uniform vec4 uBottomRightColor;

out vec4 fragColor;

vec4 superSex(vec2 uv) {
    vec4 topColor = mix(uTopLeftColor, uTopRightColor, uv.x);
    vec4 bottomColor = mix(uBottomLeftColor, uBottomRightColor, uv.x);

    return mix(topColor, bottomColor, uv.y);
}

void main() {
    vec4 gradientColor = superSex(FragCoord);

    float alpha = ralpha(uSize, FragCoord, uRadius, uSmoothness);
    vec4 color = vec4(gradientColor.rgb, gradientColor.a * alpha);

    if (color.a == 0.0) {
        discard;
    }

    fragColor = color * uColorModulator;
}