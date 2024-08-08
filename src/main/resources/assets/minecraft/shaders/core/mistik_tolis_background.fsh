#version 150

//uniform sampler2D Sampler0;
//in vec2 texCoord0;

out vec4 fragColor;

void main() {
    fragColor = vec4(texCoord0, 1.0, 1.0);
}
