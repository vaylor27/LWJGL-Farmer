package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.renderEngine.listener.MouseScrollListener;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import org.joml.Math;
import org.lwjgl.glfw.GLFW;

public class RunSpeedListener implements MouseScrollListener {
    public static float runSpeed = 50;

    @Override
    public void onScroll(long window, double x, double y) {
        if (InputUtil.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            runSpeed += (float) y;
            runSpeed = Math.clamp(20, 500, runSpeed);
        }
    }

    public static float getRunSpeed() {
        return runSpeed;
    }

    public static float getNegativeRunSpeed() {
        return -runSpeed;
    }
}
