package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.GameEntryPoint;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.listener.Listeners;
import net.vakror.farmer.renderEngine.listener.type.MouseCapturedListener;
import net.vakror.farmer.renderEngine.mouse.MouseCoordinates;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class InputUtil implements GameEntryPoint {
    public static final Map<Integer, KeyAction> keys = new HashMap<>();
    public static boolean isHoveringOverGui;
    public static final MouseCoordinates currentMousePos = new MouseCoordinates(new Vector2f(0, 0));
    public static final MouseCoordinates previousCapturedPos = new MouseCoordinates(new Vector2f());
    public static final MouseCoordinates previousShownPos = new MouseCoordinates(new Vector2f());
    public static double yScrollValue = 0;
    public static double xScrollValue = 0;
    public static boolean mouseInNeedOfCorrection = true;
    public static MouseButton buttonDown = null;
    public static Vector2d initialMouseCoords = new Vector2d(94, 481);
    public static boolean isCursorDisabled = false;
    public static boolean isWindowFocused = true;
    public static Vector2f windowCoords;
    public static boolean isWindowIconified = false;
    public static boolean isWindowMaximized = false;

    public static int getWindowWidth() {
        int[] width = new int[1];
        int[] height = new int[1];
        org.lwjgl.glfw.GLFW.glfwGetWindowSize(Window.window, width, height);
        return width[0];
    }

    public static int getWindowHeight() {
        int[] width = new int[1];
        int[] height = new int[1];
        org.lwjgl.glfw.GLFW.glfwGetWindowSize(Window.window, width, height);
        return height[0];
    }

    public static void disableCursor() {
        glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPos(Window.window, InputUtil.previousCapturedPos.getAsScreenCoords().x, InputUtil.previousCapturedPos.getAsScreenCoords().y);
        Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onCaptured);
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

    @Override
    public void initialize() {
        if (Options.startWithMouseCaptured()) {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            InputUtil.isCursorDisabled = true;
        } else {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onReleased);
            InputUtil.isCursorDisabled = false;
        }

        GLFW.glfwSetCursorPos(Window.window, initialMouseCoords.x, initialMouseCoords.y);
        currentMousePos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
        previousCapturedPos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
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

        public int getAction() {
            return action;
        }

        public static KeyAction fromInt(int action) {
            return action == GLFW_PRESS ? PRESS: action == GLFW_REPEAT ? REPEAT: action == GLFW_RELEASE? NOT_PRESSED: NULL;
        }
    }
}
