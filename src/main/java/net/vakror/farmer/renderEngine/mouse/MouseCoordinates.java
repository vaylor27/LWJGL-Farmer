package net.vakror.farmer.renderEngine.mouse;

import net.vakror.farmer.renderEngine.util.CoordinateUtil;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class MouseCoordinates {
    private Vector2f screenCoordinates;

    public MouseCoordinates(Vector2f screenCoordinates) {
        this.screenCoordinates = screenCoordinates;
    }

    public void setScreenCoordinates(double x, double y) {
        this.screenCoordinates = new Vector2f((float) x, (float) y);
    }

    public Vector2f getAsNDC() {
        return CoordinateUtil.getAsNDC(screenCoordinates);
    }

    public Vector2f getAsScreenCoords() {
        return screenCoordinates;
    }
}
