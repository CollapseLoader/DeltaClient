#version 150

in vec2 TexCoord;

uniform sampler2D Sampler0;
uniform vec2 uHalfTexelSize;
uniform float uOffset;

out vec4 fragColor;

void main() {
    fragColor = (
        texture(Sampler0, TexCoord + vec2(-uHalfTexelSize.x * 2, 0) * uOffset) +
        texture(Sampler0, TexCoord + vec2(-uHalfTexelSize.x, uHalfTexelSize.y) * uOffset) * 2 +
        texture(Sampler0, TexCoord + vec2(0, uHalfTexelSize.y * 2) * uOffset) +
        texture(Sampler0, TexCoord + uHalfTexelSize * uOffset) * 2 +
        texture(Sampler0, TexCoord + vec2(uHalfTexelSize.x * 2, 0) * uOffset) +
        texture(Sampler0, TexCoord + vec2(uHalfTexelSize.x, -uHalfTexelSize.y) * uOffset) * 2 +
        texture(Sampler0, TexCoord + vec2(0, -uHalfTexelSize.y * 2) * uOffset) +
        texture(Sampler0, TexCoord - uHalfTexelSize * uOffset) * 2
    ) / 12;

    fragColor.a = 1;
}