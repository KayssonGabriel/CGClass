package org.cgclass.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL45.*;

public class ShaderProgram {
    private final int programHandle;

    private final Map<String, Integer> uniforms = new HashMap<>();

    public ShaderProgram(int programHandle) {
        this.programHandle = programHandle;
    }

    private void checkUniformLocation(String name) {
        if (!uniforms.containsKey(name)) {
            uniforms.put(name, glGetUniformLocation(programHandle, name));
        }
    }

    public void setInt(String name, int value) {
        checkUniformLocation(name);
        glUniform1i(uniforms.get(name), value);
    }

    public void setFloat(String name, float value) {
        checkUniformLocation(name);
        glUniform1f(uniforms.get(name), value);
    }

    public void setVec2(String name, Vector2f value) {
        checkUniformLocation(name);
        glUniform2f(uniforms.get(name), value.x, value.y);
    }

    public void setVec3(String name, Vector3f value) {
        checkUniformLocation(name);
        glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }

    public void setVec4(String name, Vector4f value) {
        checkUniformLocation(name);
        glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    public void setMat4(String name, Matrix4f value) {
        checkUniformLocation(name);
        float[] matrix = new float[16];
        value.get(matrix);

        glUniformMatrix4fv(uniforms.get(name), false, matrix);
    }

    public void bind() {
        glUseProgram(programHandle);
    }

    public void unbind() {
        glUseProgram(0);
    }
}
