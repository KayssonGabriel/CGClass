package org.cgclass.context;

public class GraphicContext {

    private Window window;

    public GraphicContext(int width, int height, String title, Platform platform) {
        this.window = new Window(width, height, title, platform);
        window.init();
    }

    public void clear() {
        window.clear();
    }

    public void swapBuffers() {
        window.swapBuffers();
    }

    public void pollEvents() {
        window.pollEvents();
    }

    public boolean isCloseRequested() {
        return window.isCloseRequested();
    }
}
