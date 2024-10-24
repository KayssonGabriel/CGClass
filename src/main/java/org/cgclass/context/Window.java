package org.cgclass.context;

import org.cgclass.event.OnWindowResizeEvent;
import org.gear.framework.application.Application;
import org.gear.framework.core.service.input.Input;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL45.*;

public class Window {

    private int width;
    private int height;
    private String title;
    private Platform platform;
    private long nativeWindow;
    private Vector4f clearColor = new Vector4f(0f, 0f, 0f, 1f);

    public Window(int width, int height, String title, Platform platform) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.platform = platform;
    }

    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        if (platform == Platform.MacOS) {
            glfwWindowHint(GLFW_OPENGL_CORE_PROFILE, GLFW_TRUE);
        }

        nativeWindow = glfwCreateWindow(width, height, title, 0, 0);
        Input.init(nativeWindow);

        if (nativeWindow == 0) {
            throw new IllegalStateException("Unable to create the GLFW window");
        }

        glfwMakeContextCurrent(nativeWindow);
        createCapabilities();

        setupGLFWCallbacks();
    }

    private void windowResizeCallback(long window, int width, int height) {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
        Application.dispatchEvent(new OnWindowResizeEvent(width, height));
    }

    private void setupGLFWCallbacks() {
        glfwSetFramebufferSizeCallback(nativeWindow, this::windowResizeCallback);
    }

    public void clear() {
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void swapBuffers() {
        glfwSwapBuffers(nativeWindow);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean isCloseRequested() {
        return glfwWindowShouldClose(nativeWindow);
    }
}
