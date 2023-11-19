package net.vakror.farmer.renderEngine.listeners;

import net.vakror.farmer.FarmerGameMain;
import net.vakror.farmer.Options;
import net.vakror.farmer.renderEngine.Window;
import net.vakror.farmer.renderEngine.listener.*;
import net.vakror.farmer.renderEngine.mouse.InputUtil;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import static net.vakror.farmer.renderEngine.mouse.InputUtil.*;
import static net.vakror.farmer.renderEngine.camera.Camera.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

public class OnInit implements InitializeListener {

    @Override
    public void onInit() {
        setupMouse();
        setupListeners();
    }

    private void setupMouse() {
        if (Options.startWithMouseCaptured()) {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            InputUtil.isCursorDisabled = true;
        } else {
            glfwSetInputMode(Window.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            Listeners.getListeners(MouseCapturedListener.class).forEach(MouseCapturedListener::onReleased);
            InputUtil.isCursorDisabled = false;
        }

        fixMousePos();
    }

    public static void setupListeners() {
        Listeners.addListener(MouseCapturedListener.class, new CorrectCameraOnCapture());
        Listeners.addListener(MouseMovementListener.class, new OnGuiHover());
        Listeners.addListener(MouseButtonListener.class, new OnGuiClick());
        Listeners.addListener(MouseScrollListener.class, new RunSpeedListener());
    }

    public static void fixMousePos() {
        GLFW.glfwSetCursorPos(Window.window, initialMouseCoords.x, initialMouseCoords.y);
        FarmerGameMain.camera.data.angleAroundFocused = 0;
        FarmerGameMain.camera.data.pitch = 20;
        FarmerGameMain.camera.data.yaw = 180;
        FarmerGameMain.camera.currentCameraFocusPoint = new Vector2f(500, 500);
        lastValueX = (float) initialMouseCoords.x;
        lastValueY = (float) initialMouseCoords.y;
        currentMousePos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
        previousCapturedPos.setScreenCoordinates(initialMouseCoords.x, initialMouseCoords.y);
    }
}
