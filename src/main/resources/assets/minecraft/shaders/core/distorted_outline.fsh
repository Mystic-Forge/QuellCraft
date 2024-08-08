#version 150

uniform sampler2D Sampler0;
uniform float GameTime;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

vec2 distortUv(vec2 uv) {
    const float distortionSize = 8.0;
    const float distortionStrength = 14.0;
    return vec2(uv.x + sin(uv.y / distortionSize) * distortionStrength, uv.y + cos(uv.x / distortionSize) * distortionStrength);
}

float lerp(float a, float b, float t) {
    return a + (b - a) * t;
}

void main() {
    const float resolution = 256.0;
    vec2 uv = round(distortUv(round(texCoord0 * resolution))) / resolution;
    float dist = length(0.5 - uv.xy);

    const float scrollSpeed = 80.0;
    float offset = GameTime * scrollSpeed;

    vec4 noise1 = texture(Sampler0, uv + vec2(offset, offset));
    vec4 noise2 = texture(Sampler0, uv + vec2(0.25, 0.5) - vec2(offset, offset));
    float combinedNoise = noise1.r * noise2.r;
    float gradient = smoothstep(lerp(0.4, 0.0, vertexColor.r), 0.8, dist);
    float result = (combinedNoise * gradient) + gradient;

    float threshold = 0.5 - vertexColor.r * 0.4;
    const float edgeSize = 0.05;

    if (result < threshold) {
        discard;
    }

    fragColor = result > (threshold + edgeSize) ? vec4(0.0, 0.0, 0.0, 1.0) : vec4(0.56, 0.0, 0.7, 1.0);
}
