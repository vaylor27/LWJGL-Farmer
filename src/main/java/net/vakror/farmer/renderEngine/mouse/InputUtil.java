package net.vakror.farmer.renderEngine.mouse;

import net.vakror.farmer.DevTesting;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.gui.GuiTexture;
import net.vakror.farmer.renderEngine.listener.*;
import net.vakror.farmer.renderEngine.registry.registries.DefaultRegistries;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.*;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public class InputUtil {

    static {
        GLFW.glfwSetScrollCallback(Window.window, GLFWScrollCallback.create(InputUtil::glfwScrollCallback));
        GLFW.glfwSetCursorPosCallback(Window.window, GLFWCursorPosCallback.create(InputUtil::glfwCursorPos));
        GLFW.glfwSetMouseButtonCallback(Window.window, GLFWMouseButtonCallback.create(InputUtil::glfwClick));
        glfwSetKeyCallback(Window.window, InputUtil::onKeyPress);
    }

    public static final Map<Integer, KeyAction> keys = new HashMap<>();
    public static final MouseCoordinates currentMousePos = new MouseCoordinates(new Vector2f(0, 0));
    public static final MouseCoordinates previousCapturedPos = new MouseCoordinates(new Vector2f());
    public static final MouseCoordinates previousShownPos = new MouseCoordinates(new Vector2f());
    public static double yScrollValue = 0;
    public static double xScrollValue = 0;
    public static boolean mouseInNeedOfCorrection = true;
    public static MouseButton buttonDown = null;
    public static Vector2d initialMouseCoords = new Vector2d(94, 481);
    public static boolean isCursorDisabled = false;

    public static void glfwScrollCallback(long windowID, double dx, double dy) {
        xScrollValue = dx;
        yScrollValue = dy;

        Listeners.getListeners(MouseScrollListener.class).forEach((listener) -> listener.onScroll(windowID, dx, dy));
    }

    public static void glfwClick(long window, int button, int action, int mods) {
        for (MouseButton value : MouseButton.values()) {
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
        keys.put(key, KeyAction.fromInt(action));
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

    public static void disableCursor() {
        glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPos(Window.window, InputUtil.previousCapturedPos.getAsScreenCoords().x, InputUtil.previousCapturedPos.getAsScreenCoords().y);
        Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onCaptured);
        DevTesting.guis.forEach(GuiTexture::unHover);
        isCursorDisabled = true;
    }

    public static boolean isKeyPressed(int key) {
        KeyAction action = keys.get(key);
        return action == KeyAction.PRESS || action == KeyAction.REPEAT;
    }

    public static void enableCursor() {
        glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        if (previousShownPos.getAsScreenCoords().x != 0 && previousShownPos.getAsScreenCoords().y != 0) {
            glfwSetCursorPos(Window.window, InputUtil.previousShownPos.getAsScreenCoords().x, InputUtil.previousShownPos.getAsScreenCoords().y);
        } else {
            GLFWVidMode vidMode = org.lwjgl.glfw.GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            assert vidMode != null;
            float screenWidth = vidMode.width();
            float screenHeight = vidMode.height();
            glfwSetCursorPos(Window.window, screenWidth / 2, screenHeight / 2);
        }
        Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onReleased);
        isCursorDisabled = false;
    }

    public enum MouseButton {
        LEFT(GLFW.GLFW_MOUSE_BUTTON_LEFT),
        RIGHT(GLFW.GLFW_MOUSE_BUTTON_MIDDLE),
        MIDDLE(GLFW.GLFW_MOUSE_BUTTON_RIGHT);

        public final int id;
        MouseButton(int id) {
            this.id = id;
        }
    }

    public enum KeyAction {
        PRESS(GLFW_PRESS),
        REPEAT(GLFW_REPEAT),
        NOT_PRESSED(GLFW_RELEASE),
        NULL(0);

        private final int action;
        KeyAction(int action) {
            this.action = action;
        }

        public static KeyAction fromInt(int action) {
            return action == GLFW_PRESS ? PRESS: action == GLFW_REPEAT ? REPEAT: action == GLFW_RELEASE? NOT_PRESSED: NULL;
        }

        public int getAction() {
            return action;
        }
    }
}
