#version 150

in vec2 TexCoord;

uniform sampler2D Sampler0;
uniform vec2 uHalfTexelSize;
uniform float uOffset;

out vec4 fragColor;

void main() {
    fragColor = (
        texture(Sampler0, TexCoord) * 4 +
        texture(Sampler0, TexCoord - uHalfTexelSize.xy * uOffset) +
        texture(Sampler0, TexCoord + uHalfTexelSize.xy * uOffset) +
        texture(Sampler0, TexCoord + vec2(uHalfTexelSize.x, -uHalfTexelSize.y) * uOffset) +
        texture(Sampler0, TexCoord - vec2(uHalfTexelSize.x, -uHalfTexelSize.y) * uOffset)
    ) / 8;

    fragColor.a = 1;
}