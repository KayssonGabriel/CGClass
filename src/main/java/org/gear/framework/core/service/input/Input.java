package org.gear.framework.core.service.input;

import static org.lwjgl.glfw.GLFW.*;

public final class Input {

    private Input() {}

    private static long nativeWindow;

    public static void init(long nativeWindow) {
        Input.nativeWindow = nativeWindow;
    }

    public static boolean isKeyPressed(int keycode) {
        return glfwGetKey(nativeWindow, keycode) == GLFW_PRESS;
    }
}
