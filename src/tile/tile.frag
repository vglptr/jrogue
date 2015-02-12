#version 150 core

in vec3 vertexColor;
in vec2 Texcoord;
uniform sampler2D tex;

out vec4 fragColor;

void main() {
    fragColor = texture(tex, Texcoord);
}