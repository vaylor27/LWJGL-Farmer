package net.vakror.farmer.renderEngine.util;

import net.vakror.farmer.renderEngine.Window;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class CoordinateUtil {
    public static Vector2f getAsNDC(Vector2f screenCoordinates) {
        int[] screenWidth = new int[1];
        int[] screenHeight = new int[1];
        GLFW.glfwGetWindowSize(Window.window, screenWidth, screenHeight);
        float x_ndc = screenCoordinates.x / screenWidth[0];
        float y_ndc = 1 - screenCoordinates.y / screenHeight[0];
        x_ndc -= 0.5f;
        y_ndc -= 0.5f;
        x_ndc *= 2;
        y_ndc *= 2;
        return new Vector2f(x_ndc, y_ndc);
    }
    public static Vector2f screenPercentToNDC(Vector2f screenCoordinates) {
        int[] screenWidth = new int[1];
        int[] screenHeight = new int[1];
        GLFW.glfwGetWindowSize(Window.window, screenWidth, screenHeight);
        float x = (2.0f * (screenWidth[0] * screenCoordinates.x)) / screenWidth[0] - 1.0f;
        float y = 1.0f - (2.0f * (screenHeight[0] * screenCoordinates.y)) / screenHeight[0];

        return new Vector2f(x, y);
    }

    public static Vector2f getAsNDC1(Vector2f screenCoordinates) {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        assert vidMode != null;
        float screenWidth = vidMode.width();
        float screenHeight = vidMode.height();
        float x_ndc = (2 * screenCoordinates.x) / screenWidth - 1;
        float y_ndc = (2 * screenCoordinates.y) / screenHeight - 1;
        return new Vector2f(x_ndc, y_ndc);
    }
}