package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.listener.Listeners;
import net.vakror.farmer.renderEngine.listener.type.*;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;

import static net.vakror.farmer.renderEngine.util.InputUtil.*;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

public class Callbacks {
    static {
        GLFW.glfwSetScrollCallback(Window.window, GLFWScrollCallback.create(Callbacks::glfwScrollCallback));
        GLFW.glfwSetWindowFocusCallback(Window.window, GLFWWindowFocusCallback.create(Callbacks::glfwFocusCallback));
        GLFW.glfwSetWindowPosCallback(Window.window, GLFWWindowPosCallback.create(Callbacks::glfwPosCallback));
        GLFW.glfwSetWindowIconifyCallback(Window.window, GLFWWindowIconifyCallback.create(Callbacks::glfwIconifyCallback));
        GLFW.glfwSetWindowMaximizeCallback(Window.window, GLFWWindowMaximizeCallback.create(Callbacks::glfwMaximizeCallback));
        GLFW.glfwSetFramebufferSizeCallback(Window.window, GLFWFramebufferSizeCallback.create(Callbacks::glfwWindowResizeCallback));
        GLFW.glfwSetCursorPosCallback(Window.window, GLFWCursorPosCallback.create(Callbacks::glfwCursorPos));
        GLFW.glfwSetMouseButtonCallback(Window.window, GLFWMouseButtonCallback.create(Callbacks::glfwClick));
        glfwSetKeyCallback(Window.window, Callbacks::onKeyPress);
    }

    private static void glfwMaximizeCallback(long window, boolean maximized) {
        isWindowMaximized = maximized;
        Listeners.getListeners(WindowMaximizeListener.class).forEach(windowPosListener -> windowPosListener.onWindowMaximized(maximized));
    }

    private static void glfwIconifyCallback(long window, boolean iconified) {
        isWindowIconified = iconified;
        Listeners.getListeners(WindowIconifyListener.class).forEach(windowPosListener -> windowPosListener.onWindowIconified(iconified));
    }

    private static void glfwPosCallback(long window, int x, int y) {
        windowCoords = new Vector2f(x, y);
        Listeners.getListeners(WindowPosListener.class).forEach(windowPosListener -> windowPosListener.onWindowMove(x, y));
    }


    public static void glfwScrollCallback(long windowID, double dx, double dy) {
        xScrollValue = dx;
        yScrollValue = dy;
        Listeners.getListeners(MouseScrollListener.class).forEach((listener) -> listener.onScroll(windowID, dx, dy));
    }

    public static void glfwFocusCallback(long windowID, boolean focused) {
        isWindowFocused = focused;
        Listeners.getListeners(WindowFocusListener.class).forEach(listener -> {
            if (focused) {
                listener.onWindowFocus();
            } else {
                listener.onWindowUnFocus();
            }
        });
    }

    public static void glfwWindowResizeCallback(long windowId, int width, int height) {
        Listeners.getListeners(WindowResizeListener.class).forEach(listener -> listener.onWindowResize(width, height));
    }

    public static void glfwClick(long window, int button, int action, int mods) {
        for (InputUtil.MouseButton value : InputUtil.MouseButton.values()) {
            if (value.id == button) {
                buttonDown = value;
                break;
            }
        }
        Listeners.getListeners(MouseButtonListener.class).forEach((listener) -> listener.onClick(window, button, action));
    }

    private static void onKeyPress(long window, int key, int scancode, int action, int mods) {
        DefaultRegistries.KEYBINDINGS.forEach((keyBinding -> {
            if (keyBinding.key == key) {
                keyBinding.execute(scancode, action, mods);
            }
        }));
        Listeners.getListeners(KeyPressListener.class).forEach(listener -> listener.onPress(window, key, scancode, action, mods));
        keys.put(key, InputUtil.KeyAction.fromInt(action));
    }

    public static void glfwCursorPos(long windowID, double dx, double dy) {
        if (mouseInNeedOfCorrection) {
            dx = initialMouseCoords.x;
            dy = initialMouseCoords.y;
            GLFW.glfwSetCursorPos(windowID, dx, dy);
            mouseInNeedOfCorrection = false;
        }
        if (isCursorDisabled) {
            previousCapturedPos.setScreenCoordinates(dx, dy);
        } else {
            previousShownPos.setScreenCoordinates(dx, dy);
        }
        currentMousePos.setScreenCoordinates(dx, dy);

        double finalDx = dx;
        double finalDy = dy;
        Listeners.getListeners(MouseMovementListener.class).forEach(listener -> listener.onMouseMove(windowID, finalDx, finalDy));
    }

    public static void init() {

    }
}
