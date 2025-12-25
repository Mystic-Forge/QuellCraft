#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

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

vec2 hash(vec2 p) {
    p = vec2(dot(p, vec2(127.1, 311.7)), dot(p, vec2(269.5, 183.3)));
    return -1.0 + 2.0 * fract(sin(p) * 43758.5453123);
}

void main() {
    float aspectRatio = vertexColor.g * 3.0;
    vec2 resolution = vec2(128.0 * aspectRatio, 128.0);
    vec2 uv = (floor(distortUv(floor(texCoord0 * resolution))) / resolution);

//    fragColor = vec4(hash(uv) * 0.3, 0.0, 1.0);
//    if(length(texCoord0) < 0.001)
//        fragColor = vec4(1.0, 1.0, 1.0, 1.0);
//    return;

    float dist = length(0.5 - uv.xy);

    const float scrollSpeed = 60.0;
    float offset = GameTime * scrollSpeed;

    vec4 noise1 = texture(Sampler0, uv + vec2(offset, offset));
    vec4 noise2 = texture(Sampler0, uv + vec2(0.25, 0.5) - vec2(offset, offset));
    float combinedNoise = noise1.r * noise2.r;
    float gradient = smoothstep(lerp(0.4, 0.0, vertexColor.r), lerp(1.3, 1.0, vertexColor.r), dist);
    float result = (combinedNoise * gradient) + gradient;

    float threshold = 0.5 - vertexColor.r * 0.4;

    if (result < threshold) {
        discard;
    }

    const float edgeSize = 0.05;
    vec3 finalColor = vec3(0.0);
    finalColor = mix(vec3(0.15f, 0.07f, 0.27f), finalColor, step(threshold + edgeSize * 5, result));
    finalColor = mix(vec3(0.76f, 0.3f, 0.78f), finalColor, step(threshold + edgeSize * 2, result));
    finalColor = mix(vec3(0.93f, 0.73f, 0.4f), finalColor, step(threshold + edgeSize, result));
    fragColor = vec4(finalColor, 1.0);

}
