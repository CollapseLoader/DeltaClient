#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform sampler2D Sampler2;
uniform vec4 TintColor;
uniform float Time;

in vec2 TexCoord;
in vec4 FragColor;
out vec4 OutColor;

float noiseMask(vec2 uv) {
    float now = texture(Sampler1, uv).r;
    float was = texture(Sampler2, uv).r;
    return smoothstep(0.0001, 0.0004, was - now);
}

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 345.45));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + vec2(1, 0)), f.x),
               mix(hash(i + vec2(0, 1)), hash(i + vec2(1, 1)), f.x), f.y);
}

float fbm(vec2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        v += noise(p) * a;
        p = p * 2.02 + vec2(8.4, 5.7);
        a *= 0.5;
    }
    return v;
}

float ridged(vec2 p) {
    float v = 0.0;
    float a = 0.55;
    for (int i = 0; i < 4; i++) {
        float r = 1.0 - abs(noise(p) * 2.0 - 1.0);
        v += r * a;
        p = p * 2.18 + vec2(3.1, 9.2);
        a *= 0.52;
    }
    return v;
}

void main() {
    vec2 uv = TexCoord;
    float mask = noiseMask(uv);

    if (mask < 0.01) discard;

    float t = Time;
    vec2 flow = uv * 2.5;
    vec2 drift = vec2(t * 0.20, -t * 0.15);

    vec2 warp = vec2(
        fbm(flow * 0.90 + drift * 0.75 + vec2(0.0, 4.1)),
        fbm(flow * 0.78 - drift * 0.48 + vec2(3.7, 1.8))
    );
    vec2 q = flow + (warp - 0.5) * 1.8;

    float mist = fbm(q * 0.72 - drift * 0.24 + vec2(4.2, 8.1));
    float veins = pow(clamp(ridged(q * 1.85 + vec2(mist * 2.5, mist * 1.6) - drift * 0.55), 0.0, 1.0), 2.4);
    float sA = pow(clamp(1.0 - abs(sin((q.x * 1.08 + q.y * 0.42) * 1.7 + t * 0.85 + mist * 4.3)), 0.0, 1.0), 4.8);
    float sB = pow(clamp(1.0 - abs(sin((q.x * -0.58 + q.y * 1.12) * 1.45 - t * 0.65 - mist * 2.9)), 0.0, 1.0), 5.4);

    float energy = clamp(mist * 0.22 + veins * 0.88 + sA * 0.55 + sB * 0.32, 0.0, 1.0);
    float core = smoothstep(0.18, 0.98, energy);
    float accent = pow(clamp(max(veins, sA), 0.0, 1.0), 1.25);

    vec3 col = mix(TintColor.rgb, mix(TintColor.rgb, vec3(1.0), 0.4), clamp(core * 0.75 + sB * 0.25, 0.0, 1.0));
    float fill = mask * (0.26 + core * 0.82 + accent * 0.28);
    float outA = clamp(TintColor.a * fill * 0.92 * mask, 0.0, 1.0);

    if (outA <= 0.001) discard;

    OutColor = vec4(col * fill, outA);
}
