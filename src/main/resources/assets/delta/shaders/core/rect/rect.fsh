#version 150

#moj_import <delta:common.glsl>

in vec2 FragCoord;
in vec4 FragColor;

uniform vec2 uSize;
uniform vec4 uRadius;
uniform float uSmoothness;
uniform float uOutlineWidth;

out vec4 fragColor;

void main() {
    vec2 center = uSize * 0.5;
    vec2 fragPos = center - (FragCoord * uSize);
    float distOuter = rdist(fragPos, center - 1.0, uRadius);
    float alpha;

    if (uOutlineWidth > 0.001) {
        vec2 innerHalf = center - 1.0 - uOutlineWidth;
        vec4 radiusInner = max(uRadius - uOutlineWidth, vec4(0.01));
        float distInner = rdist(fragPos, innerHalf, radiusInner);
        float outerFill = 1.0 - smoothstep(1.0 - uSmoothness, 1.0, distOuter);
        float innerFill = 1.0 - smoothstep(1.0 - uSmoothness, 1.0, distInner);
        alpha = outerFill * (1.0 - innerFill);
    } else {
        alpha = 1.0 - smoothstep(1.0 - uSmoothness, 1.0, distOuter);
    }

    vec4 color = vec4(FragColor.rgb, FragColor.a * alpha);

    if (color.a == 0.0) {
        discard;
    }

    fragColor = color;
}