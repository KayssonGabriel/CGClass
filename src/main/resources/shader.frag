#version 330 core

in vec3 Position;

out vec4 FragColor;

uniform vec4 Color;

void main() {
    FragColor = Color;
}