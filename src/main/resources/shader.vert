#version 330 core

layout(location = 0) in vec3 aPos;

out vec3 Position;

uniform mat4 uTransformationMatrix;

void main() {
    vec4 worldPosition = vec4(aPos, 1.0) * uTransformationMatrix;

    Position = aPos;

    gl_Position = worldPosition;
}